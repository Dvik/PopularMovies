package udacity.dvik.popularmovies;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import udacity.dvik.popularmovies.model.MovieModel;


/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment {

    private MovieModel movie;

    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(Constants.MOVIE_ITEM)) {

            movie = getArguments().getParcelable(Constants.MOVIE_ITEM);

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null && movie!=null) {
                ImageView backDropImage = (ImageView) appBarLayout.findViewById(R.id.image);
                Picasso.with(activity)
                        .load("http://image.tmdb.org/t/p/w342/"+movie.getBackdropPath())
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
            Picasso.with(getActivity())
                    .load("http://image.tmdb.org/t/p/w342/"+movie.getPosterPath())
                    .into((ImageView)rootView.findViewById(R.id.thumb_image_detail));
            ((TextView) rootView.findViewById(R.id.movie_detail)).setText(movie.getOverview());
            ((TextView) rootView.findViewById(R.id.rating)).setText(String.format("%s%s", getString(R.string.rating), String.valueOf(movie.getVoteAverage())));
            ((TextView) rootView.findViewById(R.id.release_date)).setText(String.format("%s%s", getString(R.string.release), movie.getReleaseDate()));
        }

        return rootView;
    }
}
