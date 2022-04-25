package com.example.onlineclothingstore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlineclothingstore.Model.OrderModel;
import com.example.onlineclothingstore.databinding.LayoutOrderDetailsItemBinding;

import java.text.SimpleDateFormat;
import java.util.List;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.ViewHolder> {

    private Context context;
    private List<OrderModel> orderModelList;
    private SimpleDateFormat simpleDateFormat;

    public OrdersAdapter(Context context, List<OrderModel> orderModelList) {
        this.context = context;
        this.orderModelList = orderModelList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutOrderDetailsItemBinding binding = LayoutOrderDetailsItemBinding.inflate(LayoutInflater.from(context), parent, false);
        return new ViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Glide.with(context).load(orderModelList.get(position).getCartModelList().get(0).getImage())
                .into(holder.binding.stockDetailsImg);

        holder.binding.stockDetailName.setText(new StringBuilder("").append(orderModelList.get(position).getUserName()));
        holder.binding.stockDetailPrice.setText(new StringBuilder("Price: ").append(orderModelList.get(position).getTotalPayment()));
        holder.binding.stockDetailQuantity.setText(new StringBuilder("Quantity: ").append(orderModelList.get(position).getOrderDate()));



    }

    @Override
    public int getItemCount() {
        return orderModelList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private LayoutOrderDetailsItemBinding binding;
        public ViewHolder(@NonNull LayoutOrderDetailsItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
