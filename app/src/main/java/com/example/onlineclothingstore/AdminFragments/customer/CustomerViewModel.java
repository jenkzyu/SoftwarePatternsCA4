package com.example.onlineclothingstore.AdminFragments.customer;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.onlineclothingstore.Callback.IOrderCallbackListener;
import com.example.onlineclothingstore.Callback.IUserCallbackListener;
import com.example.onlineclothingstore.Constants.Constants;
import com.example.onlineclothingstore.Model.OrderModel;
import com.example.onlineclothingstore.Model.UserModel;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class CustomerViewModel extends ViewModel implements IUserCallbackListener{

    private MutableLiveData<List<UserModel>> userModelMutableList;
    //private MutableLiveData<List<OrderModel>> orListMutableLiveData;
    private MutableLiveData<String> messageError;
    private IUserCallbackListener listener;

    //private IOrderCallbackListener listener;

    public CustomerViewModel() {
        listener = this;

    }

    public MutableLiveData<List<UserModel>> getUserModelMutableList() {
        if (userModelMutableList == null){
            userModelMutableList = new MutableLiveData<>();
            messageError = new MutableLiveData<>();
            loadAllUsers();
        }
        return userModelMutableList;
    }

    private void loadAllUsers() {
        List<UserModel> list = new ArrayList<>();
        DatabaseReference userRef = FirebaseDatabase.getInstance().getReference(Constants.USER_REFERENCES);
        userRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot: snapshot.getChildren()){
                    UserModel userModel = userSnapshot.getValue(UserModel.class);
                    userModel.setUid(userSnapshot.getKey());
                    list.add(userModel);
                }
                listener.onUserLoadSuccess(list);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
               listener.onUserLoadFailed(error.getMessage());
            }
        });
    }



//    public MutableLiveData<List<OrderModel>> getOrListMutableLiveData() {
//        if (orListMutableLiveData ==null){
//            orListMutableLiveData = new MutableLiveData<>();
//            loadFromDb();
//        }
//        return orListMutableLiveData;
//    }
//
//    public void loadFromDb() {
//        List<OrderModel> orderModelList = new ArrayList<>();
//        Query orderRef = FirebaseDatabase.getInstance().getReference(Constants.ORDER_REF)
//                .orderByChild("orderDate");
////                .child(Constants.currentUser.getUid())
//        orderRef.addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                if (snapshot.exists()) {
//                    for (DataSnapshot dataSnapshot : snapshot.getChildren()) {
//                        OrderModel orderModel = dataSnapshot.getValue(OrderModel.class);
//                        orderModel.setKey(dataSnapshot.getKey());
//                        orderModelList.add(orderModel);
//                    }
//                    listener.onOrderLoadSuccess(orderModelList);
//                } else {
//                    listener.onOrderLoadFailed("Error");
//                }
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//                listener.onOrderLoadFailed(error.getMessage());
//            }
//        });
//    }

    public MutableLiveData<String> getMessageError() {
        return messageError;
    }

    @Override
    public void onUserLoadSuccess(List<UserModel> userModelList) {
        userModelMutableList.setValue(userModelList);
    }

    @Override
    public void onUserLoadFailed(String message) {
        messageError.setValue(message);

    }


}