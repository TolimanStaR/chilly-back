package org.chilly.api_gateway.authority;

import org.springframework.http.HttpMethod;
import java.util.Set;

public record AllowedEndpoint(String pathPiece, Set<HttpMethod> methods) {

    AllowedEndpoint(String pathPiece, HttpMethod ...method) {
        this(pathPiece, Set.of(method));
    }
}
