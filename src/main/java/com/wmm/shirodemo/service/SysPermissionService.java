package com.wmm.shirodemo.service;

import com.wmm.shirodemo.entity.SysPermission;
import com.wmm.shirodemo.repository.SysPermissionRepository;
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
public class SysPermissionService {

    @Autowired
    SysPermissionRepository sysPermissionRepository;

    public List findAll(){
        return sysPermissionRepository.findAllByOrderByParentIdAscOrderNumAsc();
    }

    public Page findList(String nameParam,
                         int pageNum, int pageSize){
        Pageable pageable = PageRequest.of(pageNum,pageSize, Sort.Direction.ASC,"parentId","orderNum");
        Page<SysPermission> bookPage = sysPermissionRepository.findAll(new Specification<SysPermission>(){
            @Override
            public Predicate toPredicate(Root<SysPermission> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
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

    public SysPermission findById(String id){
        return sysPermissionRepository.findById(id);
    }

    @Transactional
    public Map edit(SysPermission bean){
        Map map = new HashMap();
        if (null==bean.getId() || "".equals(bean.getId())){//新增
            bean.setId(IdUtil.getId());
        }
        SysPermission idBean = sysPermissionRepository.findById(bean.getId());
        if (null==idBean){//新增
            idBean = sysPermissionRepository.findByName(bean.getName());
            if (null!=idBean){
                map.put("code",0);
                map.put("msg","权限名称不能重复");
                return map;
            }
        }else{//修改
            SysPermission nameBean = sysPermissionRepository.findByName(bean.getName());
            if (null!=nameBean && !nameBean.getId().equals(bean.getId())){
                map.put("code",0);
                map.put("msg","权限名称不能重复");
                return map;
            }
        }

        bean.setCreateTime(DateUtil.currentDateTime());
        bean = sysPermissionRepository.save(bean);
        map.put("bean",bean);
        map.put("code",1);
        map.put("msg","成功");
        return map;
    }

    @Transactional
    public void deleteById(String id){
        SysPermission bean = new SysPermission();
        bean.setId(id);
        sysPermissionRepository.delete(bean);
    }
    
}
