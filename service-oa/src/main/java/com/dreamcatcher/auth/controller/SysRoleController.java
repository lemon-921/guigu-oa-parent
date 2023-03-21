package com.dreamcatcher.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dreamcatchar.model.system.SysRole;
import com.dreamcatchar.vo.system.AssginRoleVo;
import com.dreamcatchar.vo.system.SysRoleQueryVo;
import com.dreamcatcher.auth.service.SysRoleService;
import com.dreamcatcher.common.config.exception.GuiguException;
import com.dreamcatcher.common.result.Result;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * @author zgl
 * @create 2023-02-2023/2/28-21:46
 */
@Api(tags = "角色管理接口")
@RestController
@RequestMapping("/admin/system/sysRole")
public class SysRoleController {

    //http://localhost:8800//admin/system/sysRole/findAll
    @Autowired
    SysRoleService sysRoleService;


    //1 查询所有角色和当前用户所属角色
    @ApiOperation("获取角色")
    @GetMapping("/toAssign/{userId}")
    public Result toAssign(@PathVariable Long userId) {
        Map<String,Object> map = sysRoleService.findRoleDataByUserId(userId);
        return Result.ok(map);
    }


    //为用户分配角色
    @ApiOperation(value = "根据用户分配角色")
    @PostMapping("/doAssign")
    public Result doAssign(@RequestBody AssginRoleVo assginRoleVo) {
        sysRoleService.doAssign(assginRoleVo);
        return Result.ok();
    }



    /**
     * 查询所有角色
     *
     * @return
     */
   /* @RequestMapping("findAll")
    public List<SysRole> findAll() {
        List<SysRole> roleList = sysRoleService.list();
        return roleList;
    }*/
    @ApiOperation("查询所有的角色")
    @GetMapping("findAll")
    public Result findAll() {
        List<SysRole> roleList = sysRoleService.list();

        try {
            // int i = 10 / 0;
        } catch (Exception e) {
            //抛出自定义异常类
            throw new GuiguException(20001, "执行了自定义异常处理");
        }

        return Result.ok(roleList);
    }

    /**
     * 条件分页查询
     *
     * @param page           当前页
     * @param limit          每页显示的记录数
     * @param sysRoleQueryVo 条件对象
     * @return
     */
    @ApiOperation("条件分页查询")
    @GetMapping("{page}/{limit}")
    public Result pageQueryRole(@PathVariable Long page,
                                @PathVariable Long limit,
                                SysRoleQueryVo sysRoleQueryVo) {
        //1 创建Page对象，传递分页相关参数
        Page<SysRole> pageParam = new Page<>(page, limit);
        //2 封装条件，判断条件是否为空，不为空进行封装
        LambdaQueryWrapper<SysRole> wrapper = new LambdaQueryWrapper<>();
        String roleName = sysRoleQueryVo.getRoleName();
        if (!StringUtils.isEmpty(roleName)) {
            wrapper.like(SysRole::getRoleName, roleName);
        }
        //3 调用方法实现
        IPage<SysRole> pageModel = sysRoleService.page(pageParam, wrapper);

        return Result.ok(pageModel);
    }

    //添加角色
    @ApiOperation("添加角色")
    @PostMapping("save") //加了@RequestBody 就不能使用get请求
    public Result save(@RequestBody SysRole sysRole) {
        //@Validated
        boolean is_success = sysRoleService.save(sysRole);

        if (is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //根据id查询
    @ApiOperation("根据id查询")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {

        SysRole role = sysRoleService.getById(id);
        return Result.ok(role);
    }

    //修改角色-最终修改
    @ApiOperation("修改角色")
    @PutMapping("update")
    public Result update(@RequestBody SysRole sysRole) {

        boolean is_success = sysRoleService.updateById(sysRole);

        if (is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //根据id删除
    @ApiOperation("根据id删除")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        boolean is_success = sysRoleService.removeById(id);
        if (is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //批量删除
    @ApiOperation("批量删除")
    @DeleteMapping("batchRemove")
    public Result batchRemove(@RequestBody List<Long> idList) {
        boolean is_success = sysRoleService.removeByIds(idList);
        if (is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

}
