package com.example.onlineclothingstore.Adapter;

import android.content.Context;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.onlineclothingstore.Model.RatingModel;
import com.example.onlineclothingstore.databinding.StockReviewsItemLayoutBinding;

import java.util.List;


public class StockReviewsAdapter extends RecyclerView.Adapter<StockReviewsAdapter.ReviewsViewHolder> {
    private Context context;
    private List<RatingModel> ratingModelList;

    public StockReviewsAdapter(Context context, List<RatingModel> ratingModelList) {
        this.context = context;
        this.ratingModelList = ratingModelList;
    }

    @NonNull
    @Override
    public ReviewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        StockReviewsItemLayoutBinding binding = StockReviewsItemLayoutBinding.inflate(LayoutInflater.from(context),parent, false);
        return new ReviewsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsViewHolder holder, int position) {
        Long timeStamp = Long.valueOf(ratingModelList
                .get(position).getCommentTimeStamp()
                .get("timestamp").toString());

        holder.binding.commentDate.setText(DateUtils.getRelativeTimeSpanString(timeStamp));
        holder.binding.comments.setText(ratingModelList.get(position).getComment().trim());
        holder.binding.commentName.setText(ratingModelList.get(position).getName());
        holder.binding.commentRatingBar.setRating(ratingModelList.get(position).getRatingValue());
    }

    @Override
    public int getItemCount() {
        return ratingModelList.size();
    }

    public class ReviewsViewHolder extends RecyclerView.ViewHolder {
        private StockReviewsItemLayoutBinding binding;

        public ReviewsViewHolder(@NonNull StockReviewsItemLayoutBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }
}
