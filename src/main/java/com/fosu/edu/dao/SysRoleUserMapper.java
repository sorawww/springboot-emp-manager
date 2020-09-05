package com.fosu.edu.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.fosu.edu.model.SysRoleUser;
import com.fosu.edu.model.SysRoleUserKey;
@Mapper
public interface SysRoleUserMapper {
    int deleteByPrimaryKey(SysRoleUserKey key);

	int insert(SysRoleUser record);

	int insertSelective(SysRoleUser record);

	SysRoleUser selectByPrimaryKey(SysRoleUserKey key);

	int updateByPrimaryKeySelective(SysRoleUser record);

	int updateByPrimaryKey(SysRoleUser record);

	int deleteByPrimaryKey(String roleId);
    SysRoleUser selectByPrimaryKey(String roleId);
    
    List<Integer> getRoleIdListByUserId(@Param("userId") Integer userId);
    
    List<Integer> getUserListByRoleId(@Param("roleId") Integer roleId);
    
    
   
    void deleteUserByRoleId(@Param("roleId") Integer roleId);

    
    void batchinsert(@Param("roleUserList") List<SysRoleUser> sysRoleUserList );
    
    List<Integer> getUserIdListByRoleIdList(@Param("roleIdList") List<Integer>roleIdList);
}