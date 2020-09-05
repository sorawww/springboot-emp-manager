package com.fosu.edu.controller;

import javax.validation.constraints.Positive;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.deser.impl.CreatorCandidate.Param;
import com.fosu.edu.bean.PageQuery;
import com.fosu.edu.common.JsonData;
import com.fosu.edu.param.SearchLogParam;
import com.fosu.edu.service.SysLogService;
import com.fosu.edu.util.JsonMapper;

import lombok.extern.slf4j.Slf4j;

@Controller
@RequestMapping("/sys/log")
@Slf4j
public class SysLogController {
	@Autowired
	private SysLogService sysLogService;
	
	@RequestMapping("/log.page")
	public String getPage() {
		return "log";
	}
	
	@RequestMapping("/recover.json")
	@ResponseBody
	public JsonData recover(@RequestParam("id") int id) {
		sysLogService.recover(id);
		return JsonData.success();
	}
	
	
	@PostMapping("/page.json")
	@ResponseBody
	public JsonData searchpage(SearchLogParam param,PageQuery page) {
		return JsonData.success(sysLogService.searchPageList(param,page));
	}
	
	
	
}
