package com.fosu.edu.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.fosu.edu.common.RequestHolder;
import com.fosu.edu.dao.SysAclMapper;
import com.fosu.edu.dao.SysAclModuleMapper;
import com.fosu.edu.exception.ParamException;
import com.fosu.edu.model.SysAclModule;
import com.fosu.edu.model.SysDept;
import com.fosu.edu.param.AclMouleParam;
import com.fosu.edu.util.BeanValidator;
import com.fosu.edu.util.IpUtil;
import com.fosu.edu.util.JsonMapper;
import com.fosu.edu.util.LevelUtil;
import com.google.common.base.Preconditions;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class SysAclModuleService {
	@Autowired
	SysAclModuleMapper sysAclModuleMapper;
	
	@Autowired
	SysAclMapper sysAclMapper;
	
	@Autowired
	SysLogService sysLogService;
	
	//insert
	public void save(AclMouleParam param) {
		log.info("---------AclMouleParam----------------");
		BeanValidator.check(param);
		String msgString=JsonMapper.Obj2String(param);
		log.info(msgString);
		
		 if(checkExist(param.getParentId(), param.getName(), param.getId())) {
	            throw new ParamException("同一层级下存在相同名称的权限模块");
	        }
		SysAclModule aclModule = 
				SysAclModule.builder().name(param.getName())
				.parentId(param.getParentId())
				.remark(param.getRemark())
				.seq(param.getSeq())
				.status(param.getStatus())
				.build();
		aclModule.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
		aclModule.setOperateTime(new Date());
		aclModule.setOperator(RequestHolder.getCurrentUser().getUsername());
		aclModule.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
		sysAclModuleMapper.insertSelective(aclModule);
		sysLogService.saveAlcModuleLog(null, aclModule);

	}
	
	
	private boolean checkExist(Integer parentId, String aclModuleName, Integer deptId) {
		return sysAclModuleMapper.countByNameAndParentId(parentId, aclModuleName, deptId) > 0;
	}

	
	//update
	public void update(AclMouleParam param) {
		BeanValidator.check(param);
		if(checkExist(param.getParentId(), param.getName(), param.getId())) {
            throw new ParamException("同一层级下存在相同名称的权限模块");
        }
		SysAclModule before = sysAclModuleMapper.selectByPrimaryKey(param.getId());
		SysAclModule after = 
				SysAclModule.builder().name(param.getName())
				.parentId(param.getParentId())
				.remark(param.getRemark())
				.seq(param.getSeq())
				.status(param.getStatus())
				.build();
		after.setLevel(LevelUtil.calculateLevel(getLevel(param.getParentId()), param.getParentId()));
		after.setOperateTime(new Date());
		after.setOperator(RequestHolder.getCurrentUser().getUsername());
		after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
		updateWithChild(before, after);
		sysLogService.saveAlcModuleLog(before, after);
	}
	
	
	//递归更新
	@Transactional
	private void updateWithChild(SysAclModule before,SysAclModule after) {
		//判断是否有子部门 通过level判断
				String newLevelPrefix = after.getLevel();
				String oldLevelPrefix = before.getLevel();
				if(!after.getLevel().equals(before.getLevel())) {
					log.info("before.getLevel()" + before.getLevel());
					List<SysAclModule> list = sysAclModuleMapper.getChildListByLevel(before.getLevel());
					if(CollectionUtils.isNotEmpty(list)) {
						for(SysAclModule module : list) {
							String level = module.getLevel();
							if(level.indexOf(oldLevelPrefix) == 0) {
								level = newLevelPrefix+level.substring(oldLevelPrefix.length());
								module.setLevel(level);
							}
						}
						//批量更新
						sysAclModuleMapper.batchUpdateLevel(list);
					}
				}
				
		sysAclModuleMapper.updateByPrimaryKeySelective(after);
	}
	
	public String getLevel(Integer id){
		SysAclModule aclModule = sysAclModuleMapper.selectByPrimaryKey(id);
		if(aclModule == null) {
			return null;
		}else {
			return aclModule.getLevel();
		}
	}
	
	
	List<SysAclModule> getAll(){
		return sysAclModuleMapper.selectAll();
	}
	
	
	public void delete(int aclModuleid) {
		SysAclModule sysAclModule = sysAclModuleMapper.selectByPrimaryKey(aclModuleid);
		Preconditions.checkNotNull(sysAclModule,"要删除的权限模块不存在");
		if(sysAclModuleMapper.countByParentId(aclModuleid)>0) {
			throw new ParamException("当前模块有子模块，无法删除");
		}
		if(sysAclMapper.countByAclModuleId(aclModuleid)>0) {
			throw new ParamException("当前模块有权限点，无法删除");
		}
		
		sysAclModuleMapper.deleteByPrimaryKey(aclModuleid);
		
	}
	
	
}
