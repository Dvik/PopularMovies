package udacity.dvik.popularmovies;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
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

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import udacity.dvik.popularmovies.API.MovieAPI;
import udacity.dvik.popularmovies.data.FavoritesContract;
import udacity.dvik.popularmovies.model.MovieModel;
import udacity.dvik.popularmovies.model.MovieResponseModel;
import udacity.dvik.popularmovies.rest.MovieDataClient;

public class MovieListActivity extends AppCompatActivity {

    private boolean mTwoPane;
    GridLayoutManager gridLayoutManager;
    ProgressBar progressBar;
    TextView fail_tv;
    String value;
    RecyclerView recyclerView;

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

        recyclerView = (RecyclerView) findViewById(R.id.movie_list);
        assert recyclerView != null;

        if (findViewById(R.id.movie_detail_container) != null) {
            mTwoPane = true;

        }

        setupRecyclerView();


    }

    private void setupRecyclerView() {

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
            getDataFromServer(call);
        } else if (value.equals("1")) {
            call = apiService.getTopRatedMovies(Constants.API_KEY);
            getDataFromServer(call);
        }
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

    @Override
    protected void onResume() {
        super.onResume();
        if (value.equals("2")) {
            getDataFromProvider();
        }
    }

    public void insertBulkData(List<MovieModel> movies) {
        ContentValues[] favoritesValues = new ContentValues[movies.size()];

        for (int i = 0; i < movies.size(); i++) {
            favoritesValues[i] = new ContentValues();
            favoritesValues[i].put(FavoritesContract.FavoritesEntry.COLUMN_ID, String.valueOf(movies.get(i).getId()));
            favoritesValues[i].put(FavoritesContract.FavoritesEntry.COLUMN_POSTER_PATH, movies.get(i).getPosterPath());
            favoritesValues[i].put(FavoritesContract.FavoritesEntry.COLUMN_OVERVIEW,
                    movies.get(i).getOverview());
            favoritesValues[i].put(FavoritesContract.FavoritesEntry.COLUMN_RELEASE_DATE,
                    movies.get(i).getReleaseDate());
            favoritesValues[i].put(FavoritesContract.FavoritesEntry.COLUMN_ORIGINAL_TITLE,
                    movies.get(i).getOriginalTitle());
            favoritesValues[i].put(FavoritesContract.FavoritesEntry.COLUMN_ORIGINAL_LANG,
                    movies.get(i).getOriginalLanguage());
            favoritesValues[i].put(FavoritesContract.FavoritesEntry.COLUMN_TITLE,
                    movies.get(i).getTitle());
            favoritesValues[i].put(FavoritesContract.FavoritesEntry.COLUMN_BACKDROP_PATH,
                    movies.get(i).getBackdropPath());
            favoritesValues[i].put(FavoritesContract.FavoritesEntry.COLUMN_VOTE_AVERAGE,
                    String.valueOf(movies.get(i).getVoteAverage()));
            favoritesValues[i].put(FavoritesContract.FavoritesEntry.COLUMN_FAVORITED,
                    "0");
        }

        getContentResolver().bulkInsert(FavoritesContract.FavoritesEntry.CONTENT_URI,
                favoritesValues);
    }

    public void getDataFromServer(Call<MovieResponseModel> call) {
        call.enqueue(new Callback<MovieResponseModel>() {
            @Override
            public void onResponse(Call<MovieResponseModel> call, Response<MovieResponseModel> response) {
                progressBar.setVisibility(View.GONE);
                List<MovieModel> movies = response.body().getResults();
                insertBulkData(movies);
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

    public void getDataFromProvider() {
        String mSelectionClause = FavoritesContract.FavoritesEntry.COLUMN_FAVORITED + "= ?";
        String[] mSelectionArgs = {"1"};
        List<MovieModel> mArrayList = new ArrayList<MovieModel>();

        Cursor c = getContentResolver().query(FavoritesContract.FavoritesEntry.CONTENT_URI,
                FavoritesContract.FavoritesEntry.MOVIE_PROJECTION,
                mSelectionClause,
                mSelectionArgs,
                null);

        if (c != null) {
            if (c.getCount() > 0) {
                c.moveToFirst();
                for (c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
                    mArrayList.add(new MovieModel(c.getString(c.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_POSTER_PATH)),
                            c.getString(c.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_OVERVIEW)),
                            c.getString(c.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_RELEASE_DATE)),
                            Integer.parseInt(c.getString(c.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_ID))),
                            c.getString(c.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_ORIGINAL_TITLE)),
                            c.getString(c.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_ORIGINAL_LANG)),
                            c.getString(c.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_TITLE)),
                            c.getString(c.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_BACKDROP_PATH)),
                            Double.parseDouble(c.getString(c.getColumnIndex(FavoritesContract.FavoritesEntry.COLUMN_VOTE_AVERAGE)))));
                }
            }
        }

        c.close();

        progressBar.setVisibility(View.GONE);
        recyclerView.setAdapter(new MovieAdapter(mArrayList, MovieListActivity.this, mTwoPane));

    }
}
