package com.fosu.edu.common;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.ws.handler.Handler;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import com.fosu.edu.controller.LoginController;
import com.fosu.edu.exception.PermissionException;

import lombok.extern.slf4j.Slf4j;


@Component
@Slf4j
public class SpringExceptionResolver implements HandlerExceptionResolver {@Override
	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		// TODO Auto-generated method stub
		return null;
	}

	//异常处理方法
	//项目中所有请求json数据的，都使用json结尾，page页面则使用page结尾
	/*public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) { 
		String url = request.getRequestURI().toString();
		ModelAndView mv = null;
		String defaultMsg = "system error";
		
		if(url.endsWith(".json")) {
			if(ex instanceof PermissionException) {
				JsonData result = JsonData.fail(ex.getMessage());
				mv = new ModelAndView("jsonView",result.toMap());
			}else {
				log.error("unknow json exception"+url ,ex); 
				JsonData result= JsonData.fail(defaultMsg);
				mv = new ModelAndView("jsonView",result.toMap());
			}
		}else if(url.endsWith(".page")) {
			log.error("unknow page exception"+url ,ex);
			JsonData result = JsonData.fail(defaultMsg);
			mv = new ModelAndView("exception" ,result.toMap());
			
			
		}else {
			log.error("unknow exception"+url ,ex);
			JsonData result = JsonData.fail(defaultMsg);
			mv = new ModelAndView("jsonView" ,result.toMap());
		}
		return mv;
	}*/

}
