package cs1302.api;

import com.google.gson.annotations.SerializedName;

/**
 * Represents the result from the movie info API. This is used by GSON to create
 * an object from the JSON response body.
 */
public class MovieResult {
    @SerializedName("Title") String title;
    @SerializedName("Released") String released;
    @SerializedName("Plot") String plot;
    @SerializedName("Actors") String actors;
    @SerializedName("Poster") String poster;
    String imdbRating;
}
