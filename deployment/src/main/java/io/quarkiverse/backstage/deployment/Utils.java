package io.quarkiverse.backstage.deployment;

import java.util.Optional;
import java.util.function.Function;
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

    /**
     * Resolves the name of the component a rest client depends on.
     * If {@code quarkus.rest-client.<client>.name} is set, its value is used. Otherwise, the client key is used.
     *
     * @param propertyName the rest client url property name
     * @param configLookup function that looks up the value of a config property
     * @return the dependency name if the property refers to a rest client url
     */
    public static Optional<String> getRestClientDependencyName(String propertyName,
            Function<String, Optional<String>> configLookup) {
        return getRestClientName(propertyName)
                .map(client -> configLookup.apply("quarkus.rest-client." + client + ".name")
                        .filter(name -> !name.isBlank())
                        .orElse(client));
    }
}
