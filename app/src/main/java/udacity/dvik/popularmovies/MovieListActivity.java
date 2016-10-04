package udacity.dvik.popularmovies;

import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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
    String value;

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

        value = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext())
                .getString("example_list", "0");

        View recyclerView = findViewById(R.id.movie_list);
        assert recyclerView != null;

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;

        }

        setupRecyclerView((RecyclerView) recyclerView);


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

        Call<MovieResponseModel> call;

        if (value.equals("0")) {
            call = apiService.getPopularMovies(Constants.API_KEY);
        } else {
            call = apiService.getTopRatedMovies(Constants.API_KEY);
        }

        call.enqueue(new Callback<MovieResponseModel>() {
            @Override
            public void onResponse(Call<MovieResponseModel> call, Response<MovieResponseModel> response) {
                progressBar.setVisibility(View.GONE);
                List<MovieModel> movies = response.body().getResults();
                recyclerView.setAdapter(new MovieAdapter(movies, MovieListActivity.this, mTwoPane));

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = new MenuInflater(this);
        menuInflater.inflate(R.menu.main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.settings) {
            startActivity(new Intent(this, SettingsActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }
}
