package io.quarkiverse.backstage.client;

public class BackstageConflictException extends BackstageClientException {

    public BackstageConflictException(Throwable cause) {
        super(cause);
    }

    public BackstageConflictException(String message) {
        super(message);
    }

    public BackstageConflictException(String message, Throwable cause) {
        super(message, cause);
    }
}
