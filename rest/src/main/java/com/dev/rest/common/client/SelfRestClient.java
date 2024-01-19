package com.dev.rest.common.client;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class SelfRestClient {

    private final RestTemplate selfRestTemplate;

    public SelfRestClient(@Qualifier("selfRestTemplate") RestTemplate selfRestTemplate) {
	this.selfRestTemplate = selfRestTemplate;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(String uri, Object obj, Class<?> clazz) {
	HttpEntity<?> httpEntity = makeHttpHeader(HttpMethod.GET, obj);
	return (T) selfRestTemplate.exchange(uri, HttpMethod.GET, httpEntity, clazz);
    }

    @SuppressWarnings("unchecked")
    public <T> T post(String uri, Object obj, Class<?> clazz) {
	HttpEntity<?> httpEntity = makeHttpHeader(HttpMethod.POST, obj);
	return (T) selfRestTemplate.exchange(uri, HttpMethod.POST, httpEntity, clazz);
    }

    @SuppressWarnings("deprecation")
    private HttpHeaders makeHeader() {
	HttpHeaders httpHeaders = new HttpHeaders();
	httpHeaders.setContentType(MediaType.APPLICATION_JSON_UTF8);
	httpHeaders.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
	return httpHeaders;
    }

    private HttpEntity<?> makeHttpHeader(HttpMethod httpMethod, Object param) {
	HttpHeaders httpHeaders = makeHeader();
	return httpMethod == HttpMethod.GET ? new HttpEntity<>(httpHeaders) : new HttpEntity<>(param, httpHeaders);
    }
}
