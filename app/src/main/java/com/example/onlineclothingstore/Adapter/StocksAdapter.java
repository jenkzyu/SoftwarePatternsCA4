package com.example.onlineclothingstore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlineclothingstore.Callback.IRecyclerClickListener;
import com.example.onlineclothingstore.Constants.Constants;
import com.example.onlineclothingstore.Model.StockModel;
import com.example.onlineclothingstore.databinding.LayoutStockBinding;
import com.example.onlineclothingstore.databinding.LayoutStockItemBinding;

import java.util.List;

public class StocksAdapter extends RecyclerView.Adapter<StocksAdapter.StocksViewHolder> {
    private Context context;
    private List<StockModel> stockModelList;
    private int DEFAULT_COLUMN_COUNT = 0;
    private int FULL_WIDTH_COLUMN = 1;

    public StocksAdapter(Context context, List<StockModel> stockModelList) {
        this.context = context;
        this.stockModelList = stockModelList;
    }

    @NonNull
    @Override
    public StocksViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutStockBinding binding = LayoutStockBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new StocksViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StocksViewHolder holder, int position) {
        //populate Image
        Glide.with(context).load(stockModelList.get(position).getImage()).into(holder.binding.imgStock);
        holder.binding.txtStockPrice.setText(new StringBuilder("€").append(stockModelList.get(position).getPrice()));
        holder.binding.txtStockName.setText(new StringBuilder("").append(stockModelList.get(position).getName()));
        holder.binding.txtStockLevel.setText(new StringBuilder("Stock Level: ").append(stockModelList.get(position).getStockCount()));
        holder.binding.txtStockManufacturer.setText(new StringBuilder("Manufacturer: ").append(stockModelList.get(position).getManufacturer()));

        holder.setListener(new IRecyclerClickListener() {
            @Override
            public void onItemClickListener(View view, int pos) {
                Constants.selectedStock = stockModelList.get(pos);
                Constants.selectedStock.setKey(String.valueOf(pos));
            }
        });

    }

    @Override
    public int getItemCount() {
        return stockModelList.size();
    }

    public StockModel getItemPosition(int pos){
        return stockModelList.get(pos);
    }

    public class StocksViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LayoutStockBinding binding;
        private IRecyclerClickListener listener;

        public void setListener(IRecyclerClickListener listener) {
            this.listener = listener;
        }

        public StocksViewHolder(@NonNull LayoutStockBinding binding) {
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
