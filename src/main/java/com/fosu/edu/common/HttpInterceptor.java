package com.fosu.edu.common;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.fosu.edu.util.JsonMapper;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class HttpInterceptor extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		return super.preHandle(request, response, handler);
	}

	
	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
		//在方法正常结束时，删除线程
		removeThreadLocalInfo();
	}
	
	
	public void removeThreadLocalInfo() {
		RequestHolder.remove();
	} 
	
}
