package com.example.onlineclothingstore.CustomerFragment.home.customerStockList;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.onlineclothingstore.Constants.Constants;
import com.example.onlineclothingstore.Model.RatingModel;
import com.example.onlineclothingstore.Model.StockModel;

import java.util.List;

public class CustomerStockListViewModel extends ViewModel {
   private MutableLiveData<List<StockModel>> stockMutableLiveData;
   private MutableLiveData<List<RatingModel>> ratingListMutableLiveData;

    public CustomerStockListViewModel() {
        ratingListMutableLiveData = new MutableLiveData<>();
    }

//    public void setRatingModel(RatingModel ratingModel){
//        if (ratingListMutableLiveData !=null)
//            ratingListMutableLiveData.setValue(ratingModel);
//    }

    public MutableLiveData<List<RatingModel>> getRatingListMutableLiveData() {
        return ratingListMutableLiveData;
    }

    public MutableLiveData<List<StockModel>> getStockMutableLiveData() {
        if (stockMutableLiveData == null){
            stockMutableLiveData = new MutableLiveData<>();
        }
        stockMutableLiveData.setValue(Constants.categorySelected.getStocks());
        return stockMutableLiveData;
    }
}