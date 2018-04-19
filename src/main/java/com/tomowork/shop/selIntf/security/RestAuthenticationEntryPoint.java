package com.tomowork.shop.selIntf.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import com.tomowork.shop.api.ApiErrorVO;

/**
 * @author zlei
 */
public class RestAuthenticationEntryPoint implements AuthenticationEntryPoint, InitializingBean {

	private HttpMessageConverter messageConverter;

	@Override
	public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException, ServletException {
		ApiErrorVO error = new ApiErrorVO(HttpStatus.UNAUTHORIZED.value(), "认证失败", authException.getLocalizedMessage());

		ServerHttpResponse outputMessage = new ServletServerHttpResponse(response);
		outputMessage.setStatusCode(HttpStatus.UNAUTHORIZED);

		messageConverter.write(error, null, outputMessage);
	}

	public void setMessageConverter(HttpMessageConverter messageConverter) {
		this.messageConverter = messageConverter;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		if (messageConverter == null) {
			throw new IllegalArgumentException("Property 'messageConverter' is required");
		}
	}

}
