package com.fosu.edu.controller;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import com.fosu.edu.model.SysUser;
import com.fosu.edu.service.SysUserService;
import com.fosu.edu.util.MD5Util;


@Controller
public class UserController {
	
	@Autowired
	private SysUserService sysUserService;
	
	@RequestMapping("/logout.page")
	public void logout(HttpServletRequest request ,HttpServletResponse response) throws IOException, ServletException{
		request.getSession().invalidate();
		response.sendRedirect("signin.jsp");
	}
	
	
	
	
	
	
	@RequestMapping("/login.page")
	public void login(HttpServletRequest request ,HttpServletResponse response) throws IOException, ServletException{
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String errorMsg = "";
		String ret = request.getParameter("ret");
		if(StringUtils.isBlank(username) ) {
			errorMsg= "用户名不能为空";
		}else if( StringUtils.isBlank(password)) {
			errorMsg= "密码不能为空";
		}
		SysUser user = sysUserService.findByKeyWord(username);
		if(user == null) {
			errorMsg= "账号错误";
		}
		else if(!user.getPassword().equals(MD5Util.encrypt(password))) {
			errorMsg= "密码错误";
		}
		else if(user.getStatus() != 1) {
			errorMsg= "账号被冻结！";
		}else {
			request.getSession().setAttribute("user", user);
			if(StringUtils.isNotBlank(ret)) {
				response.sendRedirect(ret);
				return;
			}else {
				//跳转至首页
				response.sendRedirect("/admin/index.page");
				return;
			}
			
			
		}
		
		request.setAttribute("error", errorMsg);
		request.setAttribute(username, username);
		if(StringUtils.isNoneBlank(ret)) {
			request.setAttribute("ret", ret);
		}
		String path = "signin.jsp";
		response.sendRedirect(path);
		return;
		
	} 
		
	
}
