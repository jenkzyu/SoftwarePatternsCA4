package com.example.onlineclothingstore.ui.home;

import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.onlineclothingstore.Callback.IStockCallbackListener;
import com.example.onlineclothingstore.Model.StockModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class HomeViewModel extends ViewModel implements IStockCallbackListener {
    /**
     * Observer pattern using MutableLiveData as a pattern from ViewModel to Activity
     * to make its value writable or can be change anytime.
     */
    private MutableLiveData<List<StockModel>> listMutableLiveData;
    private MutableLiveData<String> messageError = new MutableLiveData<>();
    private IStockCallbackListener stockCallbackListener;

    public HomeViewModel() {
        stockCallbackListener = this;

    }

    public MutableLiveData<List<StockModel>> getListMutableLiveData() {
        if (listMutableLiveData == null) {
            listMutableLiveData = new MutableLiveData<>();
            messageError = new MutableLiveData<>();
            loadStocks();
        }
        return listMutableLiveData;
    }

    public MutableLiveData<String> getMessageError() {
        return messageError;
    }

    public void loadStocks() {
        List<StockModel> stockModelList = new ArrayList<>();
        DatabaseReference stockRef = FirebaseDatabase.getInstance().getReference("Stocks");
        stockRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){
                    for (DataSnapshot stockSnapshot: snapshot.getChildren()){
                        StockModel model = stockSnapshot.getValue(StockModel.class);
                        //model.setStock_id(stockSnapshot.getKey());
                        stockModelList.add(model);
                    }
                    if (stockModelList.size()>0){
                        stockCallbackListener.onStockLoadSuccess(stockModelList);
                    }else{
                        stockCallbackListener.onStockLoadFailed("Empty");
                    }
                }else {
                    stockCallbackListener.onStockLoadFailed("error on loading list");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                stockCallbackListener.onStockLoadFailed(error.getMessage());
            }
        });
    }


    @Override
    public void onStockLoadSuccess(List<StockModel> stockModelList) {
        listMutableLiveData.setValue(stockModelList);
    }

    @Override
    public void onStockLoadFailed(String message) {
        messageError.setValue(message);
    }
    //code something here to load all stock items
}