package com.dreamcatcher.auth.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.dreamcatchar.model.system.SysUser;
import com.dreamcatchar.vo.system.SysUserQueryVo;
import com.dreamcatcher.auth.service.SysUserService;
import com.dreamcatcher.common.result.Result;
import com.dreamcatcher.common.utils.MD5;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

/**
 * <p>
 * 用户表 前端控制器
 * </p>
 *
 * @author zgl
 * @since 2023-03-05
 */
@Api(tags = "用户管理接口")
@RestController
@RequestMapping("/admin/system/sysUser")
public class SysUserController {

    @Autowired
    private SysUserService sysUserService;

    @ApiOperation(value = "更新状态")
    @GetMapping("updateStatus/{id}/{status}")
    public Result updateStatus(@PathVariable Long id, @PathVariable Integer status) {
        sysUserService.updateStatus(id,status);
        return Result.ok();
    }


    //用户条件分页查询
    @ApiOperation("用户条件分页查询")
    @GetMapping("{page}/{limit}")
    public Result index(@PathVariable Long page,
                        @PathVariable Long limit,
                         SysUserQueryVo sysUserQueryVo) {

        //创建page对象
        Page<SysUser> pageParam = new Page<>(page,limit);
        //封装条件，判断条件值不为空
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        //获取条件值 keyword对应着user表中的username
                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            String username = sysUserQueryVo.getKeyword();
        String createTimeBegin = sysUserQueryVo.getCreateTimeBegin();
        String createTimeEnd = sysUserQueryVo.getCreateTimeEnd();
        //判断条件值不为空
        if (!StringUtils.isEmpty(username)) {
            wrapper.like(SysUser::getUsername, username);
        }
        //ge 大于等于
        if (!StringUtils.isEmpty(createTimeBegin)) {
            wrapper.ge(SysUser::getCreateTime, createTimeBegin);
        }
        //le 小于等于
        if (!StringUtils.isEmpty(createTimeEnd)) {
            wrapper.like(SysUser::getCreateTime, createTimeEnd);
        }
        // 调用mp的方法实现条件分页查询
        IPage<SysUser> pageModel = sysUserService.page(pageParam, wrapper);

        return Result.ok(pageModel);
    }

    //获取用户
    @ApiOperation(value = "获取用户")
    @GetMapping("get/{id}")
    public Result get(@PathVariable Long id) {
        SysUser user = sysUserService.getById(id);
        return Result.ok(user);
    }

    //添加用户
    @ApiOperation(value = "添加用户")
    @PostMapping("save")
    public Result save(@RequestBody SysUser sysUser) {
        //密码进行加密，使用MD5加密
        String password = sysUser.getPassword();
        String passwordMD5 = MD5.encrypt(password);
        sysUser.setPassword(passwordMD5);

        boolean is_success = sysUserService.save(sysUser);
        if (is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

    //修改用户
    @ApiOperation(value = "修改用户")
    @PutMapping("update")
    public Result update(@RequestBody SysUser sysUser) {
        boolean is_success = sysUserService.updateById(sysUser);
        if (is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }


    //删除用户
    @ApiOperation(value = "删除用户")
    @DeleteMapping("remove/{id}")
    public Result remove(@PathVariable Long id) {
        boolean is_success = sysUserService.removeById(id);
        if (is_success) {
            return Result.ok();
        } else {
            return Result.fail();
        }
    }

}

