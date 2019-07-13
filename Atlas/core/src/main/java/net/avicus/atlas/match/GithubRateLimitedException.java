package net.avicus.atlas.match;

public class GithubRateLimitedException extends RuntimeException {
    public GithubRateLimitedException(String message) {
        super(message);
    }

    public GithubRateLimitedException(String message, Throwable cause) {
        super(message, cause);
    }
}
