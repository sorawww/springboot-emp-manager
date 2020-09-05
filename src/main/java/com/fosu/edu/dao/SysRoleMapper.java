package com.fosu.edu.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.fosu.edu.model.SysRole;
@Mapper
public interface SysRoleMapper {
    int deleteByPrimaryKey(String id);

	int insert(SysRole record);

	int insertSelective(SysRole record);

	SysRole selectByPrimaryKey(String id);

	int updateByPrimaryKeySelective(SysRole record);

	int updateByPrimaryKey(SysRole record);

	int deleteByPrimaryKey(Integer id);

    
    SysRole selectByPrimaryKey(Integer id);
    
    List<SysRole> getAll();
    
    int countByName(@Param("name") String name,@Param("id") Integer id);
    
    List<SysRole> getRoleByIdList (@Param("idList") List<Integer> idList);
    
}