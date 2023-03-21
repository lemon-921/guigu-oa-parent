package com.dreamcatcher.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dreamcatchar.model.system.SysRole;
import com.dreamcatchar.vo.system.AssginRoleVo;

import java.util.Map;

/**
 * @author zgl
 * @create 2023-02-2023/2/28-21:40
 */
public interface SysRoleService extends IService<SysRole>  {
    //查询所有角色和当前用户所属角色
    Map<String, Object> findRoleDataByUserId(Long userId);
    //为用户分配角色
    void doAssign(AssginRoleVo assginRoleVo);
}
