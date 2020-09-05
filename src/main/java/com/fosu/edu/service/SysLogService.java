package com.fosu.edu.service;


import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.codehaus.jackson.type.TypeReference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fosu.edu.bean.LogType;
import com.fosu.edu.bean.PageQuery;
import com.fosu.edu.bean.PageResult;
import com.fosu.edu.common.RequestHolder;
import com.fosu.edu.dao.SysAclMapper;
import com.fosu.edu.dao.SysAclModuleMapper;
import com.fosu.edu.dao.SysDeptMapper;
import com.fosu.edu.dao.SysLogMapper;
import com.fosu.edu.dao.SysRoleMapper;
import com.fosu.edu.dao.SysUserMapper;
import com.fosu.edu.dto.SearchLogDto;
import com.fosu.edu.exception.ParamException;
import com.fosu.edu.model.SysAcl;
import com.fosu.edu.model.SysAclModule;
import com.fosu.edu.model.SysDept;
import com.fosu.edu.model.SysLog;
import com.fosu.edu.model.SysLogWithBLOBs;
import com.fosu.edu.model.SysRole;
import com.fosu.edu.model.SysUser;
import com.fosu.edu.param.SearchLogParam;
import com.fosu.edu.util.BeanValidator;
import com.fosu.edu.util.IpUtil;
import com.fosu.edu.util.JsonMapper;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;

import lombok.extern.slf4j.Slf4j;


@Service
public class SysLogService {

	 @Autowired
	 private SysLogMapper sysLogMapper;
	 
	 @Autowired
	 private SysDeptMapper sysDeptMapper;
	 
	 @Autowired
	 private SysUserMapper sysUserMapper;
	 
	 @Autowired
	 private SysAclModuleMapper sysAclModuleMapper;
	 
	 @Autowired
	 private SysAclMapper sysAclMapper;
	 
	 @Autowired
	 private SysRoleMapper sysRoleMapper;
	
	 @Autowired
	 private SysRoleAclService sysRoleAclService;
	 
