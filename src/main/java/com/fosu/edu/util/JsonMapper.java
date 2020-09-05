package com.fosu.edu.util;

import org.codehaus.jackson.map.DeserializationConfig;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig;
import org.codehaus.jackson.map.annotate.JsonSerialize;
import org.codehaus.jackson.map.ser.impl.SimpleFilterProvider;
import org.codehaus.jackson.type.TypeReference;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JsonMapper {
	private static ObjectMapper objectMapper = new ObjectMapper();
	
	static {
		//config
		objectMapper.disable(DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES);
		objectMapper.configure(SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
	    //objectMapper.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));
        objectMapper.setFilters(new SimpleFilterProvider().setFailOnUnknownId(false));
		objectMapper.setSerializationInclusion(JsonSerialize.Inclusion.NON_EMPTY);
	}
	
	
	public static <T> String Obj2String(T src) {
		if(src == null) {
			return null;
		}
		try {
			//如果原本就是String那就不转型直接返回即可
			return src instanceof String? (String) src :objectMapper.writeValueAsString(src);
				
			}catch (Exception e) {
				log.warn("ObjectMapper Exception",e);
				return null;
				
			}
		}
	
	public static  <T> T String2Obj(String src, TypeReference<T> typeReference){
		if(src == null || typeReference == null) {
			return null;
		}
			//如果要转换的本来就是String，也不进行转化，直接返回即可。
		 try {
	            return (T) (typeReference.getType().equals(String.class) ? src : objectMapper.readValue(src, typeReference));
	        } catch (Exception e) {
	            log.warn("parse String to Object exception, String:{}, TypeReference<T>:{}, error:{}", src, typeReference.getType(), e);
	            return null;
	        }
		
	} 
		
	
	
}
