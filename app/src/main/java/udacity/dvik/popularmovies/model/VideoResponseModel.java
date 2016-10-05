package udacity.dvik.popularmovies.model;


import java.util.ArrayList;
import java.util.List;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Divya on 10/5/2016.
 */

public class VideoResponseModel {

    @SerializedName("id")
    @Expose
    private Integer id;
    @SerializedName("results")
    @Expose
    private List<VideoModel> results = new ArrayList<VideoModel>();

    /**
     * @return The id
     */
    public Integer getId() {
        return id;
    }

    /**
     * @param id The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * @return The results
     */
    public List<VideoModel> getVideoModels() {
        return results;
    }

    /**
     * @param results The results
     */
    public void setVideoModels(List<VideoModel> results) {
        this.results = results;
    }

}

