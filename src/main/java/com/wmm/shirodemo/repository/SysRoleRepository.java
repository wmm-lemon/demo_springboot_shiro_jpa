package com.wmm.shirodemo.repository;

import com.wmm.shirodemo.entity.SysRole;

import java.util.List;

/**
 * Created by wmm on 2019/4/17.
 */
public interface SysRoleRepository extends BaseRepository<SysRole,Integer>{

    SysRole findById(String id);

    SysRole findByRole(String role);

    List findAllByOrderByCreateTimeDesc();
}
