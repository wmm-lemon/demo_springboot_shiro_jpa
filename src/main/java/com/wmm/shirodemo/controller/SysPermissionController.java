package com.wmm.shirodemo.controller;

import com.wmm.shirodemo.entity.SysPermission;
import com.wmm.shirodemo.service.SysPermissionService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("syspermission")
public class SysPermissionController {

    @Autowired
    SysPermissionService sysPermissionService;

    @RequestMapping("list")
    @RequiresPermissions("syspermission:view")//权限管理;
    public String list(HashMap map,String nameParam,
                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize){
        try {
            map.put("datas",sysPermissionService.findList(nameParam,page-1,pageSize));
            map.put("nameParam",nameParam);
            map.put("permissionList",sysPermissionService.findAll());
            map.put("code",1);
            map.put("msg","成功");
        }catch (Exception e){
            map.put("code",0);
            map.put("msg","失败");
            e.printStackTrace();
        }
        return "sys/permission_list";
    }

    @RequestMapping("toEdit")
    @ResponseBody
    public Map<String,Object> toEdit(String id){
        Map<String,Object> map = new HashMap();
        try {
            map.put("bean",sysPermissionService.findById(id));
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
    @RequiresPermissions("syspermission:edit")//权限管理;
    public Map editMenu(SysPermission bean){
        Map map = new HashMap();
        try {
            return sysPermissionService.edit(bean);
        }catch (Exception e){
            map.put("code",0);
            map.put("msg","失败");
            e.printStackTrace();
            return map;
        }
    }

    @RequestMapping("deleteById")
    @ResponseBody
    @RequiresPermissions("syspermission:delete")//权限管理;
    public Map deleteById(String id){
        Map map = new HashMap();
        try {
            sysPermissionService.deleteById(id);
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
