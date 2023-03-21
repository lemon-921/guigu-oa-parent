package com.dreamcatcher.auth.utils;

import com.dreamcatchar.model.system.SysMenu;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zgl
 * @create 2023-03-2023/3/7-21:59
 */
public class MenuHelper {


    public static List<SysMenu> buildTree(List<SysMenu> sysMenuList) {
        //创建list集合,最终的返回数据
        List<SysMenu> trees=new ArrayList<>();

        //遍历sysMenuList这个集合
        for (SysMenu sysMenu : sysMenuList) {
            //parentId==0是递归入口
            if (sysMenu.getParentId().longValue() == 0) {
                //sysMenu 遍历的一个数据          sysMenuList 总菜单数据
                trees.add(getChildren(sysMenu,sysMenuList));
            }
        }

        return trees;
    }

    /**
     * 遍历菜单
     * @param sysMenu
     * @param sysMenuList
     * @return
     */
    private static SysMenu getChildren(SysMenu sysMenu,
                                       List<SysMenu> sysMenuList) {
        sysMenu.setChildren(new ArrayList<SysMenu>());
        for (SysMenu it : sysMenuList) {
            //当前的id与遍历的父id作比对
            //遍历所有菜单数据，判断id和parentid对应关系
            if (sysMenu.getId().longValue() == it.getParentId().longValue()) {
                //如果为空，set一个集合
                if (sysMenu.getChildren() == null) {
                    sysMenu.setChildren(new ArrayList<>());
                }
                sysMenu.getChildren().add(getChildren(it,sysMenuList));
            }
        }
        return sysMenu;
    }
}
