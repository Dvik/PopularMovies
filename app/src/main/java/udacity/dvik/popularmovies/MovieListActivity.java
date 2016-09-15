package udacity.dvik.popularmovies;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import udacity.dvik.popularmovies.API.MovieAPI;
import udacity.dvik.popularmovies.model.MovieModel;
import udacity.dvik.popularmovies.model.MovieResponseModel;
import udacity.dvik.popularmovies.rest.MovieDataClient;

public class MovieListActivity extends AppCompatActivity {

    private boolean mTwoPane;
    GridLayoutManager gridLayoutManager;
    ProgressBar progressBar;
    TextView fail_tv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        progressBar = (ProgressBar) findViewById(R.id.progress);
        fail_tv = (TextView) findViewById(R.id.fail_tv);
        fail_tv.setVisibility(View.GONE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        View recyclerView = findViewById(R.id.movie_list);
        assert recyclerView != null;
        setupRecyclerView((RecyclerView) recyclerView);

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;
        }
    }

    private void setupRecyclerView(@NonNull final RecyclerView recyclerView) {

        gridLayoutManager =
                new GridLayoutManager(this,
                        6,
                        LinearLayoutManager.VERTICAL,
                        false);

        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {

            @Override
            public int getSpanSize(int position) {
                if (mTwoPane)
                    return 2;
                else
                    return 3;
            }
        });
        recyclerView.setLayoutManager(gridLayoutManager);

        MovieAPI apiService =
                MovieDataClient.getClient().create(MovieAPI.class);

        Call<MovieResponseModel> call = apiService.getTopRatedMovies(getString(R.string.api_key));
        call.enqueue(new Callback<MovieResponseModel>() {
            @Override
            public void onResponse(Call<MovieResponseModel> call, Response<MovieResponseModel> response) {
                progressBar.setVisibility(View.GONE);
                int statusCode = response.code();
                List<MovieModel> movies = response.body().getResults();
                recyclerView.setAdapter(new MovieAdapter(movies, MovieListActivity.this));

            }

            @Override
            public void onFailure(Call<MovieResponseModel> call, Throwable t) {
                // Log error here since request failed
                progressBar.setVisibility(View.GONE);
                fail_tv.setVisibility(View.VISIBLE);
                Log.e(this.getClass().getSimpleName(), t.toString());
            }
        });
    }

}
