#ifndef CAFFE_DENSE_IMAGE_DATA_LAYER_HPP_
#define CAFFE_DENSE_IMAGE_DATA_LAYER_HPP_

#include <string>
#include <utility>
#include <vector>

#include "caffe/blob.hpp"
#include "caffe/data_transformer.hpp"
#include "caffe/internal_thread.hpp"
#include "caffe/layer.hpp"
#include "caffe/layers/base_data_layer.hpp"
#include "caffe/proto/caffe.pb.h"
#include "boost/scoped_ptr.hpp"
#include "caffe/common.hpp"
#include "caffe/filler.hpp"
#include "caffe/util/db.hpp"


namespace caffe {
template <typename Dtype>
class DenseImageDataLayer : public BasePrefetchingDenseDataLayer<Dtype> {
 public:
  explicit DenseImageDataLayer(const LayerParameter& param)
      : BasePrefetchingDenseDataLayer<Dtype>(param) {}
  virtual ~DenseImageDataLayer<Dtype>();
  virtual void DataLayerSetUp(const vector<Blob<Dtype>*>& bottom,
      const vector<Blob<Dtype>*>& top);
//
  virtual inline const char* type() const { return "DenseImageData"; }
  virtual inline int ExactNumBottomBlobs() const { return 0; }
  virtual inline int ExactNumTopBlobs() const { return 2; }
//
 protected:
  shared_ptr<Caffe::RNG> prefetch_rng_;
  virtual void ShuffleImages();
  virtual void load_batch(Batch<Dtype>* batch);

  vector<std::pair<std::string, std::string> > lines_;
  int lines_id_;

  Blob<Dtype> transformed_label_;
};


}// namespace caffe
#endif // CAFFE_DENSSE_IMAGE_DATA_LAYER_HPP_