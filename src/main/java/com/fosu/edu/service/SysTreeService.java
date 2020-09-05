package com.fosu.edu.service;




import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fosu.edu.dao.SysAclMapper;
import com.fosu.edu.dao.SysDeptMapper;
import com.fosu.edu.dto.AclDto;
import com.fosu.edu.dto.DeptLevelDto;
import com.fosu.edu.model.SysAcl;
import com.fosu.edu.model.SysAclModule;
import com.fosu.edu.dto.SysAclModuleLevelDto;
import com.fosu.edu.model.SysDept;
import com.fosu.edu.param.AclMouleParam;
import com.fosu.edu.util.JsonMapper;
import com.fosu.edu.util.LevelUtil;
import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class SysTreeService {
	@Autowired
	private SysDeptMapper sysDeptMapper;
	
	@Autowired
	private SysAclModuleService sysAclModuleService;
	
	@Autowired
	private SysCoreService sysCoreService;
	@Autowired
	private SysAclMapper sysAclMapper;
	
	public List<SysAclModuleLevelDto> userAclTree(int userId){
		
		List<SysAcl> userAcls = sysCoreService.getUserAclList(userId);
		List<AclDto> userAclsDto = Lists.newArrayList();
		for(SysAcl acl : userAcls) {
			AclDto dto = AclDto.adapt(acl);
			userAclsDto.add(dto);
		}
		return aclListToTree(userAclsDto);
	}
	
	public List<SysAclModuleLevelDto> roleTree(int roleId){
		//1.当前用户已分配的权限点
		List<SysAcl> userAclList = sysCoreService.getCurrentUserAclList();
		//2.当前角色分配的权限点
		List<SysAcl> roleAclList = sysCoreService.getRoleAclList(roleId);
		List<AclDto> aclDtoList = Lists.newArrayList();
		//构成一个Set
		Set<Integer> userAclIdSet= userAclList.stream().map(sysAcl->sysAcl.getId()).collect(Collectors.toSet());
		
		Set<Integer> roleAclIdSet = roleAclList.stream().map(sysAcl->sysAcl.getId()).collect(Collectors.toSet());
		List<SysAcl> allAclsList = sysAclMapper.getAll();
		//取并集
		Set<SysAcl> aclSet = new HashSet<>(allAclsList);
		aclSet.addAll(userAclList);
		for(SysAcl acl : allAclsList) {
			AclDto dto = AclDto.adapt(acl);
			if(userAclIdSet.contains(acl.getId())) {
				dto.setHasAcl(true);
				
			}
			if(roleAclIdSet.contains(acl.getId())) {
				dto.setChecked(true);
			}
			aclDtoList.add(dto);
		}
		
		return aclListToTree(aclDtoList);
	}
	public List<SysAclModuleLevelDto> aclListToTree(List<AclDto> aclDtoList){
		if(CollectionUtils.isEmpty(aclDtoList)) {
			return Lists.newArrayList();
		}
		List<SysAclModuleLevelDto> aclModuleLevelList = aclModuleTree();
		
		//权限模块的Id
		Multimap<Integer, AclDto> moduleIdAclMap = ArrayListMultimap.create();
		
		for(AclDto acl :aclDtoList) {
			if(acl.getStatus() == 1) {
				moduleIdAclMap.put(acl.getAclModuleId(), acl);
			}
		}
		
		bindAclsWithOrder(aclModuleLevelList, moduleIdAclMap);
		return aclModuleLevelList;
	}
	
	public void bindAclsWithOrder(List<SysAclModuleLevelDto> aclModuleLevelList,Multimap<Integer, AclDto> moduleIdAclMap) {
		if(CollectionUtils.isEmpty(aclModuleLevelList)) {
			return;
		}
		for(SysAclModuleLevelDto dto:aclModuleLevelList) {
			List<AclDto> aclDtoList  = (List<AclDto>) moduleIdAclMap.get(dto.getId());
			if(CollectionUtils.isNotEmpty(aclDtoList)) {
				Collections.sort(aclDtoList,aclSeqComparator);
				dto.setAclList(aclDtoList);
			}
			bindAclsWithOrder(dto.getAclModuleList(), moduleIdAclMap);
		}
	}
	
	 
	
	
	
	public List<SysAclModuleLevelDto> aclModuleTree() {
		List<SysAclModule> list=sysAclModuleService.getAll();
		List<SysAclModuleLevelDto> dtoList = Lists.newArrayList();
		for(SysAclModule module:list) {
			SysAclModuleLevelDto dto = SysAclModuleLevelDto.aclLevelDto(module);
			dtoList.add(dto);
		}
		
		return aclModuleListToTree(dtoList);
	}
	
	
	public List<SysAclModuleLevelDto> aclModuleListToTree(List<SysAclModuleLevelDto> list){
		if(CollectionUtils.isEmpty(list)) {
			return Lists.newArrayList();
		}
		// level -> [dept1, dept2, ...] Map<String, List<Object>>
        Multimap<String, SysAclModuleLevelDto> levelDeptMap = ArrayListMultimap.create();
        List<SysAclModuleLevelDto> rootList = Lists.newArrayList();

        for (SysAclModuleLevelDto dto : list) {
            levelDeptMap.put(dto.getLevel(), dto);
            //没有父级 
            if (LevelUtil.ROOT.equals(dto.getLevel())) {
                rootList.add(dto);
            }
        }
        // 按照seq从小到大排序
        Collections.sort(rootList,aclModuleSeqComparator);
        // 递归生成树
        transformAclModuleTree(rootList, LevelUtil.ROOT, levelDeptMap);
        return rootList;
	}
	
	
	private void transformAclModuleTree(List<SysAclModuleLevelDto> dtoList, String level,
			Multimap<String, SysAclModuleLevelDto> levelAclModuleMap) {
		for (int i = 0; i < dtoList.size(); i++) {
			SysAclModuleLevelDto dto = dtoList.get(i);
            String nextLevel = LevelUtil.calculateLevel(level, dto.getId());
            List<SysAclModuleLevelDto> tempList = (List<SysAclModuleLevelDto>) levelAclModuleMap.get(nextLevel);
            if (CollectionUtils.isNotEmpty(tempList)) {
                Collections.sort(tempList, aclModuleSeqComparator);
                dto.setAclModuleList(tempList);
                transformAclModuleTree(tempList, nextLevel, levelAclModuleMap);
            }
        }
		
	}
	public Comparator<SysAclModuleLevelDto> aclModuleSeqComparator = new Comparator<SysAclModuleLevelDto>() {

		@Override
		public int compare(SysAclModuleLevelDto o1, SysAclModuleLevelDto o2) {
			
			return o1.getSeq()-o2.getSeq();
		}

		
		
	};


	public List<DeptLevelDto> deptTree(){
		List<SysDept> deptlist=sysDeptMapper.getAllDept();
		List<DeptLevelDto> dtoList = Lists.newArrayList();
		for(SysDept dept:deptlist) {
			DeptLevelDto dto = DeptLevelDto.adapt(dept);
			dtoList.add(dto);
		}
		
		return deptListToTree(dtoList);
	}
	
	//将各个结点组合起来
	 public List<DeptLevelDto> deptListToTree(List<DeptLevelDto> deptLevelList) {
	        if (CollectionUtils.isEmpty(deptLevelList)) {
	            return Lists.newArrayList();
	        }
	        // level -> [dept1, dept2, ...] Map<String, List<Object>>
	        Multimap<String, DeptLevelDto> levelDeptMap = ArrayListMultimap.create();
	        List<DeptLevelDto> rootList = Lists.newArrayList();

	        for (DeptLevelDto dto : deptLevelList) {
	            levelDeptMap.put(dto.getLevel(), dto);
	            //没有父级 
	            if (LevelUtil.ROOT.equals(dto.getLevel())) {
	                rootList.add(dto);
	            }
	        }
	        // 按照seq从小到大排序
	        Collections.sort(rootList, new Comparator<DeptLevelDto>() {
	            public int compare(DeptLevelDto o1, DeptLevelDto o2) {
	                return o1.getSeq() - o2.getSeq();
	            }
	        });
	        // 递归生成树
	        transformDeptTree(rootList, LevelUtil.ROOT, levelDeptMap);
	        return rootList;
	    }

	private void transformDeptTree(List<DeptLevelDto> deptLevelList, String level,
			Multimap<String, DeptLevelDto> levelDeptMap) {
			for(int i = 0;i<deptLevelList.size();i++) {
				//遍历该层的元素
				DeptLevelDto deptLevelDto = deptLevelList.get(i);
				
				//处理当前层级的数据
				String nextLevel = LevelUtil.calculateLevel(level, deptLevelDto.getId());
				//处理下一层
				List<DeptLevelDto> tempList = (List<DeptLevelDto>) levelDeptMap.get(nextLevel);
				if(CollectionUtils.isNotEmpty(tempList)) {
					//排序
					Collections.sort(tempList,deptLevelDtoComparator);
					//进入下一层部门
					deptLevelDto.setDeptList(tempList);
					//进入下一层
					transformDeptTree(tempList, nextLevel, levelDeptMap);
				}
				
			}
			
		
	}
	public Comparator<DeptLevelDto> deptLevelDtoComparator = new Comparator<DeptLevelDto>() {

		@Override
		public int compare(DeptLevelDto o1, DeptLevelDto o2) {
			
			//根据seq比较排序
			return o1.getSeq()-o2.getSeq();
		}
		
	};
	
	public Comparator<AclDto> aclSeqComparator = new Comparator<AclDto>() {

		@Override
		public int compare(AclDto o1, AclDto o2) {
			return o1.getSeq() - o2.getSeq();
		}
		
		
		
	};
	
	
	
}
