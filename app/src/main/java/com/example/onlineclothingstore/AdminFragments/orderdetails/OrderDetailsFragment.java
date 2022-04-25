package com.example.onlineclothingstore.AdminFragments.orderdetails;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.onlineclothingstore.Adapter.OrdersAdapter;
import com.example.onlineclothingstore.Callback.IOrderCallbackListener;
import com.example.onlineclothingstore.Constants.Constants;
import com.example.onlineclothingstore.Model.OrderModel;
import com.example.onlineclothingstore.Model.RatingModel;
import com.example.onlineclothingstore.R;
import com.example.onlineclothingstore.databinding.FragmentOrderDetailsBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class OrderDetailsFragment extends BottomSheetDialogFragment implements IOrderCallbackListener {

    private OrderDetailsViewModel mViewModel;
    private FragmentOrderDetailsBinding binding;
    private IOrderCallbackListener callbackListener;
    private List<OrderModel> orderModelList;
    OrdersAdapter ordersAdapter;

    public OrderDetailsFragment() {
        callbackListener = this;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel = new ViewModelProvider(this).get(OrderDetailsViewModel.class);
        binding = FragmentOrderDetailsBinding.inflate(inflater, container, false);

        loadOrder();

        loadViews();

        mViewModel.getOrListMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<OrderModel>>() {
            @Override
            public void onChanged(List<OrderModel> orderModels) {
                ordersAdapter = new OrdersAdapter(getContext(), orderModels);
                binding.recyclerOrders.setAdapter(ordersAdapter);
            }
        });

        return binding.getRoot();
    }

    private void loadViews() {
        binding.recyclerOrders.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, true);
        binding.recyclerOrders.setLayoutManager(layoutManager);
    }

    private void loadOrder() {
        List<OrderModel> orderModelList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference(Constants.ORDER_REF)
                .child(Constants.selectedOrder.getKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot itemSnapshot: snapshot.getChildren()){
                            OrderModel orderModel = itemSnapshot.getValue(OrderModel.class);
                            orderModel.setKey(itemSnapshot.getKey());
                            orderModelList.add(orderModel);
                        }
                        callbackListener.onOrderLoadSuccess(orderModelList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


    }


    @Override
    public void onOrderLoadSuccess(List<OrderModel> orderModelList) {

    }

    @Override
    public void onOrderLoadFailed(String message) {

    }
}