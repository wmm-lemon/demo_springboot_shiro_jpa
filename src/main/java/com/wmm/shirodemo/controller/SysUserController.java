package com.wmm.shirodemo.controller;

import com.wmm.shirodemo.entity.SysUser;
import com.wmm.shirodemo.service.SysRoleService;
import com.wmm.shirodemo.service.SysUserService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by wmm on 2019/4/12.
 */
@Controller
@RequestMapping("sysuser")
public class SysUserController {

    @Autowired
    SysUserService sysUserService;
    @Autowired
    SysRoleService sysRoleService;

    @RequestMapping("list")
    @RequiresPermissions("sysuser:view")//权限管理;
    public String list(HashMap map,String nameParam,
                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize){
        try {
            map.put("datas",sysUserService.findList(nameParam,page-1,pageSize));
            map.put("nameParam",nameParam);
            map.put("roleList",sysRoleService.findAll());
            map.put("code",1);
            map.put("msg","成功");
        }catch (Exception e){
            map.put("code",0);
            map.put("msg","失败");
            e.printStackTrace();
        }
        return "sys/user_list";
    }

    @RequestMapping("toEdit")
    @ResponseBody
    public Map toEdit(String id){
        Map map = new HashMap();
        try {
            map.put("bean",sysUserService.findById(id));
            map.put("code",1);
            map.put("msg","成功");
        }catch (Exception e){
            map.put("code",0);
            map.put("msg","失败");
            e.printStackTrace();
        }
        return map;
    }

    @RequestMapping("edit")
    @ResponseBody
    @RequiresPermissions("sysuser:edit")//权限管理;
    public Map editMenu(SysUser bean,String roleIdString){
        Map map = new HashMap();
        try {
            return sysUserService.edit(bean,roleIdString);
        }catch (Exception e){
            map.put("code",0);
            map.put("msg","失败");
            e.printStackTrace();
            return map;
        }
    }

    @RequestMapping("deleteById")
    @ResponseBody
    @RequiresPermissions("sysuser:delete")//权限管理;
    public Map deleteById(String id){
        Map map = new HashMap();
        try {
            sysUserService.deleteById(id);
            map.put("code",1);
            map.put("msg","成功");
        }catch (Exception e){
            map.put("code",0);
            map.put("msg","失败");
            e.printStackTrace();
        }
        return map;
    }
}
