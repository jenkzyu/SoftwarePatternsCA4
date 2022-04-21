package com.example.onlineclothingstore.CustomerFragment.home.customerStockList;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.onlineclothingstore.R;

public class CustomerStockList extends Fragment {

    private CustomerStockListViewModel mViewModel;

    public static CustomerStockList newInstance() {
        return new CustomerStockList();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.customer_stock_list_fragment, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = new ViewModelProvider(this).get(CustomerStockListViewModel.class);
        // TODO: Use the ViewModel
    }

}