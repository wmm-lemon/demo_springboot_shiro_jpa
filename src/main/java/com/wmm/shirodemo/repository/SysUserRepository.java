package com.wmm.shirodemo.repository;

import com.wmm.shirodemo.entity.SysUser;

/**
 * Created by wmm on 2019/4/12.
 */
public interface SysUserRepository extends BaseRepository<SysUser,Integer> {

    SysUser findByUsernameAndPassword(String username,String password);

    SysUser findByUserId(String userId);

    SysUser findByUsername(String username);
}
