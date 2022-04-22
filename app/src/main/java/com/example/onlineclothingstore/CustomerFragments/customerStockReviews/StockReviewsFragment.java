package com.example.onlineclothingstore.CustomerFragments.customerStockReviews;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineclothingstore.Adapter.StockReviewsAdapter;
import com.example.onlineclothingstore.Callback.IReviewsCallbackListener;
import com.example.onlineclothingstore.Constants.Constants;
import com.example.onlineclothingstore.Model.RatingModel;
import com.example.onlineclothingstore.databinding.StockReviewsFragmentBinding;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class StockReviewsFragment extends BottomSheetDialogFragment implements IReviewsCallbackListener {

    private StockReviewsViewModel reviewsViewModel;
    private StockReviewsFragmentBinding binding;
    private IReviewsCallbackListener callbackListener;
    private static StockReviewsFragment instance;

    public StockReviewsFragment() {
        callbackListener = this;
    }

    /**
     * SINGLETON Pattern
     **/
    private static StockReviewsFragment getInstance() {
        if (instance == null) {
            instance = new StockReviewsFragment();
        }
        return instance;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        binding = StockReviewsFragmentBinding.inflate(inflater, container, false);
        reviewsViewModel = new ViewModelProvider(this).get(StockReviewsViewModel.class);
        //init views
        reviewView();
        //load reviews from firebase
        loadReviews();

        reviewsViewModel.getRatingModelMutableLiveData().observe(getViewLifecycleOwner(), new Observer<List<RatingModel>>() {
            @Override
            public void onChanged(List<RatingModel> ratingModelList) {
                StockReviewsAdapter adapter = new StockReviewsAdapter(getContext(), ratingModelList);
                binding.recyclerReviews.setAdapter(adapter);
            }
        });

        return binding.getRoot();
    }

    private void loadReviews() {
        List<RatingModel> ratingList = new ArrayList<>();
        FirebaseDatabase.getInstance().getReference(Constants.RATING_REF)
                .child(Constants.selectedStock.getStock_id())
                .orderByChild("timestamp")
                .limitToLast(100)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot ratings : snapshot.getChildren()) {
                            RatingModel ratingsModel = ratings.getValue(RatingModel.class);
                            ratingList.add(ratingsModel);
                        }
                        callbackListener.onReviewLoadSuccess(ratingList);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        callbackListener.onReviewLoadFailed(error.getMessage());
                    }
                });
    }

    private void reviewView() {
        binding.recyclerReviews.setHasFixedSize(true);
        LinearLayoutManager layout = new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, true);
        binding.recyclerReviews.setLayoutManager(layout);
    }


    @Override
    public void onReviewLoadSuccess(List<RatingModel> ratingModelList) {
        reviewsViewModel.setCommentList(ratingModelList);
    }

    @Override
    public void onReviewLoadFailed(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}