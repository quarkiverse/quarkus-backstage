package io.quarkiverse.backstage.deployment;

import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Utils {

    private static final Pattern REST_CLIENT_URL_PATTERN = Pattern.compile("quarkus\\.rest-client\\.(?<name>[^.]+)\\.url");

    public static Optional<String> getRestClientName(String propertyName) {
        Matcher matcher = REST_CLIENT_URL_PATTERN.matcher(propertyName);
        if (!matcher.matches()) {
            return Optional.empty();
        }
        return Optional.of(matcher.group("name"));
    }
}
