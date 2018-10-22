package com.silalahi.valentinus.jwt.exception;

import java.io.IOException;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.springframework.boot.autoconfigure.web.DefaultErrorAttributes;
import org.springframework.boot.autoconfigure.web.ErrorAttributes;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.RequestAttributes;

@RestControllerAdvice
public class GlobalExceptionHandlerController {
	
	public ErrorAttributes errorAttributes() {
		return new DefaultErrorAttributes() {
			@Override
			public Map<String, Object> getErrorAttributes(RequestAttributes requestAttributes, boolean includeStackTrace){
				Map<String, Object> errorAttributes = super.getErrorAttributes(requestAttributes, includeStackTrace);
				errorAttributes.remove("exception");
				return errorAttributes;
			}
		};
	}
	
	@ExceptionHandler(CustomException.class)
	public void handleCustomException(HttpServletResponse response, CustomException ex) throws IOException {
		response.sendError(ex.getHttpStatus().value(), ex.getMessage());
	}
	
	@ExceptionHandler(CustomException.class)
	public void handleAccessDeniedException(HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.FORBIDDEN.value(), "Access Denied");
	}
	
	public void handleException(HttpServletResponse response) throws IOException {
		response.sendError(HttpStatus.BAD_REQUEST.value(), "Something went wrong");
	}
	
}
