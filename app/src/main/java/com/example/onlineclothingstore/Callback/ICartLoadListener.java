package com.example.onlineclothingstore.Callback;

import com.example.onlineclothingstore.Model.CartModel;

import java.util.List;

public interface ICartLoadListener {
    void onCartLoadSuccess(List<CartModel> cartModelList);

    void onCartLoadFailed(String message);
}
