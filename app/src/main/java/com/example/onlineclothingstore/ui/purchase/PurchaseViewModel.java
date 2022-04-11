package com.example.onlineclothingstore.ui.purchase;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PurchaseViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public PurchaseViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is slideshow fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}