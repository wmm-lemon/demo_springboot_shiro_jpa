package com.wmm.shirodemo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.wmm.shirodemo.entity.SysPermission;
import com.wmm.shirodemo.entity.SysRole;
import com.wmm.shirodemo.repository.SysPermissionRepository;
import com.wmm.shirodemo.repository.SysRoleRepository;
import com.wmm.shirodemo.util.DateUtil;
import com.wmm.shirodemo.util.IdUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by wmm on 2019/4/17.
 */
@Service
public class SysRoleService {
    
    @Autowired
    SysRoleRepository sysRoleRepository;

    @Autowired
    SysPermissionRepository sysPermissionRepository;

    public List findAll(){
        return sysRoleRepository.findAllByOrderByCreateTimeDesc();
    }

    public Page findList(String nameParam,
                         int pageNum, int pageSize){
        Pageable pageable = PageRequest.of(pageNum,pageSize, Sort.Direction.DESC,"createTime");
        Page<SysRole> bookPage = sysRoleRepository.findAll(new Specification<SysRole>(){
            @Override
            public Predicate toPredicate(Root<SysRole> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList();
                if(!StringUtils.isEmpty(nameParam)){
                    list.add(criteriaBuilder.like(root.get("role").as(String.class), "%"+nameParam+"%"));
                }
                Predicate[] p = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(p));
            }
        },pageable);
        return bookPage;
    }

    public SysRole findById(String id){
        return sysRoleRepository.findById(id);
    }

    @Transactional
    public Map edit(SysRole bean,String permissionString){
        Map map = new HashMap();
        if (null==bean.getId() || "".equals(bean.getId())){//新增
            bean.setId(IdUtil.getId());
        }
        SysRole idBean = sysRoleRepository.findById(bean.getId());
        if (null==idBean){//新增
            idBean = sysRoleRepository.findByRole(bean.getRole());
            if (null!=idBean){
                map.put("code",0);
                map.put("msg","角色不能重复");
                return map;
            }
        }else{//修改
            SysRole nameBean = sysRoleRepository.findByRole(bean.getRole());
            if (null!=nameBean && !nameBean.getId().equals(bean.getId())){
                map.put("code",0);
                map.put("msg","角色不能重复");
                return map;
            }
        }

        //设置权限
        JSONArray array = JSON.parseArray(permissionString);
        List<SysPermission> permissions = new ArrayList<>();
        for (int i=0;i<array.size();i++){
            String permissionId = array.get(i).toString();
            SysPermission sysPermission = sysPermissionRepository.findById(permissionId);
            permissions.add(sysPermission);
        }
        bean.setPermissions(permissions);
        bean.setCreateTime(DateUtil.currentDateTime());
        bean = sysRoleRepository.save(bean);
        map.put("bean",bean);
        map.put("code",1);
        map.put("msg","成功");
        return map;
    }

    public Boolean findRoleUserById(String id){
        SysRole sysRole = sysRoleRepository.findById(id);
        if (null!=sysRole.getUserInfos() && sysRole.getUserInfos().size()>0){
            return true;
        }else{
            return false;
        }
    }

    @Transactional
    public void deleteById(String id){
        SysRole bean = new SysRole();
        bean.setId(id);
        sysRoleRepository.delete(bean);
    }
}
