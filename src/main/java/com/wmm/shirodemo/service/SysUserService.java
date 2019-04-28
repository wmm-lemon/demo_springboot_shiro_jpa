package com.wmm.shirodemo.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.wmm.shirodemo.entity.SysRole;
import com.wmm.shirodemo.entity.SysUser;
import com.wmm.shirodemo.repository.SysRoleRepository;
import com.wmm.shirodemo.repository.SysUserRepository;
import com.wmm.shirodemo.util.DateUtil;
import com.wmm.shirodemo.util.IdUtil;
import com.wmm.shirodemo.util.PasswordUtil;
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
 * Created by wmm on 2019/4/12.
 */
@Service
public class SysUserService {

    @Autowired
    SysUserRepository sysUserRepository;
    @Autowired
    SysRoleRepository sysRoleRepository;

    public SysUser findByUsername(String username){
        return sysUserRepository.findByUsername(username);
    }

    public Page findList(String nameParam,
                         int pageNum, int pageSize){
        Pageable pageable = PageRequest.of(pageNum,pageSize, Sort.Direction.DESC,"createTime");
        Page<SysUser> bookPage = sysUserRepository.findAll(new Specification<SysUser>(){
            @Override
            public Predicate toPredicate(Root<SysUser> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> list = new ArrayList();
                if(!StringUtils.isEmpty(nameParam)){
                    list.add(criteriaBuilder.like(root.get("name").as(String.class), "%"+nameParam+"%"));
                }
                Predicate[] p = new Predicate[list.size()];
                return criteriaBuilder.and(list.toArray(p));
            }
        },pageable);
        return bookPage;
    }

    public SysUser findById(String id){
        return sysUserRepository.findByUserId(id);
    }

    @Transactional
    public Map edit(SysUser bean,String roleIdString){
        Map map = new HashMap();
        if (null==bean.getUserId() || "".equals(bean.getUserId())){//新增
            bean.setUserId(IdUtil.getId());
        }
        SysUser idBean = sysUserRepository.findByUserId(bean.getUserId());
        if (null==idBean){//新增
            idBean = sysUserRepository.findByUsername(bean.getUsername());
            if (null!=idBean){
                map.put("code",0);
                map.put("msg","名称不能重复");
                return map;
            }
            bean.setPassword(PasswordUtil.encryptPassword(bean.getPassword(),bean.getUsername()));
        }else{//修改
            SysUser nameBean = sysUserRepository.findByUsername(bean.getUsername());
            if (null!=nameBean && !nameBean.getUserId().equals(bean.getUserId())){
                map.put("code",0);
                map.put("msg","名称不能重复");
                return map;
            }
            if (null!=nameBean && !nameBean.getPassword().equals(bean.getPassword())){
                bean.setPassword(PasswordUtil.encryptPassword(bean.getPassword(),bean.getUsername()));
            }
        }
        //设置角色
        JSONArray array = JSON.parseArray(roleIdString);
        List<SysRole> roleList = new ArrayList<>();
        for (int i=0;i<array.size();i++){
            String roleId = array.get(i).toString();
            SysRole sysRole = sysRoleRepository.findById(roleId);
            roleList.add(sysRole);
        }
        bean.setRoleList(roleList);
        bean.setCreateTime(DateUtil.currentDateTime());
        bean = sysUserRepository.save(bean);
        map.put("bean",bean);
        map.put("code",1);
        map.put("msg","成功");
        return map;
    }

    @Transactional
    public void deleteById(String id){
        SysUser bean = new SysUser();
        bean.setUserId(id);
        sysUserRepository.delete(bean);
    }
}
