package com.example.onlineclothingstore.Callback;

import com.example.onlineclothingstore.Model.StockModel;

import java.util.List;

public interface IStockCallbackListener {
    void onStockLoadSuccess(List<StockModel> stockModelList);
    void onStockLoadFailed(String message);
}
