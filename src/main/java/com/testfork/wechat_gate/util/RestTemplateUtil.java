package com.testfork.wechat_gate.util;

import org.springframework.http.client.SimpleClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * description: http 工具类
 * 
 * @author xubin
 *
 */
public class RestTemplateUtil {

	private static RestTemplate restTemplate = null;
	static {
		SimpleClientHttpRequestFactory clientHttpRequestFactory = new SimpleClientHttpRequestFactory();
		clientHttpRequestFactory.setReadTimeout(5000);
		clientHttpRequestFactory.setConnectTimeout(5000);
		restTemplate = new RestTemplate(clientHttpRequestFactory);
	}

	public static <T> T getForObject(String url, Class<T> clazz, Object... uriVariables) {
		return restTemplate.getForObject(url, clazz, uriVariables);
	}

	public static <T> T postForObject(String url, Object request, Class<T> clazz) {
		return restTemplate.postForObject(url, request, clazz);
	}
}
