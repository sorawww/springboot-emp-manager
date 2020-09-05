package com.fosu.edu.util;

import java.util.List;
import java.util.stream.Collectors;

import com.google.common.base.Splitter;

//字符串转id数组
public class StringUtil {
	public static List<Integer> splitToListInt(String str){
		List<String> strList = Splitter.on(",").trimResults().omitEmptyStrings().splitToList(str);
		List<Integer> list = strList.stream().map(strItem ->Integer.parseInt(strItem)).collect(Collectors.toList());
		return list;
	}
}
