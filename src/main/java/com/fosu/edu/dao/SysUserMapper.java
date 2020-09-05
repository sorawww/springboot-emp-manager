package com.fosu.edu.dao;

import java.util.List;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.fosu.edu.bean.PageQuery;
import com.fosu.edu.model.SysUser;
@Mapper
public interface SysUserMapper {
    int deleteByPrimaryKey(Integer id);

	int insert(SysUser record);

	int insertSelective(SysUser record);

	SysUser selectByPrimaryKey(Integer id);

	int updateByPrimaryKeySelective(SysUser record);

	int updateByPrimaryKey(SysUser record);

	int deleteByPrimaryKey(String id);
    
	SysUser selectByPrimaryKey(String id);
	
	SysUser findByKeyWord(@Param ("keyword") String keyWord);
	
	int countByMail(@Param("mail") String mail, @Param("id") Integer id);
	
	int countByTelephone(@Param("telephone")String telephone ,@Param("id") Integer id);
	
	int countByDeptId(@Param("deptId") Integer deptId);
	
	List<SysUser> getPageByDeptId(@Param("deptId") int deptId,PageQuery page);
	
	List<SysUser> getUsersByIdLists(@Param("idList") List<Integer> idList);
	
	List<SysUser> getAll();
	
	int countByDeptId(@Param("deptId") int deptId);
}