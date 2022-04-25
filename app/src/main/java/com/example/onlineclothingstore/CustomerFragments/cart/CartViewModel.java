package com.example.onlineclothingstore.CustomerFragments.cart;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.onlineclothingstore.Callback.ICartLoadListener;
import com.example.onlineclothingstore.Constants.Constants;
import com.example.onlineclothingstore.Model.CartModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CartViewModel extends ViewModel implements ICartLoadListener {
   private MutableLiveData<List<CartModel>> carListMutableLiveData;
    private MutableLiveData<String> messageError = new MutableLiveData<>();
   private ICartLoadListener listener;

    public CartViewModel() {
     listener = this;
    }

    public MutableLiveData<List<CartModel>> getCarListMutableLiveData() {
        if (carListMutableLiveData == null){
            carListMutableLiveData = new MutableLiveData<>();
            loadCartFromDB();
        }
        return carListMutableLiveData;
    }

    public void loadCartFromDB() {
        List<CartModel> cartModelList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference(Constants.CART_REF)
                .child(Constants.currentUser.getUid())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            for (DataSnapshot cartSnapshot: snapshot.getChildren()){
                                CartModel cartModel = cartSnapshot.getValue(CartModel.class);
                                cartModel.setKey(cartSnapshot.getKey());
                                cartModelList.add(cartModel);
                            }
                            listener.onCartLoadSuccess(cartModelList);
                        }else{
                            listener.onCartLoadFailed("Cart Empty");
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        listener.onCartLoadFailed(error.getMessage());
                    }
                });
    }

    public MutableLiveData<String> getMessageError() {
        return messageError;
    }

    @Override
    public void onCartLoadSuccess(List<CartModel> cartModelList) {
        carListMutableLiveData.setValue(cartModelList);
    }

    @Override
    public void onCartLoadFailed(String message) {
        messageError.setValue(message);
    }
}