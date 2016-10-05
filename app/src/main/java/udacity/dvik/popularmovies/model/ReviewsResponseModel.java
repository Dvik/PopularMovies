package udacity.dvik.popularmovies.model;

import java.util.ArrayList;
import java.util.List;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
/**
 * Created by Divya on 9/30/2016.
 */

public class ReviewsResponseModel {@SerializedName("id")
@Expose
private Integer id;
    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("results")
    @Expose
    private List<ReviewModel> results = new ArrayList<ReviewModel>();
    @SerializedName("total_pages")
    @Expose
    private Integer totalPages;
    @SerializedName("total_results")
    @Expose
    private Integer totalReviewModels;

    /**
     *
     * @return
     * The id
     */
    public Integer getId() {
        return id;
    }

    /**
     *
     * @param id
     * The id
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     *
     * @return
     * The page
     */
    public Integer getPage() {
        return page;
    }

    /**
     *
     * @param page
     * The page
     */
    public void setPage(Integer page) {
        this.page = page;
    }

    /**
     *
     * @return
     * The results
     */
    public List<ReviewModel> getReviewModels() {
        return results;
    }

    /**
     *
     * @param results
     * The results
     */
    public void setReviewModels(List<ReviewModel> results) {
        this.results = results;
    }

    /**
     *
     * @return
     * The totalPages
     */
    public Integer getTotalPages() {
        return totalPages;
    }

    /**
     *
     * @param totalPages
     * The total_pages
     */
    public void setTotalPages(Integer totalPages) {
        this.totalPages = totalPages;
    }

    /**
     *
     * @return
     * The totalReviewModels
     */
    public Integer getTotalReviewModels() {
        return totalReviewModels;
    }

    /**
     *
     * @param totalReviewModels
     * The total_results
     */
    public void setTotalReviewModels(Integer totalReviewModels) {
        this.totalReviewModels = totalReviewModels;
    }

}