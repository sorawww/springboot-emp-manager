package com.fosu.edu.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.fosu.edu.bean.PageQuery;
import com.fosu.edu.model.SysAcl;

import lombok.experimental.PackagePrivate;
@Mapper
public interface SysAclMapper {
    int deleteByPrimaryKey(Integer id);

    int insert(SysAcl record);

    int insertSelective(SysAcl record);

    SysAcl selectByPrimaryKey(Integer id);

    int updateByPrimaryKeySelective(SysAcl record);

    int updateByPrimaryKey(SysAcl record);
    
    int countByNameAndAclModuleId(@Param("aclModuleId") int aclModuleId,@Param("name") String name,@Param("id") int id); 
    
    int countByAclModuleId(@Param("aclModuleId") int aclModuleId);
    
    List<SysAcl> getPageByAclModuleId(@Param("aclModuleId") int aclModuleId,@Param("page") PageQuery page);

	List<SysAcl> getAll();
	
	List<SysAcl> getByIdList(@Param("idList") List<Integer> idList );
	
	//public List<Integer> getAllaclModuleId();
	
	public int countByAclModuleId(@Param("aclModuleId") Integer aclModuleId);
	
	List<SysAcl> getByUrl(@Param("url") String url );
}