package io.quarkiverse.backstage.client;

import java.util.concurrent.ExecutionException;

public class BackstageClientException extends RuntimeException {

    BackstageClientException(Throwable cause) {
        super(cause);
    }

    BackstageClientException(String message, Throwable cause) {
        super(message, cause);
    }

    public BackstageClientException(String message) {
        super(message);
    }

    public static BackstageClientException launderThrowable(String message, Throwable throwable) {
        if (throwable instanceof BackstageClientException) {
            return (BackstageClientException) throwable;
        } else if (throwable instanceof ExecutionException) {
            return launderThrowable(throwable.getCause());
        }
        return new BackstageClientException(message, throwable);
    }

    public static BackstageClientException launderThrowable(Throwable throwable) {
        if (throwable instanceof BackstageClientException) {
            return (BackstageClientException) throwable;
        } else if (throwable instanceof ExecutionException) {
            return launderThrowable(throwable.getCause());
        }
        return new BackstageClientException(throwable);
    }
}
