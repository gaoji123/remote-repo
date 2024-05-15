package com.gcf.controller;

import com.gcf.domain.ResponseResult;
import com.gcf.domain.entity.LoginUser;
import com.gcf.domain.entity.Menu;
import com.gcf.domain.entity.User;
import com.gcf.domain.vo.AdminUserInfoVo;
import com.gcf.domain.vo.RoutersVo;
import com.gcf.domain.vo.UserInfoVo;
import com.gcf.enums.AppHttpCodeEnum;
import com.gcf.exception.SystemException;
import com.gcf.service.LoginService;
import com.gcf.service.MenuService;
import com.gcf.service.RoleService;
import com.gcf.utils.BeanCopyUtils;
import com.gcf.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LoginController {
        @Autowired
    private LoginService loginService;
    @Autowired
    private MenuService menuService;
    @Autowired
    private RoleService roleService;


    @PostMapping("/user/login")
    public ResponseResult login(@RequestBody User user){
        if(!StringUtils.hasText(user.getUserName()))
        {
            throw new SystemException(AppHttpCodeEnum.REQUIRE_USERNAME);
        }
        return loginService.login(user);
    }




    @GetMapping("/getInfo")
    public ResponseResult<AdminUserInfoVo> getInfo()
    {
        LoginUser loginUser = SecurityUtils.getLoginUser();

        List<String> perms =  menuService.selectPermsByUserId(loginUser.getUser().getId());

        List<String> roleKeyList = roleService.selectRoleKeyByUserId(loginUser.getUser().getId());

        User user = loginUser.getUser();
        UserInfoVo userInfoVo = BeanCopyUtils.copyBean(user, UserInfoVo.class);

        AdminUserInfoVo adminUserInfoVo = new AdminUserInfoVo(perms,roleKeyList,userInfoVo);

        return ResponseResult.okResult(adminUserInfoVo);
    }
    @GetMapping("/getRouters")
    public ResponseResult<RoutersVo> getRouters(){
        Long userId = SecurityUtils.getUserId();
        //查询menu 结果是tree的形式
        List<Menu> menus = menuService.selectRouterMenuTreeByUserId(userId);
        //封装数据返回
        return ResponseResult.okResult(new RoutersVo(menus));
    }
    @PostMapping("/user/logout")
    public ResponseResult logout(){
       return loginService.logout();
    }

}