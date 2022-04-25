package com.example.onlineclothingstore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlineclothingstore.Model.CartModel;
import com.example.onlineclothingstore.databinding.LayoutCartItemBinding;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<CartModel> cartModelList;

    public CartAdapter(Context context, List<CartModel> cartModelList) {
        this.context = context;
        this.cartModelList = cartModelList;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutCartItemBinding binding =LayoutCartItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Glide.with(context).load(cartModelList.get(position).getImage()).into(holder.binding.cartItemImage);

        holder.binding.cartStockPrice.setText(new StringBuilder("€").append(cartModelList.get(position).getPrice()));
        holder.binding.cartStockName.setText(new StringBuilder("").append(cartModelList.get(position).getName()));
        holder.binding.cartStockQuantity.setText(new StringBuilder("Quantity: ").append(cartModelList.get(position).getQuantity()));


    }

    @Override
    public int getItemCount() {
        return cartModelList.size();
    }

    public CartModel getItemPosition(int pos) {
        return cartModelList.get(pos);
    }

    public class CartViewHolder extends RecyclerView.ViewHolder{
        private LayoutCartItemBinding binding;
        public CartViewHolder(@NonNull LayoutCartItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
