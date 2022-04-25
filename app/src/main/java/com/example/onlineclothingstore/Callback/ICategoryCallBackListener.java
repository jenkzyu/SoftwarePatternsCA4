package com.example.onlineclothingstore.Callback;

import com.example.onlineclothingstore.Model.CategoryModel;

import java.util.List;

public interface ICategoryCallBackListener {
    void onCategoryLoadSuccess(List<CategoryModel> categoryModelList);
    void onCategoryLoadFailed(String message);
}
