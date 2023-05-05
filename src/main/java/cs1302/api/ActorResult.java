package cs1302.api;

import com.google.gson.annotations.SerializedName;

/**
 * Represents the result from the actor info API. This is used by GSON to create
 * an object from the JSON response body.
 */
public class ActorResult {
    String name;
    @SerializedName("net_worth") String netWorth;
    String birthday;
    String age;
}
