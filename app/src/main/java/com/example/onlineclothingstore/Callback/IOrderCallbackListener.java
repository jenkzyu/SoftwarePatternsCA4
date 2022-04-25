package com.example.onlineclothingstore.Callback;

import com.example.onlineclothingstore.Model.OrderModel;

import java.util.List;

public interface IOrderCallbackListener {
    void onOrderLoadSuccess(List<OrderModel> orderModelList);

    void onOrderLoadFailed(String message);
}
