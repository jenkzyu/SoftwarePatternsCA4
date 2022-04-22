package com.example.onlineclothingstore.CustomerFragments.customerStockList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.onlineclothingstore.Constants.Constants;
import com.example.onlineclothingstore.Model.StockModel;

import java.util.List;

public class CustomerStockListViewModel extends ViewModel {
   private MutableLiveData<List<StockModel>> stockMutableLiveData;


    public CustomerStockListViewModel() {

    }


    public MutableLiveData<List<StockModel>> getStockMutableLiveData() {
        if (stockMutableLiveData == null){
            stockMutableLiveData = new MutableLiveData<>();
        }
        stockMutableLiveData.setValue(Constants.categorySelected.getStocks());
        return stockMutableLiveData;
    }
}