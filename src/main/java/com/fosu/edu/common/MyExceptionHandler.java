package com.fosu.edu.common;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fosu.edu.exception.ParamException;
import com.fosu.edu.exception.PermissionException;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestControllerAdvice
public class MyExceptionHandler {
	@ExceptionHandler(RuntimeException.class)
	JsonData PremissionExHandler(HttpServletRequest request,Exception ex){
		String url = request.getRequestURI().toString();
		String defaultMsg = "system error";
		if(url.endsWith(".json")) {
			if(ex instanceof PermissionException) {
				JsonData result = JsonData.fail("PermissionException");
				return result;
				
			}else if(ex instanceof ParamException) {
				JsonData result = JsonData.fail("ParamException");
				return result;
			}else {
				log.error("unknow json exception"+url ,ex); 
				JsonData result= JsonData.fail(defaultMsg);
				return result;
				
			}
		}else if(url.endsWith(".page")) {
			log.error("unknow page exception"+url ,ex);
			JsonData result = JsonData.fail(defaultMsg);
			return result;
			
			
			
		}else {
			log.error("unknow exception"+url ,ex);
			JsonData result = JsonData.fail(defaultMsg);
			return result;
			
		}
		
	}
	
	
}
