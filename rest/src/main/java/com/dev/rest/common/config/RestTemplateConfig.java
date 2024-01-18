package com.dev.rest.common.config;

import java.nio.charset.StandardCharsets;
import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.SSLContext;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.ssl.SSLContextBuilder;
import org.apache.http.ssl.TrustStrategy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.BufferingClientHttpRequestFactory;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import com.dev.rest.common.handler.RestTemplateErrorHandler;
import com.dev.rest.common.interceptor.HttpClientRequestInterceptor;

@Configuration
public class RestTemplateConfig {

    @Value("${target.self:http://localhost:8080}")
    private String targetSelf;

    @Bean
    HttpClient httpClient() throws KeyStoreException, NoSuchAlgorithmException, KeyManagementException {

	TrustStrategy acceptingTrustStrategy = (X509Certificate[] chain, String authType) -> true;

	SSLContext sslContext = new SSLContextBuilder().loadTrustMaterial(null, acceptingTrustStrategy).build();

	SSLConnectionSocketFactory csf = new SSLConnectionSocketFactory(sslContext);

	return HttpClientBuilder.create().setMaxConnTotal(200).setMaxConnPerRoute(100) // IP:포트별 최대 커넥션 수
		.setConnectionTimeToLive(30, TimeUnit.SECONDS) // keep-alive
		.setSSLSocketFactory(csf).build();
    }

    @Bean
    HttpComponentsClientHttpRequestFactory factory(HttpClient httpClient) {
	HttpComponentsClientHttpRequestFactory httpComponentsClientHttpRequestFactory = new HttpComponentsClientHttpRequestFactory(
		httpClient);
	httpComponentsClientHttpRequestFactory
		.setConnectionRequestTimeout(3); // 3s
	httpComponentsClientHttpRequestFactory.setConnectTimeout(3000); // 3s
	httpComponentsClientHttpRequestFactory.setReadTimeout(30000); // 30s
	return httpComponentsClientHttpRequestFactory;
    }

    @Bean("selfRestTemplate")
    RestTemplate selfRestTemplate(RestTemplateBuilder restTemplateBuilder,
	    HttpComponentsClientHttpRequestFactory factory) {
	return restTemplateBuilder.rootUri(targetSelf)
		.additionalMessageConverters(new StringHttpMessageConverter(StandardCharsets.UTF_8),
			new MappingJackson2HttpMessageConverter())
		.setConnectTimeout(Duration.ofSeconds(3)).setReadTimeout(Duration.ofSeconds(30))
		.interceptors(new HttpClientRequestInterceptor()).errorHandler(new RestTemplateErrorHandler())
		.customizers(
			restTemplate -> restTemplate.setRequestFactory(new BufferingClientHttpRequestFactory(factory)))
		.defaultHeader("organization", "supermoon")
		.build();
    }

}
