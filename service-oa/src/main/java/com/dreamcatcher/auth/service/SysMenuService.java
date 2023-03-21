package com.dreamcatcher.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.dreamcatchar.model.system.SysMenu;
import com.dreamcatchar.vo.system.AssginMenuVo;
import com.dreamcatchar.vo.system.RouterVo;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * 菜单表 服务类
 * </p>
 *
 * @author zgl
 * @since 2023-03-07
 */
public interface SysMenuService extends IService<SysMenu> {
    //获取菜单
    List<SysMenu> findNodes();
    // 删除菜单
    void removeMenuById(Long id);
    //查询所有菜单和角色分配的菜单
    List<SysMenu> findSysMenuByRoleId(Long roleId);
    //给角色分配权限
    void doAssign(AssginMenuVo assignMenuVo);
    // 根据用户id获取用户可以操作菜单列表
    //查询数据库动态构建路由结构，进行显示
    List<RouterVo> findUserMenuListByUserId(Long userId);
    //根据用户id获取用户可以操作按钮列表
    List<String> findUserPermsByUserId(Long userId);
}
