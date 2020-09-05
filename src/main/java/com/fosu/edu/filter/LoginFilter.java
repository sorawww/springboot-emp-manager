package com.fosu.edu.filter;

import java.io.IOException;


import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.core.annotation.Order;

import com.fosu.edu.common.RequestHolder;
import com.fosu.edu.model.SysUser;

import lombok.extern.slf4j.Slf4j;

@WebFilter(filterName = "loginFilter",urlPatterns = {"/sys/*","/admin/*"})
@Slf4j
public class LoginFilter implements Filter {

	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest res= (HttpServletRequest) request; 
		HttpServletResponse resp = (HttpServletResponse) response;
		//login成功后会存储
		SysUser user = (SysUser) res.getSession().getAttribute("user");
		//null说明未登陆，跳转至登录页面
		if(user == null) {
			resp.sendRedirect("/signin.jsp");
			return;
		}
		RequestHolder.add(res);
		RequestHolder.add(user);
		
		//传递到下一个Filter
		chain.doFilter(request, response);
		
	}
	
}
