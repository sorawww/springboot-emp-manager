package com.fosu.edu.common;


import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

@Component("applicationContextHelper")
public class ApplicationContextHelper {
	 private static ApplicationContext applicationContext;
	 
	 public void setApplicationContext(ApplicationContext context) throws BeansException {
		 applicationContext = context;
		 
	 }
	 
	 public static <T> T popBean(Class<T> clazz) {
		 if(applicationContext == null) {
			 return null;
		 }
		 
		 return applicationContext.getBean(clazz);
	 }
	 
	 public static <T> T popBean(String name,Class<T> clazz) {
		 if(applicationContext == null) {
			 return null;
		 }
		 return applicationContext.getBean(name,clazz);
	 }
	 
}
