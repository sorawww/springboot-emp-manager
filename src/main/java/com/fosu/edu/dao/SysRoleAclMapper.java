package com.fosu.edu.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.fosu.edu.model.SysRole;
import com.fosu.edu.model.SysRoleAcl;
@Mapper
public interface SysRoleAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysRoleAcl record);

    int insertSelective(SysRoleAcl record);

    SysRoleAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysRoleAcl record);

    int updateByPrimaryKey(SysRoleAcl record);
    
    
    List<Integer> getAclIdListByRoleIdList(@Param("roleIdList") List<Integer> roleIdList);
    
    
    //删除
    void deleteByRoleId(@Param("roleId") int roleId);
    
    //批量更新
    void batchInsert(@Param("roleAclList") List<SysRoleAcl> roleAclList);
    
    //
    List<Integer> getRolesIdListByaclId(@Param("aclId") Integer aclId);
    
}