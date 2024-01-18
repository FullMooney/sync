package com.dev.rest.common.interceptor;

import java.io.IOException;
import java.net.URI;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.ClientHttpRequestExecution;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpClientRequestInterceptor implements ClientHttpRequestInterceptor {

    @Override
    public ClientHttpResponse intercept(HttpRequest request, byte[] body, ClientHttpRequestExecution execution)
	    throws IOException {
	logReq(request.getURI(), body);
	ClientHttpResponse response = execution.execute(request, body);
	logReq(request.getURI(), response);
	return response;
    }

    private void logReq(URI requestUri, byte[] body) throws IOException {
	if (log.isInfoEnabled()) {
	    log.info("- URI : {}", requestUri);
	    log.info("- request body : {}", new String(body, StandardCharsets.UTF_8));
	}
    }

    private void logReq(URI requestUri, ClientHttpResponse response) throws IOException {
	if (log.isInfoEnabled()) {
	    log.info("- URI : {}", requestUri);
	    log.info("- Response statuscode : {} ", response.getStatusCode());
	    log.info("- Response body : {} ", IOUtils.toString(response.getBody(), StandardCharsets.UTF_8));
	}
    }

}
