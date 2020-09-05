package com.fosu.edu.service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fosu.edu.bean.PageQuery;
import com.fosu.edu.bean.PageResult;
import com.fosu.edu.common.JsonData;
import com.fosu.edu.common.RequestHolder;
import com.fosu.edu.dao.SysAclMapper;
import com.fosu.edu.exception.ParamException;
import com.fosu.edu.model.SysAcl;
import com.fosu.edu.param.AclParam;
import com.fosu.edu.util.BeanValidator;
import com.fosu.edu.util.IpUtil;
import com.google.common.base.Preconditions;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AclService {
	
	@Autowired
	private SysAclMapper sysAclMapper;
	
	
	//新增
	public JsonData save(AclParam param) {
		BeanValidator.check(param);
		/*if(checkExits(param.getAclModuleId(), param.getName(), param.getId())) {
			throw new ParamException();
		}*/
		SysAcl acl = 
				SysAcl.builder()
				.name(param.getName())
				.aclModuleId(param.getAclModuleId())
				.url(param.getUrl())
				.type(param.getType())
				.status(param.getStatus())
				.seq(param.getSeq())
				.remark(param.getRemark())
				.build();
		acl.setCode(generateCode());
		acl.setOperateTime(new Date());
		acl.setOperator(RequestHolder.getCurrentUser().getUsername());
		acl.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
		sysAclMapper.insert(acl);
		return JsonData.success();
	}
	//更新
	public JsonData update(AclParam param) {
		log.info("====================service update ============================");
		BeanValidator.check(param);
		SysAcl before = sysAclMapper.selectByPrimaryKey(param.getId());
		Preconditions.checkNotNull(before,"原对象不存在，无法更新");
		SysAcl after = 
				SysAcl.builder()
				.name(param.getName())
				.aclModuleId(param.getAclModuleId())
				.url(param.getUrl())
				.type(param.getType())
				.status(param.getStatus())
				.seq(param.getSeq())
				.remark(param.getRemark())
				.id(param.getId())
				.build();
		after.setOperateTime(new Date());
		after.setOperator(RequestHolder.getCurrentUser().getUsername());
		after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
		sysAclMapper.updateByPrimaryKeySelective(after);
		return JsonData.success();
	}
	
	//TODO
	private boolean checkExits(int aclMouleId,String aclName,Integer id) {
		return sysAclMapper.countByNameAndAclModuleId(aclMouleId,aclName,id)>0;
	}
	//生成唯一的指定code
	 public String generateCode() {
	        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
	        return dateFormat.format(new Date()) + "_" + (int)(Math.random() * 100);
	    }
	//通过模块id查询
	public PageResult<SysAcl> findAclsByModuleId(int ModuleId, PageQuery page){
		BeanValidator.check(page);
		int  cnt = sysAclMapper.countByAclModuleId(ModuleId);
		if(cnt>0) {
			List<SysAcl> aclList = sysAclMapper.getPageByAclModuleId(ModuleId, page);
			PageResult<SysAcl> rs = new PageResult<>();
			rs.setData(aclList);
			rs.setTotal(cnt);
			return rs;
		}else {
			PageResult<SysAcl> rs = new PageResult<>();
			rs.setData(null);
			rs.setTotal(0);
			return rs;
		}
		
		
	}
}
