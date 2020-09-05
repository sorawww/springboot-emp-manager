package com.fosu.edu.bean;

import java.util.List;

import com.google.common.collect.Lists;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class PageResult< T > {
	public PageResult() {
		// TODO Auto-generated constructor stub
	}

	private List<T> data = Lists.newArrayList();
	
	private int total = 0; 
}
