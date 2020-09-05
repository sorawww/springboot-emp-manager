package com.fosu.edu.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.fosu.edu.model.SysAclModule;

@Mapper
public interface SysAclModuleMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAclModule record);

    int insertSelective(SysAclModule record);

    SysAclModule selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAclModule record);

    int updateByPrimaryKey(SysAclModule record);
    
    int countByNameAndParentId(@Param("parentId") Integer parentId, @Param("name") String name, @Param("id") Integer id);

	List<SysAclModule> getChildListByLevel(String string);

	void batchUpdateLevel(@Param("sysAclModuleList")List<SysAclModule> list);

	List<SysAclModule> selectAll();
	
	int countByParentId(@Param("parentId") Integer parentId);
}