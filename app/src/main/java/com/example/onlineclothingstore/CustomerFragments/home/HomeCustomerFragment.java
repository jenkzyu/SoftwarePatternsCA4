package com.example.onlineclothingstore.CustomerFragments.home;

import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineclothingstore.Adapter.CategoryAdapter;
import com.example.onlineclothingstore.Constants.Constants;
import com.example.onlineclothingstore.Constants.SpacesItemDecorator;
import com.example.onlineclothingstore.Model.CategoryModel;
import com.example.onlineclothingstore.R;
import com.example.onlineclothingstore.databinding.HomeCustomerFragmentBinding;

import java.util.ArrayList;
import java.util.List;

import dmax.dialog.SpotsDialog;

public class HomeCustomerFragment extends Fragment {

    private HomeCustomerViewModel mViewModel;
    private HomeCustomerFragmentBinding binding;
    private AlertDialog alertDialog;
    private CategoryAdapter categoryAdapter;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mViewModel= new ViewModelProvider(this).get(HomeCustomerViewModel.class);

        binding = HomeCustomerFragmentBinding.inflate(inflater, container, false);

        categoryView();

        mViewModel.getMessageError().observe(getViewLifecycleOwner(), new Observer<String>() {
            @Override
            public void onChanged(String s) {
                Toast.makeText(getContext(), ""+s, Toast.LENGTH_SHORT).show();
            }
        });

        mViewModel.getCategoryListMutable().observe(getViewLifecycleOwner(), new Observer<List<CategoryModel>>() {
            @Override
            public void onChanged(List<CategoryModel> categoryModels) {
                alertDialog.dismiss();
                categoryAdapter = new CategoryAdapter(getContext(), categoryModels);
                binding.categoryRecycler.setAdapter(categoryAdapter);
            }
        });

        return binding.getRoot();

    }

    private void categoryView() {
        setHasOptionsMenu(true);
        alertDialog = new SpotsDialog.Builder().setContext(getContext()).setCancelable(false).build();
        alertDialog.show();

        GridLayoutManager layoutManager = new GridLayoutManager(getContext(), 2);
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        layoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (categoryAdapter != null) {
                    switch (categoryAdapter.getItemViewType(position)) {
                        case Constants.DEFAULT_COLUMN_COUNT:
                            return 1;
                        case Constants.FULL_WIDTH_COLUMN:
                            return 2;
                        default:
                            return -1;
                    }
                }
                return -1;
            }
        });

        binding.categoryRecycler.setLayoutManager(layoutManager);
        binding.categoryRecycler.addItemDecoration(new SpacesItemDecorator(8));
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        inflater.inflate(R.menu.search_menu, menu);

        MenuItem menuItem = menu.findItem(R.id.action_search);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

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
                mViewModel.loadCategories();
            }
        });
    }

    private void search(String query) {
        List<CategoryModel> categoryModelList = new ArrayList<>();
        for (int i = 0; i < categoryAdapter.getCategoryList().size(); i++) {
            CategoryModel categoryModel = categoryAdapter.getCategoryList().get(i);
            if (categoryModel.getName().toLowerCase().contains(query)) {
                categoryModelList.add(categoryModel);
            }
        }
        mViewModel.getCategoryListMutable().setValue(categoryModelList);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}