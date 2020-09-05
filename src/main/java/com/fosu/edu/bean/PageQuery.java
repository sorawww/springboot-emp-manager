package com.fosu.edu.bean;

import javax.validation.constraints.Min;

import lombok.Getter;
import lombok.Setter;

public class PageQuery {
	
	
	
	@Getter
	@Setter
	@Min(value = 1,message = "页码不合法")
	private int pageNo = 1;
	
	@Getter
	@Setter
	@Min(value = 1,message = "页面的尺寸不合法")
	private int pageSize = 10;
	
	@Setter
	private int offset;
	
	public int getOffset() {
		return (pageNo -1)*pageSize;
	}
	
}
