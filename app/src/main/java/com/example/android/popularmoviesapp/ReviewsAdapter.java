package com.example.android.popularmoviesapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.popularmoviesapp.data.Review;


import java.util.List;

public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ReviewsMovieViewHolder>{

    public final static String LOG_TAG= ReviewsAdapter.class.getName().toString();
    private Context context;
    private List<Review> mMovieData;
    //private final ReviewsAdapterOnClickHandler mClickHandler;

    public ReviewsAdapter( List<Review> ReviewsFetchedData){
        mMovieData=ReviewsFetchedData;
        //mClickHandler=clickHandler;
    }


    public interface ReviewsAdapterOnClickHandler {
        void onClick(Review ReviewData);
    }

    @NonNull
    @Override
    public ReviewsMovieViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        context = viewGroup.getContext();
        int layoutIdForListItem = R.layout.review_movie_list_item;
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;

        View view = inflater.inflate(layoutIdForListItem, viewGroup, shouldAttachToParentImmediately);
        return new ReviewsMovieViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewsMovieViewHolder holder, int position) {

        String movieReview = mMovieData.get(position).getContent();
        String movieAuthor= mMovieData.get(position).getAuthor();

        holder.mReviewIcon.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_baseline_comment_24px));
        holder.mReviewContent.setText(movieReview);
        holder.mReviewAuthor.setText(movieAuthor);

    }

    @Override
    public int getItemCount() {
        if (null == mMovieData) return 0;
        return mMovieData.size();
    }

    public void setMovieData(List<Review> movieData) {
        mMovieData = movieData;
        notifyDataSetChanged();
    }

    public void restartData(){
        mMovieData.clear();
        notifyDataSetChanged();
    }


    public class ReviewsMovieViewHolder extends RecyclerView.ViewHolder{

        public final ImageView mReviewIcon;
        public final TextView mReviewAuthor;
        public final TextView mReviewContent;


        public ReviewsMovieViewHolder(View itemView) {
            super(itemView);

            mReviewIcon= (ImageView) itemView.findViewById(R.id.review_movie_icon);
            mReviewAuthor=(TextView)itemView.findViewById(R.id.review_movie_author);
            mReviewContent=(TextView)itemView.findViewById(R.id.review_movie_content);

        }

    }
}
