package udacity.dvik.popularmovies;

import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import udacity.dvik.popularmovies.model.MovieModel;

/**
 * Created by Divya on 9/15/2016.
 */


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<MovieModel> movies;
    private Context context;


    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView moviePoster;

        public MovieViewHolder(View v) {
            super(v);
            moviePoster = (ImageView) v.findViewById(R.id.poster);
        }
    }

    public MovieAdapter(List<MovieModel> movies, Context context) {
        this.movies = movies;
        this.context = context;
    }

    @Override
    public MovieAdapter.MovieViewHolder onCreateViewHolder(ViewGroup parent,
                                                            int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list_content, parent, false);
        return new MovieViewHolder(view);
    }


    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        Picasso.with(context)
                .load("http://image.tmdb.org/t/p/w185/"+movies.get(position).getPosterPath())
                .into(holder.moviePoster);

        holder.moviePoster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(context,MovieDetailActivity.class);
                i.putExtra(Constants.MOVIE_ITEM,(Parcelable)movies.get(position));

                context.startActivity(i);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }
}