package com.example.onlineclothingstore.CustomerFragments.customerStockReviews;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.onlineclothingstore.Model.RatingModel;

import java.util.List;

public class StockReviewsViewModel extends ViewModel {
    private MutableLiveData<List<RatingModel>> ratingModelMutableLiveData;

    public StockReviewsViewModel() {
        ratingModelMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<RatingModel>> getRatingModelMutableLiveData() {
        return ratingModelMutableLiveData;
    }

    public void setCommentList(List<RatingModel> ratingModelList) {
        ratingModelMutableLiveData.setValue(ratingModelList);

    }
}