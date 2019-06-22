package com.zendesk.codingchallenge.search.exception;

/**
 * Exception to throw when the search command fails.
 */
public class SearchCommandFailedException extends RuntimeException {

    public SearchCommandFailedException(String message) {
        super(message);
    }

    public SearchCommandFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