	 @Autowired
	 private SysRoleUserService sysRoleUserService;
	 
	 
	 public void recover(int id) {
		 SysLogWithBLOBs sysLog = sysLogMapper.selectByPrimaryKey(id);
		 Preconditions.checkNotNull(sysLog,"参数不正确，请检查并重新尝试");
		 switch(sysLog.getType()) {
		 	case LogType.TYPE_DEPT:
		 		SysDept afterdept = sysDeptMapper.selectByPrimaryKey(sysLog.getTargetId());
		 		Preconditions.checkNotNull(afterdept,"数据不存在！");
		 		if(StringUtils.isBlank(sysLog.getNewValue()) || StringUtils.isBlank(sysLog.getOldValue())) {
		 			throw new ParamException("删除和新增不可还原");
		 		}
		 		SysDept beforedept = JsonMapper.String2Obj(sysLog.getOldValue(),new TypeReference<SysDept>() {
				});
		 		beforedept.setOperator(RequestHolder.getCurrentUser().getUsername());
		 		sysDeptMapper.updateByPrimaryKeySelective(beforedept);
		 		saveDeptLog(afterdept, beforedept);
		 		break;
		 	case LogType.TYPE_USER:
		 		SysUser afteruser = sysUserMapper.selectByPrimaryKey(sysLog.getTargetId());
		 		Preconditions.checkNotNull(afteruser,"参数不正确，请检查并重新尝试");
		 		if(StringUtils.isBlank(sysLog.getNewValue()) || StringUtils.isBlank(sysLog.getOldValue())) {
		 			throw new ParamException("删除和新增不可还原");
		 		}
		 		SysUser beforeuser = JsonMapper.String2Obj(sysLog.getOldValue(),new TypeReference<SysUser>() {
				});
		 		beforeuser.setOperator(RequestHolder.getCurrentUser().getUsername());
		 		beforeuser.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
		 		beforeuser.setOperateTime(new Date());
		 		sysUserMapper.updateByPrimaryKeySelective(beforeuser);
		 		saveUserLog(afteruser, beforeuser);
		 		break;
		 	case LogType.TYPE_ACL_MODULE:
		 		SysAclModule afterAcl_Module = sysAclModuleMapper.selectByPrimaryKey(sysLog.getTargetId());
		 		Preconditions.checkNotNull(afterAcl_Module,"参数不正确，请检查并重新尝试");
		 		if(StringUtils.isBlank(sysLog.getNewValue()) || StringUtils.isBlank(sysLog.getOldValue())) {
		 			throw new ParamException("删除和新增不可还原");
		 		}
		 		SysAclModule beforeAcl_Module = JsonMapper.String2Obj(sysLog.getOldValue(),new TypeReference<SysAclModule>() {
				});
		 		beforeAcl_Module.setOperator(RequestHolder.getCurrentUser().getUsername());
		 		beforeAcl_Module.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
		 		beforeAcl_Module.setOperateTime(new Date());
		 		sysAclModuleMapper.updateByPrimaryKeySelective(beforeAcl_Module);
		 		saveAlcModuleLog(afterAcl_Module, beforeAcl_Module);
		 		break;
		 	case LogType.TYPE_ACL:
		 		SysAcl after = sysAclMapper.selectByPrimaryKey(sysLog.getTargetId());
		 		Preconditions.checkNotNull(after,"数据不存在！");
		 		if(StringUtils.isBlank(sysLog.getNewValue()) || StringUtils.isBlank(sysLog.getOldValue())) {
		 			throw new ParamException("删除和新增不可还原");
		 		}
		 		SysAcl before = JsonMapper.String2Obj(sysLog.getOldValue(),new TypeReference<SysAcl>() {
				});
		 		before.setOperator(RequestHolder.getCurrentUser().getUsername());
		 		before.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
		 		before.setOperateTime(new Date());
		 		sysAclMapper.updateByPrimaryKeySelective(before);
		 		saveAclLog(after, before);
		 		break;
		 	case LogType.TYPE_ROLE:
		 		SysRole afterAcl = sysRoleMapper.selectByPrimaryKey(sysLog.getTargetId());
		 		Preconditions.checkNotNull(afterAcl,"数据不存在！");
		 		if(StringUtils.isBlank(sysLog.getNewValue()) || StringUtils.isBlank(sysLog.getOldValue())) {
		 			throw new ParamException("删除和新增不可还原");
		 		}
		 		SysRole beforeAcl = JsonMapper.String2Obj(sysLog.getOldValue(),new TypeReference<SysRole>() {
				});
		 		beforeAcl.setOperator(RequestHolder.getCurrentUser().getUsername());
		 		beforeAcl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
		 		beforeAcl.setOperateTime(new Date());
		 		sysRoleMapper.updateByPrimaryKeySelective(beforeAcl);
		 		break;
		 	case LogType.TYPE_ROLE_ACL:
		 		 SysRole aclRole = sysRoleMapper.selectByPrimaryKey(sysLog.getTargetId());
	                Preconditions.checkNotNull(aclRole, "角色已经不存在了");
	                sysRoleAclService.changeRoleAcls(sysLog.getTargetId(), JsonMapper.String2Obj(sysLog.getOldValue(), new TypeReference<List<Integer>>() {
	                }));
		 		break;
		 	case LogType.TYPE_ROLE_USER:
		 		 SysRole userRole = sysRoleMapper.selectByPrimaryKey(sysLog.getTargetId());
	                Preconditions.checkNotNull(userRole, "角色已经不存在了");
	                sysRoleUserService.changeRoleUsers(sysLog.getTargetId(), JsonMapper.String2Obj(sysLog.getOldValue(), new TypeReference<List<Integer>>() {
	                }));
		 		break;
		 	default:;
		 }
		 
		 
	 }
	 

	 public PageResult<SysLogWithBLOBs> searchPageList(SearchLogParam param , PageQuery page){
		 BeanValidator.check(page);
		 SearchLogDto dto = new SearchLogDto();
		 dto.setType(param.getType());
		 if(StringUtils.isNotBlank(param.getBeforeSeq())) {
			 dto.setBeforeSeq("%" + param.getBeforeSeq() + "%");
		 }
		 if(StringUtils.isNotBlank(param.getAfterSeq())) {
			 dto.setBeforeSeq("%" + param.getAfterSeq() + "%");
		 }
		 
		 if(StringUtils.isNotBlank(param.getOperator())) {
			 dto.setBeforeSeq("%" + param.getOperator() + "%");
		 }
		 
		 SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		 try {
			if(StringUtils.isNotBlank(param.getFromTime())) {
				dto.setFromTime(simpleDateFormat.parse(param.getFromTime()));	
			}
			if(StringUtils.isNotBlank(param.getToTime())) {
				dto.setFromTime(simpleDateFormat.parse(param.getToTime()));	
			}
		} catch (Exception e) {
		}
		 PageResult<SysLogWithBLOBs> rs = new PageResult<SysLogWithBLOBs>();
		 rs.setTotal(sysLogMapper.countBySearchDto(dto));
		 if(rs.getTotal()>0){
			 List<SysLogWithBLOBs>logList= sysLogMapper.getPageListBySearchDto(dto, page);
			 rs.setData(logList);
			 return rs;
		 }else {
			 //rs.setData(Lists.newArrayList());
			 return rs;
		 }
		 
	 }
	 
	
	 public void saveDeptLog(SysDept before , SysDept after) {
		SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
		sysLog.setType(LogType.TYPE_DEPT);
		sysLog.setTargetId(after == null ? before.getId() :after.getId());
		sysLog.setOldValue(before == null ? "":JsonMapper.Obj2String(before));
		sysLog.setNewValue(after == null ? "":JsonMapper.Obj2String(after));
		sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
		sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
		sysLog.setOperateTime(new Date());
		sysLog.setStatus(1);
		sysLogMapper.insertSelective(sysLog);
		
	 } 
	
