#include <vector>

#include "caffe/layers/base_data_layer.hpp"

namespace caffe {

template<typename Dtype>
void BasePrefetchingDataLayer<Dtype>::Forward_gpu(
    const vector<Blob<Dtype>*>& bottom, const vector<Blob<Dtype>*>& top) {

  Batch<Dtype>* batch = prefetch_full_.pop("Data layer prefetch queue empty");

  if (this->device_->backend() == BACKEND_CUDA) {
#ifdef USE_CUDA
    // Reshape to loaded data.
    top[0]->ReshapeLike(batch->data_);
    // Copy the data
    caffe_copy(batch->data_.count(), batch->data_.gpu_data(),
        top[0]->mutable_gpu_data());
    if (this->output_labels_) {
      // Reshape to loaded labels.
      top[1]->ReshapeLike(batch->label_);
      // Copy the labels.
      caffe_copy(batch->label_.count(), batch->label_.gpu_data(),
          top[1]->mutable_gpu_data());
    }
    // Ensure the copy is synchronous wrt the host, so that the next batch isn't
    // copied in meanwhile.
    CUDA_CHECK(cudaStreamSynchronize(cudaStreamDefault));
#endif  // USE_CUDA
  } else {
#ifdef USE_GREENTEA
    viennacl::ocl::context &ctx = viennacl::ocl::get_context(
        this->device_->id());

    // Reshape to loaded data.
    top[0]->ReshapeLike(batch->data_);
    // Copy the data
    greentea_copy<Dtype>(batch->data_.count(),
                         (cl_mem) (batch->data_.gpu_data()), 0,
                         (cl_mem) (top[0]->mutable_gpu_data()), 0, &ctx);
    if (this->output_labels_) {
      // Reshape to loaded labels.
      top[1]->ReshapeLike(batch->label_);
      // Copy the labels.
      greentea_copy<Dtype>(batch->label_.count(),
                           (cl_mem) (batch->label_.gpu_data()), 0,
                           (cl_mem) (top[1]->mutable_gpu_data()), 0, &ctx);
    }
#endif  // USE_GREENTEA
  }

  prefetch_free_.push(batch);
}

<<<<<<< HEAD

template <typename Dtype>
void BasePrefetchingDenseDataLayer<Dtype>::Forward_gpu(
    const vector<Blob<Dtype>*>& bottom, const vector<Blob<Dtype>*>& top) {
  // First, join the thread
  JoinPrefetchThread();
  // Reshape to loaded data.
  top[0]->ReshapeLike(this->prefetch_data_);
  // Copy the data
  caffe_copy(prefetch_data_.count(), prefetch_data_.cpu_data(),
      top[0]->mutable_gpu_data());
  if (this->output_labels_) {
    // Reshape to loaded labels.
    top[1]->ReshapeLike(prefetch_label_);
    // Copy the labels.
    caffe_copy(prefetch_label_.count(), prefetch_label_.cpu_data(),
        top[1]->mutable_gpu_data());
  }
  // Start a new prefetch thread
  CreatePrefetchThread();
}

INSTANTIATE_LAYER_GPU_FORWARD(BasePrefetchingDenseDataLayer);
=======
>>>>>>> add_extract_patch_module
INSTANTIATE_LAYER_GPU_FORWARD(BasePrefetchingDataLayer);

}  // namespace caffe
