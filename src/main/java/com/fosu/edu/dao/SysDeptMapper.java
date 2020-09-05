package com.fosu.edu.dao;
import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import com.fosu.edu.model.SysDept;
@Mapper
public interface SysDeptMapper {
    int deleteByPrimaryKey(@Param("id")Integer id);

    int insert(SysDept record);

    int insertSelective(SysDept record);

    SysDept selectByPrimaryKey(@Param("id")Integer id);

    int updateByPrimaryKeySelective(SysDept record);

    int updateByPrimaryKey(SysDept record);
    
    
    //返回部门树
    List<SysDept> getAllDept();
    
    //获取所有子部门
    List<SysDept> getChildListByLevel(@Param("level") String level);
    
    //批量更新子部门的level
    void batchUpdateLevel(@Param("sysDeptList")List<SysDept> sysDeptsList);
    
    int countByNameAndParentId(@Param("parentId") Integer parentId,@Param("name") String name, @Param("id") Integer id);
    
    int countByParentId(@Param("deptId") int deptId);
}