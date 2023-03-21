package com.dreamcatcher.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.dreamcatchar.model.system.SysUser;
import com.dreamcatchar.vo.system.LoginVo;
import com.dreamcatchar.vo.system.RouterVo;
import com.dreamcatcher.auth.service.SysMenuService;
import com.dreamcatcher.auth.service.SysUserService;
import com.dreamcatcher.common.config.exception.GuiguException;
import com.dreamcatcher.common.jwt.JwtHelper;
import com.dreamcatcher.common.result.Result;
import com.dreamcatcher.common.utils.MD5;
import io.swagger.annotations.Api;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 后台登录登出
 *
 * @author zgl
 * @create 2023-03-2023/3/3-9:44
 */
@Api(tags = "后台登录管理")
@RestController
@RequestMapping("/admin/system/index")
public class IndexController {

    @Autowired
    private SysUserService sysUserService;

    @Autowired
    private SysMenuService sysMenuService;

    /**
     * 登录
     *
     * @return
     */
    @PostMapping("login")
    public Result login(@RequestBody LoginVo loginVo) {
        // {"code":20000,"data":{"token":"admin-token"}}
        // Map<String, Object> map = new HashMap<>();
        // map.put("token", "admin-token");
        // return Result.ok(map);

        //1 获取输入用户名和密码
        String username = loginVo.getUsername();
        LambdaQueryWrapper<SysUser> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(SysUser::getUsername,username);
        SysUser sysUser = sysUserService.getOne(wrapper);
        if (sysUser == null) {
            throw new GuiguException(201,"出错啦，用户不存在");
        }
        String password_db = sysUser.getPassword();

        String password_input = MD5.encrypt(loginVo.getPassword());
        if (!password_db.equals(password_input)) {
            throw new GuiguException(201,"出错啦，密码错啦");

        }
        if (sysUser.getStatus().intValue() == 0) {
            throw new GuiguException(201,"出错啦，用户已经被禁用啦");
        }
        String token = JwtHelper.createToken(sysUser.getId(), sysUser.getUsername());
        HashMap<String, Object> map = new HashMap<>();
        map.put("token",token);

        return Result.ok(map);


    }

    /**
     * 获取用户信息
     *
     * @return
     */
    @GetMapping("info")
    public Result info(HttpServletRequest request) {
        /*
        {"code":20000,"data":{"roles":["admin"],
        "introduction":"I am a super administrator",
        "avatar":"https://wpimg.wallstcn.com/f778738c-e4f8-4870-b634-56703b4acafe.gif",
        "name":"Super Admin"}}
        */
        //

        //1 从请求头获取用户信息（获取请求头token字符串）
        String token = request.getHeader("token");
        //2 从token字符串获取用户id或者用户名称
        Long userId = JwtHelper.getUserId(token);
        //3 根据用户id查询数据库，把用户信息获取出来
        SysUser sysUser = sysUserService.getById(userId);

        //4 根据用户id获取用户可以操作菜单列表
        //查询数据库动态构建路由结构，进行显示
        List<RouterVo> routerList = sysMenuService.findUserMenuListByUserId(userId);

        //5 根据用户id获取用户可以操作按钮列表
        List<String> permsList = sysMenuService.findUserPermsByUserId(userId);

        //6 返回相应的数据
        Map<String, Object> map = new HashMap<>();
        map.put("roles","[admin]");
        map.put("name",sysUser.getName());
        map.put("avatar","https://oss.aliyuncs.com/aliyun_id_photo_bucket/default_handsome.jpg");
        //返回用户可以操作菜单
        map.put("routers",routerList);
        //返回用户可以操作按钮
        map.put("buttons",permsList);
        return Result.ok(map);

    }



    /**
     * 退出
     *
     * @return
     */
    @PostMapping("logout")
    public Result logout() {
        return Result.ok();
    }

}
