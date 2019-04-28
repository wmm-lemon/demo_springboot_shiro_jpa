package com.wmm.shirodemo.repository;

import com.wmm.shirodemo.entity.SysPermission;

import java.util.List;

/**
 * Created by wmm on 2019/4/17.
 */
public interface SysPermissionRepository extends BaseRepository<SysPermission,Integer> {

    SysPermission findById(String id);

    SysPermission findByName(String name);

    List findAllByOrderByParentIdAscOrderNumAsc();

}
