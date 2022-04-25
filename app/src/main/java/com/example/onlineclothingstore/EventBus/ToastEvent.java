package com.example.onlineclothingstore.EventBus;

import com.example.onlineclothingstore.Constants.Constants;

public class ToastEvent {
    private Constants.ACTION action;
    private boolean isFromStockList;

    public ToastEvent(Constants.ACTION action, boolean isFromStockList) {
        this.action = action;
        this.isFromStockList = isFromStockList;
    }

    public Constants.ACTION getAction() {
        return action;
    }

    public void setAction(Constants.ACTION action) {
        this.action = action;
    }

    public boolean isFromStockList() {
        return isFromStockList;
    }

    public void isFromStockList(boolean fromStockList) {
        isFromStockList = fromStockList;
    }
}
