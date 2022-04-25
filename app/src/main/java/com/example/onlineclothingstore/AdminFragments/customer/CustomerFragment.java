package com.example.onlineclothingstore.AdminFragments.customer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.example.onlineclothingstore.Adapter.CustomerAdapter;
import com.example.onlineclothingstore.Model.OrderModel;
import com.example.onlineclothingstore.Model.UserModel;
import com.example.onlineclothingstore.databinding.FragmentCustomerBinding;

import java.util.List;

public class CustomerFragment extends Fragment {

    private CustomerViewModel customerViewModel;
    private FragmentCustomerBinding binding;
    private CustomerAdapter customerAdapter;
    private List<UserModel> userModels;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        customerViewModel =
                new ViewModelProvider(this).get(CustomerViewModel.class);

        binding = FragmentCustomerBinding.inflate(inflater, container, false);


        initData();

       customerViewModel.getUserModelMutableList().observe(getViewLifecycleOwner(), new Observer<List<UserModel>>() {
           @Override
           public void onChanged(List<UserModel> userModelList) {
               userModels = userModelList;
               customerAdapter = new CustomerAdapter(getContext(), userModels);
               binding.customerRecycler.setAdapter(customerAdapter);

           }
       });
        return binding.getRoot();
    }

    private void initData() {
        binding.customerRecycler.setHasFixedSize(true);
        binding.customerRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}