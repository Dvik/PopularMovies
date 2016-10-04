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
    Call<MovieResponseModel> getPopularMovies(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MovieResponseModel> getTopRatedMovies(@Query("api_key") String apiKey);

    /*@GET("/movie/{id}/reviews")
    Call<List<Repo>> listRepos(@Path("user") String user);*/

}
