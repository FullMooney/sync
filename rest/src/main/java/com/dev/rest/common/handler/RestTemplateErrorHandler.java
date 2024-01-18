package com.dev.rest.common.handler;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.apache.commons.io.IOUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RestTemplateErrorHandler implements ResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
	return httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR
		|| httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR;
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {

	String errorStr = IOUtils.toString(httpResponse.getBody(), StandardCharsets.UTF_8);
	log.debug("handleError statusCode: {}", httpResponse.getStatusCode().value());
	log.debug("handleError errorStr: {}", errorStr);
	if (httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR
		|| httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR) {
	    throw new RuntimeException(errorStr);
	}
    }

}
