package com.fosu.edu.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.fosu.edu.bean.PageQuery;
import com.fosu.edu.dto.SearchLogDto;
import com.fosu.edu.model.SysLog;
import com.fosu.edu.model.SysLogWithBLOBs;
@Mapper
public interface SysLogMapper {
    int deleteByPrimaryKey(Integer id);

	int insert(SysLogWithBLOBs record);

	int insertSelective(SysLogWithBLOBs record);

	SysLogWithBLOBs selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(SysLogWithBLOBs record);

	int updateByPrimaryKeyWithBLOBs(SysLogWithBLOBs record);

	int updateByPrimaryKey(SysLog record);

    int insert(SysLog record);

    int insertSelective(SysLog record);

    int updateByPrimaryKeySelective(SysLog record);

    int updateByPrimaryKeyWithBLOBs(SysLog record);
    
    //TODO
    int countBySearchDto(@Param("dto")SearchLogDto dto);
    
    //TODO
    List<SysLogWithBLOBs> getPageListBySearchDto(@Param("dto")SearchLogDto dto,@Param("page")PageQuery page);

    
}