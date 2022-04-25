package com.example.onlineclothingstore.AdminFragments.orderdetails;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.onlineclothingstore.Model.OrderModel;

import java.util.List;

public class OrderDetailsViewModel extends ViewModel {

    private MutableLiveData<List<OrderModel>> orListMutableLiveData;

    public OrderDetailsViewModel() {
        orListMutableLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<List<OrderModel>> getOrListMutableLiveData() {
        return orListMutableLiveData;
    }

    public void setOrderList(List<OrderModel> orderModelList){
        orListMutableLiveData.setValue(orderModelList);
    }
}