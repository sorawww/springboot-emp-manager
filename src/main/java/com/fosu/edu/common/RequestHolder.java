package com.fosu.edu.common;

import javax.servlet.http.HttpServletRequest;

import com.fosu.edu.model.SysUser;

public class RequestHolder {
	private static final ThreadLocal<SysUser> userHolder  = new ThreadLocal<SysUser>();
	
	private static final ThreadLocal<HttpServletRequest> requestHolder = new ThreadLocal<HttpServletRequest>();
	
	/**新增*/
	public static void add(SysUser sysUser) {
		userHolder.set(sysUser);
	}
	
	public static void add(HttpServletRequest request) {
		requestHolder.set(request);
	}

	/**获取*/
	public static SysUser getCurrentUser() {
		return userHolder.get();
	}
	
	public static HttpServletRequest getCurrentRequest() {
		return requestHolder.get();
	}
	/**移除*/
	public static void remove() {
		userHolder.remove();
		requestHolder.remove();
	}
	
	
}
