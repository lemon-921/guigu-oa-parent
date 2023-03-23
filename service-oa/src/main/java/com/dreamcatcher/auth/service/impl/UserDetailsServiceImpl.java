package com.dreamcatcher.auth.service.impl;

import com.dreamcatchar.model.system.SysUser;
import com.dreamcatcher.auth.service.SysMenuService;
import com.dreamcatcher.auth.service.SysUserService;
import com.dreamcatcher.security.custom.CustomUser;
import com.dreamcatcher.security.custom.UserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author zgl
 * @create 2023-03-2023/3/22-14:43
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private SysUserService sysUserService;
    @Autowired
    private SysMenuService sysMenuService;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        SysUser sysUser=sysUserService.getByUsername(username);
        if(null == sysUser) {
            throw new UsernameNotFoundException("用户名不存在！");
        }

        if(sysUser.getStatus().intValue() == 0) {
            throw new RuntimeException("账号已停用");
        }

        //根据userid查询用户操作权限数据
        List<String> userPermsList = sysMenuService.findUserPermsByUserId(sysUser.getId());
        //创建list集合，封装最终权限数据
        List<SimpleGrantedAuthority> authList =new ArrayList<>();
        //查询list集合遍历
        for (String perm : userPermsList) {
            authList.add(new SimpleGrantedAuthority(perm.trim()));
        }

        return new CustomUser(sysUser, authList);
    }
}
