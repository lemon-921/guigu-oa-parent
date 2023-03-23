package com.dreamcatcher.auth.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dreamcatchar.model.system.SysUser;
import com.dreamcatcher.auth.mapper.SysUserMapper;
import com.dreamcatcher.auth.service.SysUserService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户表 服务实现类
 * </p>
 *
 * @author zgl
 * @since 2023-03-05
 */
@Service
public class SysUserServiceImpl extends ServiceImpl<SysUserMapper, SysUser> implements SysUserService {

    @Override
    public void updateStatus(Long id, Integer status) {

        //根据id查询当前用户
        // SysUser sysUser = this.getById(id);
        SysUser sysUser = baseMapper.selectById(id);
        //设置修改状态值
        sysUser.setStatus(status);
        //修改完更新这个用户
        this.updateById(sysUser);

    }

    @Override
    public SysUser getByUsername(String username) {
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername,username);
        SysUser sysUser = baseMapper.selectOne(wrapper);
        return sysUser;
    }
}
