package com.fosu.edu.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fosu.edu.common.RequestHolder;
import com.fosu.edu.dao.SysDeptMapper;
import com.fosu.edu.dao.SysUserMapper;
import com.fosu.edu.exception.ParamException;
import com.fosu.edu.model.SysDept;
import com.fosu.edu.param.DeptParam;
import com.fosu.edu.util.BeanValidator;
import com.fosu.edu.util.IpUtil;
import com.fosu.edu.util.LevelUtil;
import com.google.common.base.Preconditions;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SysDeptService {
	@Autowired
	private SysDeptMapper mapper;
	
	@Autowired
	private SysUserMapper sysUserMapper;
	
	@Autowired
	private SysLogService sysLogService;
	
	public boolean save(DeptParam param) throws ParamException{
		log.info("service层！");
		//1.验证传入的param是否符合规范
		BeanValidator.check(param);	
		if(checkExist(param.getParentId(),param.getName(),param.getId())) {
			throw new ParamException("数据实际上已存在！请检查你的添加是否有误！");
		}
		SysDept dept = SysDept.builder()
				.name(param.getName()) 
				.parentId(param.getParentId())
				.remark(param.getRemark())
				.seq(param.getSeq())
				.build();
		dept.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
		//操作者是当前的用户
		dept.setOperator(RequestHolder.getCurrentUser().getUsername());
		dept.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
		dept.setOperateTime(new Date());
		int i = mapper.insertSelective(dept);
		sysLogService.saveDeptLog(null, dept);
		if(i!=0) {
			return true;
		}else {
			return false;
		}
		
		
	}
	
	//检查添加是否会重复
	public boolean checkExist(Integer parentId,String deptName,Integer deptId) {
			return mapper.countByNameAndParentId(parentId, deptName, deptId)>0;
	}
	//获取参数level
	private String getLevel(Integer deptId) {
		SysDept dept = mapper.selectByPrimaryKey(deptId);
		if(dept == null) {
			return null;
		}else {
			return dept.getLevel();
		}
	}
	
	//update
	public void update(DeptParam param) {
		log.info("service层！update");
		//1.验证传入的param是否符合规范
		BeanValidator.check(param);	
		/*if(checkExist(param.getParentId(),param.getName(),param.getId())) {
			throw new ParamException("同一层级下存在相同名称的部门");
		}*/
		SysDept before = mapper.selectByPrimaryKey(param.getId());
		//确认更新之前，这个对象不为空
		Preconditions.checkNotNull(before,"不存在，无法更新,需要插入");
		
		SysDept after = SysDept.builder()
				.name(param.getName()) 
				.parentId(param.getParentId())
				.id(param.getId())
				.remark(param.getRemark())
				.seq(param.getSeq())
				.build();
		after.setOperator(RequestHolder.getCurrentUser().getUsername());
		after.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
		after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
		after.setOperateTime(new Date());
		updateWithChild(before, after);
		sysLogService.saveDeptLog(before,after);
		
		
		
	}
	//事务处理，必须保证部门和所有子部门的处理一致
	@Transactional
	private void updateWithChild(SysDept before ,SysDept after) {
		//判断是否有子部门 通过level判断
		String newLevelPrefix = after.getLevel();
		String oldLevelPrefix = before.getLevel();
		//level有所更改需要修改子部门
		if(!after.getLevel().equals(before.getLevel())) {
			log.info("before.getLevel()" + before.getLevel());
			List<SysDept> list = mapper.getChildListByLevel(before.getLevel());
			if(CollectionUtils.isNotEmpty(list)) {
				for(SysDept dept : list) {
					String level = dept.getLevel();
					if(level.indexOf(oldLevelPrefix) == 0) {
						level = newLevelPrefix+level.substring(oldLevelPrefix.length());
						dept.setLevel(level);
					}
				}
				//批量更新
				mapper.batchUpdateLevel(list);
			}
		}
		
		//更新主部门
		mapper.updateByPrimaryKey(after);
	}
	
	
	public List<SysDept> getByLevel(){
		return mapper.getChildListByLevel("0");
	}
	
	//删除
	public void deleteById(Integer deptId) {
		SysDept dept = mapper.selectByPrimaryKey(deptId);
		Preconditions.checkNotNull(deptId,"部门不存在，无法删除");
		if(mapper.countByParentId(deptId)>0) {
			throw new ParamException("当前部门下面仍有子部门，无法删除");
		}
		if(sysUserMapper.countByDeptId(deptId)>0) {
			throw new ParamException("当前部门下面仍有用户，无法删除");
		}
		mapper.deleteByPrimaryKey(deptId);
		
		
		mapper.deleteByPrimaryKey(deptId);
}
}
