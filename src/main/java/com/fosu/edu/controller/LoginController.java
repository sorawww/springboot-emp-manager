package com.fosu.edu.controller;


import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.fosu.edu.common.JsonData;
import com.fosu.edu.dao.SysAclModuleMapper;
import com.fosu.edu.exception.ParamException;
import com.fosu.edu.exception.PermissionException;
import com.fosu.edu.model.SysAclModule;
import com.fosu.edu.param.TestVo;
import com.fosu.edu.util.BeanValidator;
import com.fosu.edu.util.JsonMapper;

import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
public class LoginController {
	@Autowired
	private SysAclModuleMapper moduleMapper ;
	
	@RequestMapping("/")
	public String getindex() {
		log.info("Test!!!!");
		return "index";
	}
	
	
	@RequestMapping("/test.json")
	@ResponseBody
	public JsonData getData() {	
		JsonData data = JsonData.success("成功显示！");
		return data;
	}
	
	@RequestMapping("/testex.json")
	public JsonData getDataException() {	
		throw new PermissionException();
		
	}
	
	@RequestMapping("/testvo.json")
	@ResponseBody
	public JsonData validate(TestVo vo) {
		log.info("validate");
		try {
			Map<String,String> map = BeanValidator.validateObject(vo);
			if(map == null) {
				log.info("map is null");
				
			}
			if(map !=null && map.entrySet().size()>0) {
				log.info("map is not null");
				for(Map.Entry<String, String> entry :map.entrySet()) {
					log.info("{}-->{}",entry.getKey(),entry.getValue());
				}
			}
		} catch (Exception e) {
			
		}
		return JsonData.success("test validate");
		
	}
	
	@RequestMapping("/testvo2.json")
	@ResponseBody
	public JsonData validate2(TestVo vo) throws ParamException   {
		log.info("validate");
		BeanValidator.check(vo);//check 方法会抛出异常
		return JsonData.success("test validate");
		
	}
	
	@RequestMapping("/testmodel.json")
	@ResponseBody
	public JsonData getModel()   {
		log.info("validate");
		SysAclModule module = moduleMapper.selectByPrimaryKey(1);
        log.info(JsonMapper.Obj2String(module));	
		return JsonData.success("test validate");
	}
	
	@RequestMapping("/getex.json")
	@ResponseBody
	public JsonData getException() throws ParamException   {
		throw new ParamException();
	}
	

   
}
