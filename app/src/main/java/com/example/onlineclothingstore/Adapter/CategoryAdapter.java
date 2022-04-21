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
import com.example.onlineclothingstore.EventBus.CategoryClick;
import com.example.onlineclothingstore.Model.CategoryModel;
import com.example.onlineclothingstore.databinding.LayoutCategoryItemBinding;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.CatViewHolder> {
    private Context context;
    private List<CategoryModel> categoryModelList;
    private int DEFAULT_COLUMN_COUNT = 0;
    private int FULL_WIDTH_COLUMN = 1;

    public CategoryAdapter(Context context, List<CategoryModel> categoryModelList) {
        this.context = context;
        this.categoryModelList = categoryModelList;
    }

    @NonNull
    @Override
    public CatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutCategoryItemBinding binding = LayoutCategoryItemBinding.inflate(LayoutInflater.from(context),parent, false);
        return new CatViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CatViewHolder holder, int position) {
        Glide.with(context).load(categoryModelList.get(position).getImage()).into(holder.binding.imgCategory);
        holder.binding.txtCName.setText(new StringBuilder(categoryModelList.get(position).getName()));

        //EventBus
        holder.setListener(new IRecyclerClickListener() {
            @Override
            public void onItemClickListener(View view, int pos) {
                Constants.categorySelected = categoryModelList.get(pos);
                EventBus.getDefault().postSticky(new CategoryClick(true, categoryModelList.get(pos)));
            }
        });

    }

    @Override
    public int getItemCount() {
        return categoryModelList.size();
    }

    public class CatViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private LayoutCategoryItemBinding binding;
        private IRecyclerClickListener listener;

        public void setListener(IRecyclerClickListener listener) {
            this.listener = listener;
        }

        public CatViewHolder(@NonNull LayoutCategoryItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.getRoot().setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            listener.onItemClickListener(v, getAdapterPosition());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (categoryModelList.size() == 1){
            return DEFAULT_COLUMN_COUNT;
        }else{
            if (categoryModelList.size() % 2 == 0){
                return DEFAULT_COLUMN_COUNT;
            }else{
                return (position > 1 && position == categoryModelList.size() -1) ? FULL_WIDTH_COLUMN: DEFAULT_COLUMN_COUNT;
            }
        }
    }
}
