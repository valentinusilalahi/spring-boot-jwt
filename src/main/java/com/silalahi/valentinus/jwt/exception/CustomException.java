package com.silalahi.valentinus.jwt.exception;

import org.springframework.http.HttpStatus;

public class CustomException extends RuntimeException {

	private static final long serialVersionUID = 4933336507400489701L;

	private final String message;
	private final HttpStatus status;

	public CustomException(String message, HttpStatus status) {
		this.message = message;
		this.status = status;
	}

	@Override
	public String getMessage() {
		return message;
	}

	public HttpStatus getHttpStatus() {
		return status;
	}

}
