package udacity.dvik.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import udacity.dvik.popularmovies.model.MovieModel;
import udacity.dvik.popularmovies.model.ReviewModel;

/**
 * Created by Divya on 10/5/2016.
 */

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewViewHolder> {

    private List<ReviewModel> reviews;

    public static class ReviewViewHolder extends RecyclerView.ViewHolder {
        TextView author,content;
        public ReviewViewHolder(View v) {
            super(v);
            author = (TextView) v.findViewById(R.id.author_tv);
            content = (TextView) v.findViewById(R.id.review_tv);
        }
    }

    public ReviewAdapter(List<ReviewModel> reviews) {
        this.reviews = reviews;
    }

    @Override
    public ReviewAdapter.ReviewViewHolder onCreateViewHolder(ViewGroup parent,
                                                           int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review, parent, false);
        return new ReviewAdapter.ReviewViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final ReviewAdapter.ReviewViewHolder holder, int position) {
        holder.author.setText(reviews.get(position).getAuthor());
        holder.content.setText(reviews.get(position).getContent());
    }

    @Override
    public int getItemCount() {
        return reviews.size();
    }
}