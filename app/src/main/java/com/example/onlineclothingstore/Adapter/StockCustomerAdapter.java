package com.example.onlineclothingstore.Adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlineclothingstore.Callback.IRecyclerClickListener;
import com.example.onlineclothingstore.Constants.Constants;
import com.example.onlineclothingstore.CustomerFragment.home.StockReviews.StockReviewsFragment;
import com.example.onlineclothingstore.CustomerFragment.home.customerStockList.CustomerStockListViewModel;
import com.example.onlineclothingstore.Model.RatingModel;
import com.example.onlineclothingstore.Model.StockModel;
import com.example.onlineclothingstore.R;
import com.example.onlineclothingstore.databinding.LayoutStockItemBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import net.cachapa.expandablelayout.ExpandableLayout;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StockCustomerAdapter extends RecyclerView.Adapter<StockCustomerAdapter.StocksViewHolder> {
    private Context context;
    private List<StockModel> stockModelList;
    private ExpandableLayout expandableLayout;

    public StockCustomerAdapter(Context context, List<StockModel> stockModelList) {
        this.context = context;
        this.stockModelList = stockModelList;
    }

    @NonNull
    @Override
    public StocksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutStockItemBinding binding = LayoutStockItemBinding.inflate(LayoutInflater.from(parent.getContext()),parent,false);
        return new StocksViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StocksViewHolder holder, int position) {
        //populate Image
        Glide.with(context).load(stockModelList.get(position).getImage()).into(holder.binding.foodDetailsImg);
        holder.binding.itemDetailPrice.setText(new StringBuilder("€").append(stockModelList.get(position).getPrice()));
        holder.binding.itemDetailName.setText(new StringBuilder("").append(stockModelList.get(position).getName()));
        holder.binding.itemDetailStock.setText(new StringBuilder("Stock Level: ").append(stockModelList.get(position).getStockCount()));
        holder.binding.itemDetailMake.setText(new StringBuilder("Manufacturer: ").append(stockModelList.get(position).getManufacturer()));
        holder.binding.itemDetailCategory.setText(new StringBuilder("Category: ").append(stockModelList.get(position).getCategory()));

        if (stockModelList.get(position).getRatingValue() !=null){
            holder.binding.ratingBar.setRating(stockModelList.get(position).getRatingValue().floatValue());
        }

        //setListener
        holder.setListener(new IRecyclerClickListener() {
            @Override
            public void onItemClickListener(View view, int pos) {
                Constants.selectedStock = stockModelList.get(position);
                Constants.selectedStock.setKey(String.valueOf(position));
                if (expandableLayout != null && expandableLayout.isExpanded())
                    //holder.expandable_layout.setSelected(true);
                    expandableLayout.collapse();

                if (!holder.binding.expandableLayout.isExpanded()) {
                    holder.binding.expandableLayout.setSelected(true);
                    holder.binding.expandableLayout.expand();

                } else {
                    //holder.binding.expandableLayout.setSelected(false);
                    holder.binding.expandableLayout.collapse();
                }

                expandableLayout = holder.binding.expandableLayout;
            }
        });

        //check all reviews and ratings for stock item
        holder.binding.btnReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StockReviewsFragment fragment = new StockReviewsFragment();
                fragment.show(((FragmentActivity)context).getSupportFragmentManager(),fragment.getTag());
            }
        });

        holder.binding.imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Clicked!", Toast.LENGTH_SHORT).show();
            }
        });

        holder.binding.imgRate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.selectedStock = stockModelList.get(position);
                Constants.selectedStock.setKey(String.valueOf(position));
                showDialogRating();
            }
        });

    }
    // Rating Sequence Methods
    private void showDialogRating() {
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(context);
        builder.setTitle("Rate Food");
        builder.setMessage("Please fill information");

        View itemView = LayoutInflater.from(context).inflate(R.layout.layout_rating, null);

        RatingBar ratingBar = itemView.findViewById(R.id.rating_bar2);

        TextInputEditText edtComment = itemView.findViewById(R.id.edtComment);

        builder.setView(itemView);
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                RatingModel ratingModel = new RatingModel();
                ratingModel.setName(Constants.currentUser.getName());
                ratingModel.setUid(Constants.currentUser.getUid());
                ratingModel.setComment(edtComment.getText().toString());
                ratingModel.setRatingValue(ratingBar.getRating());

                Map<String, Object> serverTime = new HashMap<>();
                serverTime.put("timestamp", ServerValue.TIMESTAMP);
                ratingModel.setCommentTimeStamp(serverTime);

                submitRatingToFirebase(ratingModel);

            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void submitRatingToFirebase(RatingModel ratingModel) {
        FirebaseDatabase.getInstance()
                .getReference(Constants.RATING_REF)
                .child(Constants.selectedStock.getStock_id())
                .push()
                .setValue(ratingModel)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(context, "HEllothere", Toast.LENGTH_SHORT).show();
                            addRatingToStock(ratingModel.getRatingValue());
                        }
                    }
                });


    }

    private void addRatingToStock(float ratingValue) {
        FirebaseDatabase.getInstance().getReference(Constants.CATEGORY_REFERENCE)
                .child(Constants.categorySelected.getCat_id())
                .child("stocks")
                .child(Constants.selectedStock.getKey())
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            StockModel stockModel = snapshot.getValue(StockModel.class);
                            stockModel.setKey(Constants.selectedStock.getKey());

                            // applying rating
                            if (stockModel.getRatingValue() == null)
                                stockModel.setRatingValue(0d); //d = D
                            if (stockModel.getRatingCount() == null)
                                stockModel.setRatingCount(0l); //l = L signify (number_01)
                            // rating formula
                            double sumRating = stockModel.getRatingValue() * stockModel.getRatingCount() + ratingValue;
                            long ratingCount = stockModel.getRatingCount() + 1;
                            double result = sumRating / ratingCount;

                            Map<String, Object> updateData = new HashMap<>();
                            updateData.put("ratingValue", result);
                            updateData.put("ratingCount", ratingCount);

                            snapshot.getRef().updateChildren(updateData)
                                    .addOnCompleteListener(task -> {
                                        if (task.isSuccessful()){
                                            Toast.makeText(context, "Thank you for submitting!", Toast.LENGTH_SHORT).show();
                                            Constants.selectedStock = stockModel;
                                        }
                                    });

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        Toast.makeText(context, error.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    @Override
    public int getItemCount() {
        return stockModelList.size();
    }

    public class StocksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LayoutStockItemBinding binding;
        private IRecyclerClickListener listener;

        public void setListener(IRecyclerClickListener listener) {
            this.listener = listener;
        }

        public StocksViewHolder(@NonNull LayoutStockItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClickListener(v, getAdapterPosition());
        }
    }
}
