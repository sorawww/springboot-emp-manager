package com.fosu.edu.util;

import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.apache.commons.collections.MapUtils;

import com.fosu.edu.exception.ParamException;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;




public class BeanValidator {
	//校验工厂
	private static ValidatorFactory  validatorFactory = Validation.buildDefaultValidatorFactory();
	//Class... groups 同时传入多个分组
	public static <T> Map<String, String> validate(T t ,Class... groups){
		Validator validator = validatorFactory.getValidator();
		Set ValidateResult = validator.validate(t, groups);
		if(ValidateResult.isEmpty()) {
			//使用工具类直接生成的空map	
			return Collections.emptyMap();
		}else {
			//实际上就是普通的构造方法
			LinkedHashMap errors = Maps.newLinkedHashMap();
			Iterator it = ValidateResult.iterator();
			while(it.hasNext()) {
				ConstraintViolation violation = (ConstraintViolation) it.next();
				errors.put(violation.getPropertyPath().toString(), violation.getMessage());
				
			}
			return errors;
		}
		
		
	}
	public static <T> Map<String, String> validateList(Collection<?> collection){
		//google 校验
		Preconditions.checkNotNull(collection);
		Iterator it = collection.iterator();
		Map<String,String> errors;
		do {
			if(!it.hasNext()) {
				return Collections.emptyMap();
			}
			Object object = it.next();
			//为何传入new Class[0]?https://www.cnblogs.com/keyi/p/7205796.html
			errors = validate(object, new Class[0]);
		}while (errors.isEmpty()); 
			return errors;
		
	}
	
	public static <T> Map<String, String> validateObject(Object first,Object... objects){
		
		if(objects !=null && objects.length>0) {
			return validateList(Lists.asList(first, objects));
			
		}else {
			return validate(first, new Class[0]);
		}
	}
	
	//抛出自定义异常的校验方法
	//https://blog.csdn.net/Richard_666/article/details/94407992
	public static void check(Object param) throws ParamException{
		Map<String, String> map = BeanValidator.validateObject(param);
		if(MapUtils.isNotEmpty(map)) {
			throw new ParamException(map.toString());
		}
	}
	
	
}
