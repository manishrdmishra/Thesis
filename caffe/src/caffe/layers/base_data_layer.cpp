#include <boost/thread.hpp>
#include <vector>

#include "caffe/blob.hpp"
#include "caffe/data_transformer.hpp"
#include "caffe/internal_thread.hpp"
#include "caffe/layer.hpp"
#include "caffe/layers/base_data_layer.hpp"
#include "caffe/proto/caffe.pb.h"
#include "caffe/util/blocking_queue.hpp"

namespace caffe {

template<typename Dtype>
BaseDataLayer<Dtype>::BaseDataLayer(const LayerParameter& param) :
		Layer<Dtype>(param), transform_param_(param.transform_param()) {
}

template<typename Dtype>
void BaseDataLayer<Dtype>::LayerSetUp(const vector<Blob<Dtype>*>& bottom,
		const vector<Blob<Dtype>*>& top) {
	if (top.size() == 1) {
		output_labels_ = false;
	} else {
		output_labels_ = true;
	}
	data_transformer_.reset(
			new DataTransformer<Dtype>(transform_param_, this->phase_,
					this->device_));
	data_transformer_->InitRand();
	// The subclasses should setup the size of bottom and top
	DataLayerSetUp(bottom, top);
}

template<typename Dtype>
BasePrefetchingDataLayer<Dtype>::BasePrefetchingDataLayer(
		const LayerParameter& param) :
		BaseDataLayer<Dtype>(param), prefetch_free_(), prefetch_full_() {
	for (int_tp i = 0; i < PREFETCH_COUNT; ++i) {
		prefetch_free_.push(&prefetch_[i]);
	}
}

template<typename Dtype>
void BasePrefetchingDataLayer<Dtype>::LayerSetUp(
		const vector<Blob<Dtype>*>& bottom, const vector<Blob<Dtype>*>& top) {
	BaseDataLayer<Dtype>::LayerSetUp(bottom, top);
	// Before starting the prefetch thread, we make cpu_data and gpu_data
	// calls so that the prefetch thread does not accidentally make simultaneous
	// cudaMalloc calls when the main thread is running. In some GPUs this
	// seems to cause failures if we do not so.
	for (int_tp i = 0; i < PREFETCH_COUNT; ++i) {
		prefetch_[i].data_.mutable_cpu_data();
		if (this->output_labels_) {
			prefetch_[i].label_.mutable_cpu_data();
		}

	}
#ifndef CPU_ONLY
	if (Caffe::mode() == Caffe::GPU) {
		for (int_tp i = 0; i < PREFETCH_COUNT; ++i) {
			prefetch_[i].data_.mutable_gpu_data();
			if (this->output_labels_) {
				prefetch_[i].label_.mutable_gpu_data();
			}
		}
	}
#endif
	DLOG(INFO) << "Initializing prefetch";
	this->data_transformer_->InitRand();
	StartInternalThread(this->get_device());
	DLOG(INFO) << "Prefetch initialized.";
}

template<typename Dtype>
void BasePrefetchingDataLayer<Dtype>::InternalThreadEntry() {
#ifndef CPU_ONLY
#ifdef USE_CUDA
	cudaStream_t stream;
	if (Caffe::mode() == Caffe::GPU) {
		if (this->get_device()->backend() == BACKEND_CUDA) {
			CUDA_CHECK(cudaStreamCreateWithFlags(&stream, cudaStreamNonBlocking));
		}
	}
#endif  // USE_CUDA
#endif  // !CPU_ONLY
	try {
		while (!must_stop()) {
			Batch<Dtype>* batch = prefetch_free_.pop();
			load_batch(batch);
#ifndef CPU_ONLY
#ifdef USE_CUDA
			if (Caffe::mode() == Caffe::GPU) {
				if (this->get_device()->backend() == BACKEND_CUDA) {
					batch->data_.data().get()->async_gpu_push(stream);
					CUDA_CHECK(cudaStreamSynchronize(stream));
				}
			}
#endif  // USE_CUDA
#endif  // !CPU_ONLY
			prefetch_full_.push(batch);
		}
	} catch (boost::thread_interrupted&) {
		// Interrupted exception is expected on shutdown
	}
#ifndef CPU_ONLY
#ifdef USE_CUDA
	if (Caffe::mode() == Caffe::GPU) {
		if (this->get_device()->backend() == BACKEND_CUDA) {
			CUDA_CHECK(cudaStreamDestroy(stream));
		}
	}
#endif  // USE_CUDA
#endif  // !CPU_ONLY
}

template<typename Dtype>
void BasePrefetchingDataLayer<Dtype>::Forward_cpu(
		const vector<Blob<Dtype>*>& bottom, const vector<Blob<Dtype>*>& top) {
	Batch<Dtype>* batch = prefetch_full_.pop("Data layer prefetch queue empty");
	// Reshape to loaded data.
	top[0]->ReshapeLike(batch->data_);
	// Copy the data
	caffe_cpu_copy(batch->data_.count(), batch->data_.cpu_data(),
			top[0]->mutable_cpu_data());
	DLOG(INFO) << "Prefetch copied";
	if (this->output_labels_) {
		// Reshape to loaded labels.
		top[1]->ReshapeLike(batch->label_);
		// Copy the labels.
		caffe_cpu_copy(batch->label_.count(), batch->label_.cpu_data(),
				top[1]->mutable_cpu_data());
	}

	prefetch_free_.push(batch);
}

template<typename Dtype>
void BasePrefetchingDenseDataLayer<Dtype>::LayerSetUp(
		const vector<Blob<Dtype>*>& bottom, const vector<Blob<Dtype>*>& top) {
	BaseDataLayer<Dtype>::LayerSetUp(bottom, top);
	// Now, start the prefetch thread. Before calling prefetch, we make two
	// cpu_data calls so that the prefetch thread does not accidentally make
	// simultaneous cudaMalloc calls when the main thread is running. In some
	// GPUs this seems to cause failures if we do not so.
	this->prefetch_data_.mutable_cpu_data();
	if (this->output_labels_) {
		this->prefetch_label_.mutable_cpu_data();
	}
	DLOG(INFO) << "Initializing prefetch";
	this->CreatePrefetchThread();
	DLOG(INFO) << "Prefetch initialized.";
}

template<typename Dtype>
void BasePrefetchingDenseDataLayer<Dtype>::CreatePrefetchThread() {
	this->data_transformer_->InitRand();
	//CHECK(StartInternalThread(this->get_device())) << "Thread execution failed";
	StartInternalThread(this->get_device());
}

template<typename Dtype>
void BasePrefetchingDenseDataLayer<Dtype>::JoinPrefetchThread() {
	//CHECK(WaitForInternalThreadToExit()) << "Thread joining failed";
	StopInternalThread();
}

template<typename Dtype>
void BasePrefetchingDenseDataLayer<Dtype>::Forward_cpu(
		const vector<Blob<Dtype>*>& bottom, const vector<Blob<Dtype>*>& top) {
	// First, join the thread
	JoinPrefetchThread();
	DLOG(INFO) << "Thread joined";
	// Reshape to loaded data.
	top[0]->ReshapeLike(prefetch_data_);
	// Copy the data
	caffe_copy(prefetch_data_.count(), prefetch_data_.cpu_data(),
			top[0]->mutable_cpu_data());
	DLOG(INFO) << "Prefetch copied";
	if (this->output_labels_) {
		// Reshape to loaded labels.
		top[1]->ReshapeLike(prefetch_label_);
		// Copy the labels.
		caffe_copy(prefetch_label_.count(), prefetch_label_.cpu_data(),
				top[1]->mutable_cpu_data());
	}
	// Start a new prefetch thread
	DLOG(INFO) << "CreatePrefetchThread";
	CreatePrefetchThread();
}

#ifdef CPU_ONLY
STUB_GPU_FORWARD(BasePrefetchingDataLayer, Forward);
STUB_GPU_FORWARD(BasePrefetchingDenseDataLayer, Forward);
#endif

INSTANTIATE_CLASS(BaseDataLayer);
INSTANTIATE_CLASS(BasePrefetchingDataLayer);
INSTANTIATE_CLASS(BasePrefetchingDenseDataLayer);

} // namespace caffe
