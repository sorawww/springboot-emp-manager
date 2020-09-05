package com.fosu.edu.filter;

import java.io.IOException;
import java.util.List;
import java.util.Set;
import java.util.Spliterator;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.annotation.WebInitParam;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;

import com.fosu.edu.common.JsonData;
import com.fosu.edu.common.RequestHolder;
import com.fosu.edu.model.SysUser;
import com.fosu.edu.service.SysCoreService;
import com.fosu.edu.util.JsonMapper;
import com.google.common.base.Preconditions;
import com.google.common.base.Splitter;
import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@WebFilter(filterName = "AclControlFilte",urlPatterns = {"/sys/*","/admin/*"})
@WebInitParam(name="exclusionUrls",value="/sys/user/noAuth.page,/login.page")
@Order()
public class ZAclControlFilter  implements Filter {
	@Autowired
	private SysCoreService sysCoreService;
	
	private static Set<String> exclusionUrlSet = Sets.newConcurrentHashSet();
	
	private final static String noAuthUrl = "/sys/user/noAuth.page";
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		
		//String exclusionUrls =  filterConfig.getInitParameter("exclusionUrls");
		String exclusionUrls = "";
		List<String> exclusionUrlsList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(exclusionUrls);
		Preconditions.checkNotNull(exclusionUrlsList,"为空");
		exclusionUrlSet = Sets.newConcurrentHashSet(exclusionUrlsList);
		exclusionUrlSet.add(noAuthUrl);
	}
	
	@Override
	public void doFilter(ServletRequest Request, ServletResponse Response, FilterChain chain)
			throws IOException, ServletException {
		HttpServletRequest  request = (HttpServletRequest) Request;
		HttpServletResponse response = (HttpServletResponse) Response;
		String servletPath =  request.getServletPath();
		//exclusionUrlSet 白名单
		if(exclusionUrlSet.contains(servletPath)) {
			chain.doFilter(request, response);
			return;
		}
		//当前用户没有权限访问
		SysUser sysUser = RequestHolder.getCurrentUser();
		if(sysUser == null) {
			log.info("你没有权限访问");
			noAuth(request, response);
			return;
		}
		if(!sysCoreService.hasUrlAcl(servletPath)) {
			log.info("你没有权限访问");
			noAuth(request, response);
			return;
		}
		
		chain.doFilter(Request, Response);
		return;
		
		
	}
	private void noAuth(HttpServletRequest  request,HttpServletResponse response) throws IOException {
		String servletPath = request.getServletPath();
		//请求Json格式
		if(servletPath.endsWith(".json")) {
			JsonData jsonData = JsonData.fail("没有访问权限");
			response.getWriter().print(JsonMapper.Obj2String(jsonData));

		}else {
			clientRedirect(noAuthUrl,response);
			return;	 
		}
	}
	
	private void clientRedirect(String url,HttpServletResponse response) throws IOException {
	     response.setHeader("Content-type", "text/html");
		 response.getWriter().print("<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Strict//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd\">\n"
	                + "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" + "<head>\n" + "<meta http-equiv=\"content-type\" content=\"text/html; charset=UTF-8\"/>\n"
	                + "<title>跳转中...</title>\n" + "</head>\n" + "<body>\n" + "跳转中，请稍候...\n" + "<script type=\"text/javascript\">//<![CDATA[\n"
	                + "window.location.href='" + url + "?ret='+encodeURIComponent(window.location.href);\n" + "//]]></script>\n" + "</body>\n" + "</html>\n");
	}
}
