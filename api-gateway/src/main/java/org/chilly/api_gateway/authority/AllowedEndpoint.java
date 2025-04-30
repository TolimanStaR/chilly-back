package org.chilly.api_gateway.authority;

import org.springframework.http.HttpMethod;
import java.util.Set;
import java.util.regex.Pattern;

public record AllowedEndpoint(Pattern pattern, Set<HttpMethod> methods) {

    AllowedEndpoint(String pathPattern, HttpMethod ...method) {
        this(Pattern.compile(pathPattern), Set.of(method));
    }
}