	 public void saveUserLog(SysUser before, SysUser after) {
		    SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
			sysLog.setType(LogType.TYPE_USER);
			sysLog.setTargetId(after == null ? before.getId() :after.getId());
			sysLog.setOldValue(before == null ? "":JsonMapper.Obj2String(before));
			sysLog.setNewValue(after == null ? "":JsonMapper.Obj2String(after));
			sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
			sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
			sysLog.setOperateTime(new Date());
			sysLog.setStatus(1);
			sysLogMapper.insertSelective(sysLog);
	 }
	 
	 public void saveAlcModuleLog(SysAclModule before, SysAclModule after) {
		    SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
			sysLog.setType(LogType.TYPE_ACL_MODULE);
			sysLog.setTargetId(after == null ? before.getId() :after.getId());
			sysLog.setOldValue(before == null ? "":JsonMapper.Obj2String(before));
			sysLog.setNewValue(after == null ? "":JsonMapper.Obj2String(after));
			sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
			sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
			sysLog.setOperateTime(new Date());
			sysLog.setStatus(1);
			sysLogMapper.insertSelective(sysLog);
	 }
	 
	 public void saveAclLog(SysAcl before,SysAcl after) {
		    SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
			sysLog.setType(LogType.TYPE_ACL);
			sysLog.setTargetId(after == null ? before.getId() :after.getId());
			sysLog.setOldValue(before == null ? "":JsonMapper.Obj2String(before));
			sysLog.setNewValue(after == null ? "":JsonMapper.Obj2String(after));
			sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
			sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
			sysLog.setOperateTime(new Date());
			sysLog.setStatus(1);
			sysLogMapper.insertSelective(sysLog);
	 }
	 
	 
	 public void saveRoleLog(SysRole before,SysRole after) {
		    SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
			sysLog.setType(LogType.TYPE_ROLE);
			sysLog.setTargetId(after == null ? before.getId() :after.getId());
			sysLog.setOldValue(before == null ? "":JsonMapper.Obj2String(before));
			sysLog.setNewValue(after == null ? "":JsonMapper.Obj2String(after));
			sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
			sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
			sysLog.setOperateTime(new Date());
			sysLog.setStatus(1);
			sysLogMapper.insertSelective(sysLog);
	 }
	 
	 public void saveRoleAclLog(int roleId,List<Integer> before,List<Integer> after) {
		    SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
			sysLog.setType(LogType.TYPE_ROLE);
			sysLog.setTargetId(roleId);
			sysLog.setOldValue(before == null ? "":JsonMapper.Obj2String(before));
			sysLog.setNewValue(after == null ? "":JsonMapper.Obj2String(after));
			sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
			sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
			sysLog.setOperateTime(new Date());
			sysLog.setStatus(1);
			sysLogMapper.insertSelective(sysLog);
	 }

	public void saveRoleUSerLog(int roleId, List<Integer> before, List<Integer> after) {
		    SysLogWithBLOBs sysLog = new SysLogWithBLOBs();
			sysLog.setType(LogType.TYPE_ROLE);
			sysLog.setTargetId(roleId);
			sysLog.setOldValue(before == null ? "":JsonMapper.Obj2String(before));
			sysLog.setNewValue(after == null ? "":JsonMapper.Obj2String(after));
			sysLog.setOperator(RequestHolder.getCurrentUser().getUsername());
			sysLog.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
			sysLog.setOperateTime(new Date());
			sysLog.setStatus(1);
			sysLogMapper.insertSelective(sysLog);
		
	}
	 
		
}
