package com.dreamcatcher.auth.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.dreamcatchar.model.system.SysUser;

/**
 * <p>
 * 用户表 服务类
 * </p>
 *
 * @author zgl
 * @since 2023-03-05
 */
public interface SysUserService extends IService<SysUser> {

    //修改用户状态
    void updateStatus(Long id, Integer status);
}
