package com.example.onlineclothingstore.CustomerFragment.home.customerStockList;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.onlineclothingstore.Adapter.StockCustomerAdapter;
import com.example.onlineclothingstore.Constants.Constants;
import com.example.onlineclothingstore.Model.StockModel;
import com.example.onlineclothingstore.R;
import com.example.onlineclothingstore.databinding.CustomerStockListFragmentBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class CustomerStockList extends Fragment {

    private CustomerStockListViewModel stockListViewModel;
    private CustomerStockListFragmentBinding binding;
    private StockCustomerAdapter stockCustomerAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        stockListViewModel = new ViewModelProvider(this).get(CustomerStockListViewModel.class);
        binding = CustomerStockListFragmentBinding.inflate(inflater, container, false);

        //init stockview
        stockView();
        stockListViewModel.getStockMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<StockModel>>() {
            @Override
            public void onChanged(List<StockModel> stockModelList) {
                if (stockModelList != null) {
                    stockCustomerAdapter = new StockCustomerAdapter(getContext(), stockModelList);
                    binding.stockListRecycler.setAdapter(stockCustomerAdapter);
                }
            }
        });
        return binding.getRoot();
    }

    private void stockView() {
        setHasOptionsMenu(true);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(Constants.categorySelected.getName());
        binding.stockListRecycler.setHasFixedSize(true);
        binding.stockListRecycler.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    //Search


    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

        //Event
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        //Clear text for clear button
        ImageView clearButton = searchView.findViewById(R.id.search_close_btn);
        clearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditText editText = searchView.findViewById(R.id.search_src_text);
                editText.setText("");
                //clear query
                searchView.setQuery("", false);
                // collapse action view
                searchView.onActionViewCollapsed();
                //collapse search widget
                menuItem.collapseActionView();
                //Restore to original view
                stockListViewModel.getStockMutableLiveData();
            }
        });
    }

    private void search(String query) {
        List<StockModel> searchResults = new ArrayList<>();
        for (int i = 0; i< Constants.categorySelected.getStocks().size(); i++){
            StockModel stockModel = Constants.categorySelected.getStocks().get(i);
            if (stockModel.getName().toLowerCase().contains(query.toLowerCase()) || stockModel.getManufacturer().toLowerCase().contains(query.toLowerCase())){
                stockModel.setPosInList(i); // save index
                searchResults.add(stockModel);
            }
        }
        //set search results
        stockListViewModel.getStockMutableLiveData().setValue(searchResults);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }


}