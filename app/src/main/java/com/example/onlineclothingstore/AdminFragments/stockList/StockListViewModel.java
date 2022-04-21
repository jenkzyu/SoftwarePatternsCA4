package com.example.onlineclothingstore.AdminFragments.stockList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.onlineclothingstore.Constants.Constants;
import com.example.onlineclothingstore.Model.StockModel;

import java.util.List;

public class StockListViewModel extends ViewModel {
    /**
     * Observer pattern using MutableLiveData as a pattern from ViewModel to Activity
     * to make its value writable or can be change anytime.
     */
    private MutableLiveData<List<StockModel>> listMutableLiveData;

    public StockListViewModel() {

    }

    public MutableLiveData<List<StockModel>> getListMutableLiveData() {
        if (listMutableLiveData == null) {
            listMutableLiveData = new MutableLiveData<>();

        }
        listMutableLiveData.setValue(Constants.categorySelected.getStocks());
        return listMutableLiveData;
    }


}