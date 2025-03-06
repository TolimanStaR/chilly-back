package org.chilly.api_gateway.authority;

import org.springframework.http.HttpMethod;

public record AllowedEndpoint(String path, HttpMethod method) {

}
