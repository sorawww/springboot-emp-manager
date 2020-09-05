package com.fosu.edu.util;

import org.apache.commons.lang3.StringUtils;

public class LevelUtil {
	//分隔符
	public final static String SEPARATOR = ".";
	//默认值
	public final static String ROOT="0";
	
	//  计算
	public static String calculateLevel(String parentLevel,int parentId) {
		//如果为空则返回0
		if(StringUtils.isEmpty(parentLevel)) {
			return ROOT;
		}else {
			return StringUtils.join(parentLevel,SEPARATOR,parentId);
		}
	}
}
