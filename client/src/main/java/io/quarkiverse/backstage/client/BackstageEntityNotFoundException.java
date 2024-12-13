package io.quarkiverse.backstage.client;

public class BackstageEntityNotFoundException extends BackstageClientException {

    public BackstageEntityNotFoundException(Throwable cause) {
        super(cause);
    }

    public BackstageEntityNotFoundException(String message) {
        super(message);
    }

    public BackstageEntityNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
