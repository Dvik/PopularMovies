package udacity.dvik.popularmovies.API;


import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import udacity.dvik.popularmovies.model.MovieResponseModel;

/**
 * Created by Divya on 9/15/2016.
 */

public interface MovieAPI {

    @GET("movie/popular")
    Call<MovieResponseModel> getTopRatedMovies(@Query("api_key") String apiKey);
}