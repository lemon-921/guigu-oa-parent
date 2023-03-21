package com.dreamcatcher.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.dreamcatchar.model.system.SysRole;
import com.dreamcatchar.model.system.SysUserRole;
import com.dreamcatchar.vo.system.AssginRoleVo;
import com.dreamcatcher.auth.mapper.SysRoleMapper;
import com.dreamcatcher.auth.service.SysRoleService;
import com.dreamcatcher.auth.service.SysUserRoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zgl
 * @create 2023-02-2023/2/28-21:40
 */
@Service
public class SysRoleServiceImpl extends ServiceImpl<SysRoleMapper, SysRole> implements SysRoleService {

    @Autowired
    private SysUserRoleService sysUserRoleService;

    @Override
    public Map<String, Object> findRoleDataByUserId(Long userId) {
        //SysRoleMapper 已经注入了，baseMapper
        //传入null 表示查询所有


        //1 查询所有角色，返回list集合，返回  (这是查询表中所有角色的数据)
        List<SysRole> allRoleList =
                baseMapper.selectList(null);

        //2 根据userid查询 角色用户关系表，查询userid对应所有角色id (这是根据传过来的userid值，看一看这个userId的人对应有几个角色)
        LambdaQueryWrapper<SysUserRole> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId, userId);
        List<SysUserRole> existUserRoleList = sysUserRoleService.list(wrapper);

        //从查询出来的用户id对应角色list集合，获取所有角色id (这是查询数据库中这个角色对应的id值)
        // List<Long> list = new ArrayList<>();
        // for (SysUserRole sysUserRole:existUserRoleList) {
        //     Long roleId = sysUserRole.getRoleId();
        //     list.add(roleId);
        // }
        List<Long> existRoleIdList =
                existUserRoleList.stream().map(c -> c.getRoleId()).collect(Collectors.toList());

        //3 根据查询所有角色id，找到对应角色信息
        //根据角色id到所有的角色的list集合进行比较  (这里是查询的角色和数据库中角色对做对比，
        // 如果对应的上在这个用户下显示该角色，也就是默认勾选了复选框)
        List<SysRole> assignRoleList = new ArrayList<>();
        for (SysRole sysRole : allRoleList) {
            //比较
            if (existRoleIdList.contains(sysRole.getId())) {
                assignRoleList.add(sysRole);
            }
        }

        //4 把得到两部分数据封装map集合，返回
        Map<String, Object> roleMap = new HashMap<>();
        roleMap.put("assginRoleList", assignRoleList);
        roleMap.put("allRolesList", allRoleList);
        return roleMap;

    }

    //分配
    @Override
    public void doAssign(AssginRoleVo assginRoleVo) {
        //把用户之前分配角色数据删除，用户角色关系表里面，根据userid删除
        LambdaQueryWrapper<SysUserRole> wrapper=new LambdaQueryWrapper<>();
        wrapper.eq(SysUserRole::getUserId,assginRoleVo.getUserId());
        sysUserRoleService.remove(wrapper);

        //重新分配 (这里面放的是一些用户选择的角色，)
        List<Long> roleIdList = assginRoleVo.getRoleIdList();
        for (Long roleId : roleIdList) {
            //判断用户是否为空
            if (StringUtils.isEmpty(roleId)) {
                continue;
            }
            SysUserRole sysUserRole = new SysUserRole();
            sysUserRole.setUserId(assginRoleVo.getUserId());
            sysUserRole.setRoleId(roleId);
            //这个角色保存起来
            sysUserRoleService.save(sysUserRole);
        }


    }















}
