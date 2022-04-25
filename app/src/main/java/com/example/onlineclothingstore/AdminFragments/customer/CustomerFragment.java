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

import com.example.onlineclothingstore.databinding.FragmentCustomerBinding;

public class CustomerFragment extends Fragment {

    private CustomerViewModel customerViewModel;
    private FragmentCustomerBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        customerViewModel =
                new ViewModelProvider(this).get(CustomerViewModel.class);

        binding = FragmentCustomerBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}