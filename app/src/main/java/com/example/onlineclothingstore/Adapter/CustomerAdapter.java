package com.example.onlineclothingstore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlineclothingstore.Callback.IRecyclerClickListener;
import com.example.onlineclothingstore.Model.CartModel;
import com.example.onlineclothingstore.Model.OrderModel;
import com.example.onlineclothingstore.Model.UserModel;
import com.example.onlineclothingstore.databinding.LayoutCustomerItemBinding;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class CustomerAdapter extends RecyclerView.Adapter<CustomerAdapter.CustomerViewHolder> {

    private Context context;
    private List<OrderModel> orderModelList;
    private List<UserModel> userModelList;
    private SimpleDateFormat simpleDateFormat;

    public CustomerAdapter(Context context, List<UserModel> userModelList) {
        this.context = context;
        this.userModelList = userModelList;
        simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy");
    }

    @NonNull
    @Override
    public CustomerViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutCustomerItemBinding binding = LayoutCustomerItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CustomerViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CustomerViewHolder holder, int position) {
//        Glide.with(context).load(orderModelList.get(position).getCartModelList().get(0).getImage())
//                .into(holder.binding.imgOrdersPlaced);

        holder.binding.orderNumber.setText(userModelList.get(position).getUid());
        holder.binding.orderCustomerName.setText(new StringBuilder("Name: ").append(userModelList.get(position).getName()));
        holder.binding.orderCustomerEmail.setText(new StringBuilder("Email: ").append(userModelList.get(position).getEmail()));
        holder.binding.custAddress.setText(new StringBuilder("Address: ").append(userModelList.get(position).getAddress()));

        holder.setListener(new IRecyclerClickListener() {
            @Override
            public void onItemClickListener(View view, int pos) {
                showOrderDetails(orderModelList.get(pos).getCartModelList());
            }
        });

    }

    private void showOrderDetails(List<CartModel> cartModelList) {
    }


    @Override
    public int getItemCount() {
        return userModelList.size();
    }

    public class CustomerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LayoutCustomerItemBinding binding;
        private IRecyclerClickListener listener;

        public void setListener(IRecyclerClickListener listener) {
            this.listener = listener;
        }

        public CustomerViewHolder(@NonNull LayoutCustomerItemBinding binding) {
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
