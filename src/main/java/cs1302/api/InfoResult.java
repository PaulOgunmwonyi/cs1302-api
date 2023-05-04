package cs1302.api;

/**
 * Represents the result from the character info API. This is used by GSON to create
 * an object from the JSON response body.
 */
public class InfoResult {
    String photo;
    String portrayedBy;
    String[] aliases;
    String born;
}
