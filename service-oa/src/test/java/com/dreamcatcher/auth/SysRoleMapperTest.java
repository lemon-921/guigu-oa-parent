package com.dreamcatcher.auth;


import com.dreamcatchar.model.system.SysRole;
import com.dreamcatcher.auth.mapper.SysRoleMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

/**
 * @author zgl
 * @create 2023-02-2023/2/28-19:24
 */
@SpringBootTest
public class SysRoleMapperTest {

    @Autowired
    private SysRoleMapper sysRoleMapper;

    /**
     * 查询所有记录
     */
    @Test
    public void getAll() {
        //所以不填写就是无任何条件
        List<SysRole> users = sysRoleMapper.selectList(null);
        users.forEach(System.out::println);
    }

    @Test
    public void add() {
        SysRole role=new SysRole();
        role.setRoleName("xiaozhang");
        role.setRoleCode("SYSTEM");
        sysRoleMapper.insert(role);
    }
}
