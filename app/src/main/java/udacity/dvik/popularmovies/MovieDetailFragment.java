package udacity.dvik.popularmovies;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import udacity.dvik.popularmovies.API.MovieAPI;
import udacity.dvik.popularmovies.data.FavoritesContract;
import udacity.dvik.popularmovies.model.MovieModel;
import udacity.dvik.popularmovies.model.MovieResponseModel;
import udacity.dvik.popularmovies.model.VideoModel;
import udacity.dvik.popularmovies.model.VideoResponseModel;
import udacity.dvik.popularmovies.rest.MovieDataClient;


/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment {

    private MovieModel movie;
    private Button favButton;
    private Button viewTrailer;

    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(Constants.MOVIE_ITEM)) {

            movie = getArguments().getParcelable(Constants.MOVIE_ITEM);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null && movie != null) {
                ImageView backDropImage = (ImageView) appBarLayout.findViewById(R.id.image);
                Picasso.with(activity)
                        .load("http://image.tmdb.org/t/p/w342/" + movie.getBackdropPath())
                        .into(backDropImage);
                appBarLayout.setTitle(movie.getOriginalTitle());

            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);

        if (movie != null) {
            int favOrNot = 0;
            Picasso.with(getActivity())
                    .load("http://image.tmdb.org/t/p/w342/" + movie.getPosterPath())
                    .into((ImageView) rootView.findViewById(R.id.thumb_image_detail));
            ((TextView) rootView.findViewById(R.id.movie_detail)).setText(movie.getOverview());
            ((TextView) rootView.findViewById(R.id.rating)).setText(String.valueOf(movie.getVoteAverage()));
            ((TextView) rootView.findViewById(R.id.release_date)).setText(movie.getReleaseDate());
            favButton = ((Button) rootView.findViewById(R.id.fav_btn));
            viewTrailer = (Button) rootView.findViewById(R.id.view_trailer);

            String mSelectionClauseQuery = FavoritesContract.FavoritesEntry.COLUMN_ID + "= ?";
            String[] mSelectionArgsQuery = {String.valueOf(movie.getId())};

            Cursor c =
                    getActivity().getContentResolver().query(FavoritesContract.FavoritesEntry.CONTENT_URI,
                            new String[]{FavoritesContract.FavoritesEntry.COLUMN_FAVORITED},
                            mSelectionClauseQuery,
                            mSelectionArgsQuery,
                            null);

            if(c!=null) {
                if (c.getCount() > 0) {
                    c.moveToFirst();
                    favOrNot = Integer.parseInt(c.getString(0));
                }
            }

            c.close();

            if (favOrNot == 1) {
                favButton.setText(R.string.removeFav);
            } else {
                favButton.setText(R.string.setFav);
            }


            favButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (favButton.getText().toString().equalsIgnoreCase(getString(R.string.setFav))) {
                        favButton.setText(R.string.removeFav);
                        Log.d("sdsadsa",String.valueOf(updateFav(1, movie.getId())));
                    } else {
                        favButton.setText(R.string.setFav);
                        updateFav(0, movie.getId());
                    }

                }
            });

            final MovieAPI apiService =
                    MovieDataClient.getClient().create(MovieAPI.class);

            viewTrailer.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Call<VideoResponseModel> call = apiService.getAllVideos(String.valueOf(movie.getId()),Constants.API_KEY);
                    call.enqueue(new Callback<VideoResponseModel>() {
                        @Override
                        public void onResponse(Call<VideoResponseModel> call, Response<VideoResponseModel> response) {
                            List<VideoModel> movies = response.body().getVideoModels();
                            int found = 0;
                            for(VideoModel vm: movies)
                            {
                                if(vm.getType().equals("Trailer") && vm.getSite().equals("YouTube"))
                                {
                                    found = 1;
                                    startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+vm.getKey())));
                                    break;
                                }
                            }
                            if(found == 0 && movies.get(0).getSite().equals("YouTube"))
                            {
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v="+movies.get(0).getKey())));
                            }
                        }

                        @Override
                        public void onFailure(Call<VideoResponseModel> call, Throwable t) {
                            Toast.makeText(getActivity(),"Not able to play trailer now",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            });


        }

        return rootView;
    }

    public int updateFav(int fav, int id) {
        ContentValues favValue = new ContentValues();
        favValue.put(FavoritesContract.FavoritesEntry.COLUMN_FAVORITED,
                String.valueOf(fav));
        String mSelectionClause = FavoritesContract.FavoritesEntry.COLUMN_ID + "= ?";
        String[] mSelectionArgs = {String.valueOf(id)};
        return getActivity().getContentResolver().update(FavoritesContract.FavoritesEntry.CONTENT_URI,
                favValue,
                mSelectionClause,
                mSelectionArgs);
    }


}
