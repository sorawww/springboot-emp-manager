package com.fosu.edu.service;

import java.util.Date;
import java.util.List;

import org.apache.commons.codec.digest.Md5Crypt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fosu.edu.bean.PageQuery;
import com.fosu.edu.bean.PageResult;
import com.fosu.edu.common.RequestHolder;
import com.fosu.edu.dao.SysUserMapper;
import com.fosu.edu.exception.ParamException;
import com.fosu.edu.model.SysUser;
import com.fosu.edu.param.UserParam;
import com.fosu.edu.util.BeanValidator;
import com.fosu.edu.util.IpUtil;
import com.fosu.edu.util.MD5Util;
import com.fosu.edu.util.PasswordUtil;
import com.google.common.base.Preconditions;

@Service
public class SysUserService {
	@Autowired
	private SysUserMapper sysUserMapper;
	
	@Autowired
	private SysLogService sysLogService;
	
	public void save(UserParam param) {
		//校验数据无误
		BeanValidator.check(param);
		if(checkEmailExit(param.getMail(),param.getId())) {
			throw new ParamException("邮箱已被占用！");
		}
		if(checkTelExit(param.getTelephone(),param.getId())) {
			throw new ParamException("电话已被占用！");
		}
		
		SysUser user =SysUser.builder()
		.id(param.getId())
		.username(param.getUsername())
		.telephone(param.getTelephone())
		.mail(param.getMail())
		.deptId(param.getDeptId())
		.status(param.getStatus())
		.remark(param.getRemark())
		.build();
		
		user.setOperator(RequestHolder.getCurrentUser().getUsername());
		user.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
		user.setOperateTime(new Date());
		String password =PasswordUtil.randomPassword();
		//密码不能明文存储 需要Md5加密
		String encryptedPassword = MD5Util.encrypt(password);
 		user.setPassword(encryptedPassword);
		//TODO:sendEmail
		sysUserMapper.insertSelective(user);
		sysLogService.saveUserLog(null, user);
		
		
	}
	
	//更新电话或邮箱等信息
	public void update(UserParam param){
		//校验数据无误
		BeanValidator.check(param);
		if(checkEmailExit(param.getMail(),param.getId())) {
			throw new ParamException("邮箱已被占用！");
		}
		if(checkTelExit(param.getTelephone(),param.getId())) {
			throw new ParamException("电话已被占用！");
		}
		SysUser before = sysUserMapper.selectByPrimaryKey(param.getId());
		Preconditions.checkNotNull(before,"请检查用户主键是否有错误？");
		SysUser after =SysUser.builder()
				.id(param.getId())
				.username(param.getUsername())
				.telephone(param.getTelephone())
				.mail(param.getMail())
				.deptId(param.getDeptId())
				.status(param.getStatus())
				.remark(param.getRemark())
				.build();
				
		after.setOperator(RequestHolder.getCurrentUser().getUsername());
		after.setOperateIp(IpUtil.getRemoteIp(RequestHolder.getCurrentRequest()));
		after.setOperateTime(new Date());
		sysUserMapper.updateByPrimaryKeySelective(after);
		sysLogService.saveUserLog(before,after);
		
		
	}
	
	
	private boolean checkEmailExit(String mail,Integer userid) {
		
		return sysUserMapper.countByMail(mail, userid) >0;
	}
	private boolean checkTelExit(String telephone,Integer userid) {
		return sysUserMapper.countByTelephone(telephone, userid)>0;
	}
	
	public SysUser findByKeyWord(String KeyWord) {
		return sysUserMapper.findByKeyWord(KeyWord);
	}
	
	public PageResult<SysUser> getPageById(int deptId,PageQuery page){
		BeanValidator.check(page);
		//结果总条数
		int count = sysUserMapper.countByDeptId(deptId);
		PageResult<SysUser> rs = new PageResult<SysUser>();
		if(count>0) {
			List<SysUser> list = sysUserMapper.getPageByDeptId(deptId, page);
			
			rs.setTotal(count);
			rs.setData(list);
		}
		return rs;
	}
	public List<SysUser> getAll(){
		return sysUserMapper.getAll();
	}
	
	
}
