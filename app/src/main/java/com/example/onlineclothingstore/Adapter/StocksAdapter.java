package com.example.onlineclothingstore.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.onlineclothingstore.Model.StockModel;
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
        LayoutStockItemBinding binding = LayoutStockItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new StocksViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull StocksViewHolder holder, int position) {
        //populate Image
        Glide.with(context).load(stockModelList.get(position).getImage()).into(holder.binding.foodDetailsImg);
        holder.binding.itemDetailName.setText(stockModelList.get(position).getName());
        holder.binding.itemDetailStock.setText(new StringBuilder("Stock: ").append(stockModelList.get(position).getStockCount()));
        holder.binding.itemDetailMake.setText(new StringBuilder("Manufacturer: ").append(stockModelList.get(position).getManufacturer()));
        holder.binding.itemDetailCategory.setText(new StringBuilder("Category: ").append(stockModelList.get(position).getCategory()));
        holder.binding.itemDetailPrice.setText(new StringBuilder("Price: ").append("€").append(stockModelList.get(position).getPrice()));

    }

    @Override
    public int getItemCount() {
        return stockModelList.size();
    }

    public class StocksViewHolder extends RecyclerView.ViewHolder {
        private LayoutStockItemBinding binding;

        public StocksViewHolder(@NonNull LayoutStockItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (stockModelList.size() == 1) {
            return DEFAULT_COLUMN_COUNT;
        } else {
            if (stockModelList.size() % 2 == 0) {
                return DEFAULT_COLUMN_COUNT;
            } else
                return (position > 1 && position == stockModelList.size() - 1) ? FULL_WIDTH_COLUMN : DEFAULT_COLUMN_COUNT;
        }
    }
}
