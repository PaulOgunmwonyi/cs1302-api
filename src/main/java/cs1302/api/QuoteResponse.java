package cs1302.api;

/**
 * Represents a response from the Quote Generator API. This is used by GSON to create an object
 * from the JSON response body.
 */
public class QuoteResponse {
    int totalQuotes;
    QuoteResult[] data;
}
