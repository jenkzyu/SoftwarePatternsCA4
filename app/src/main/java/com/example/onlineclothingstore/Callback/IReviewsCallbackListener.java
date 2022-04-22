package com.example.onlineclothingstore.Callback;

import com.example.onlineclothingstore.Model.RatingModel;

import java.util.List;

public interface IReviewsCallbackListener {
    void onReviewLoadSuccess(List<RatingModel> ratingModelList);
    void onReviewLoadFailed(String message);
}
