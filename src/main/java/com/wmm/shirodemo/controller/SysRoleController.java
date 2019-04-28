package com.wmm.shirodemo.controller;

import com.wmm.shirodemo.entity.SysRole;
import com.wmm.shirodemo.service.SysPermissionService;
import com.wmm.shirodemo.service.SysRoleService;
import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("sysrole")
public class SysRoleController {

    @Autowired
    SysRoleService sysRoleService;
    @Autowired
    SysPermissionService sysPermissionService;

    @RequestMapping("list")
    @RequiresPermissions("sysrole:view")//权限管理;
    public String list(HashMap map,String nameParam,
                       @RequestParam(value = "page", defaultValue = "1") Integer page,
                       @RequestParam(value = "pageSize", defaultValue = "10") Integer pageSize){
        try {
            map.put("datas",sysRoleService.findList(nameParam,page-1,pageSize));
            map.put("nameParam",nameParam);
            map.put("permissionList",sysPermissionService.findAll());
            map.put("code",1);
            map.put("msg","成功");
        }catch (Exception e){
            map.put("code",0);
            map.put("msg","失败");
            e.printStackTrace();
        }
        return "sys/role_list";
    }

    @RequestMapping("toEdit")
    @ResponseBody
    public Map toEdit(String id){
        Map map = new HashMap();
        try {
            SysRole role = sysRoleService.findById(id);
            map.put("bean",role);
            map.put("permissions",role.getPermissions());
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
    @RequiresPermissions("sysrole:edit")//权限管理;
    public Map editMenu(SysRole bean,String permissionString){
        Map map = new HashMap();
        try {
            return sysRoleService.edit(bean,permissionString);
        }catch (Exception e){
            map.put("code",0);
            map.put("msg","失败");
            e.printStackTrace();
            return map;
        }
    }

    @RequestMapping("deleteById")
    @ResponseBody
    @RequiresPermissions("sysrole:delete")//权限管理;
    public Map deleteById(String id){
        Map map = new HashMap();
        try {
            //查询角色下是否存在用户
            if(sysRoleService.findRoleUserById(id)){
                map.put("code",0);
                map.put("msg","该角色下存在用户，不能删除");
                return map;
            }
            sysRoleService.deleteById(id);
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
