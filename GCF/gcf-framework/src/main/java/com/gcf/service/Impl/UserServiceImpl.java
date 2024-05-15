package com.gcf.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gcf.domain.ResponseResult;
import com.gcf.domain.dto.AddUserDto;
import com.gcf.domain.dto.UpdateUserDto;
import com.gcf.domain.dto.UserDto;
import com.gcf.domain.entity.Role;
import com.gcf.domain.entity.User;
import com.gcf.domain.entity.UserRole;
import com.gcf.domain.vo.*;
import com.gcf.enums.AppHttpCodeEnum;
import com.gcf.exception.SystemException;
import com.gcf.mapper.RoleMapper;
import com.gcf.mapper.UserMapper;
import com.gcf.mapper.UserRoleMapper;
import com.gcf.service.UserService;
import com.gcf.utils.BeanCopyUtils;
import com.gcf.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户表(User)表服务实现类
 *
 * @author makejava
 * @since 2024-04-18 20:42:33
 */
@Service("userService")
    public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired
        private RoleMapper roleMapper;

    @Override
            public ResponseResult userInfo() {
        //获取当前用户id
        Long userId = SecurityUtils.getUserId();
        //根据用户id查询用户信息
        User user = getById(userId);
        //封装成UserInfoVo
        UserInfoVo vo = BeanCopyUtils.copyBean(user, UserInfoVo.class);
        return ResponseResult.okResult(vo);
    }

    @Override
    public ResponseResult updateUserInfo(User user) {
        updateById(user);
        return ResponseResult.okResult();
    }

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public ResponseResult register(User user) {
        //对数据进行非空判断
        if (!StringUtils.hasText(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getPassword())) {
            throw new SystemException(AppHttpCodeEnum.PASSWORD_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_NOT_NULL);
        }
        if (!StringUtils.hasText(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_NOT_NULL);
        }
        //对数据进行是否存在的判断
        if (userNameExist(user.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (nickNameExist(user.getNickName())) {
            throw new SystemException(AppHttpCodeEnum.NICKNAME_EXIST);
        }
        if (emailExist(user.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        //...
        //对密码进行加密
        String encodePassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encodePassword);
        //存入数据库
        save(user);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<PageVo> pageUserList(Integer pageNum, Integer pageSize, UserDto userDto) {
        LambdaQueryWrapper<User> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(userDto.getUserName()), User::getUserName, userDto.getUserName());
        wrapper.eq(StringUtils.hasText(userDto.getPhonenumber()), User::getPhonenumber, userDto.getPhonenumber());
        wrapper.eq(StringUtils.hasText(userDto.getStatus()), User::getStatus, userDto.getStatus());
        Page<User> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, wrapper);
        List<UserListVo> userListVos = BeanCopyUtils.copyBeanList(page.getRecords(), UserListVo.class);
        PageVo pageVo = new PageVo(userListVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addUser(AddUserDto addUserDto) {
        if (!StringUtils.hasText(addUserDto.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_NOT_NULL);
        }
        if (userNameExist(addUserDto.getUserName())) {
            throw new SystemException(AppHttpCodeEnum.USERNAME_EXIST);
        }
        if (emailExist(addUserDto.getEmail())) {
            throw new SystemException(AppHttpCodeEnum.EMAIL_EXIST);
        }
        if (phonenumberExist(addUserDto.getPhonenumber())) {
            throw new SystemException(AppHttpCodeEnum.PHONENUMBER_EXIST);
        }
        String encodePassword = passwordEncoder.encode(addUserDto.getPassword());
        addUserDto.setPassword(encodePassword);
        User user = BeanCopyUtils.copyBean(addUserDto, User.class);
        save(user);
        User user1 = userMapper.selectByUserName(addUserDto.getUserName());
        Long id = user1.getId();
        List<String> roleIds = addUserDto.getRoleIds();
        List<UserRole> collect = roleIds.stream().map(s -> new UserRole(id, Long.valueOf(s).longValue())).collect(Collectors.toList());
        for (UserRole userRole : collect) {
            userRoleMapper.insert(userRole);
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteUserById(Long id) {
        if (SecurityUtils.getUserId().equals(id)) {
            throw new SystemException(AppHttpCodeEnum.USER_USING);
        }
        userMapper.deleteById(id);
        userRoleMapper.deleteByUserId(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<PageVo1> listPageVo1(Long id) {
        List<UserRole> userRoles = userRoleMapper.selectByUserId(id);
        List<Long> longs = userRoles.stream().map(userRole -> userRole.getRoleId()).collect(Collectors.toList());
        List<Role> roles = new ArrayList<>();
        for (Long roleId : longs) {
            Role role = roleMapper.selectById(roleId);
            roles.add(role);
        }
        User user = userMapper.selectById(id);
        UserSelfVo userSelfVo = BeanCopyUtils.copyBean(user, UserSelfVo.class);
        List<String> collect = longs.stream().map(aLong -> aLong.toString()).collect(Collectors.toList());
        PageVo1 pageVo1 = new PageVo1(collect,roles,userSelfVo);
        return ResponseResult.okResult(pageVo1);
    }

    @Override
    public ResponseResult updateUser(UpdateUserDto updateUserDto) {
        User user = BeanCopyUtils.copyBean(updateUserDto, User.class);
        userMapper.updateById(user);
        List<String> roleIds = updateUserDto.getRoleIds();
        userRoleMapper.deleteByUserId(updateUserDto.getId());
        List<UserRole> collect = roleIds.stream().map(s -> new UserRole(updateUserDto.getId(), Long.valueOf(s).longValue())).collect(Collectors.toList());
        for(UserRole userRole:collect)
        {
            userRoleMapper.insert(userRole);
        }
        return ResponseResult.okResult();
    }

    private boolean phonenumberExist(String phonenumber) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getPhonenumber, phonenumber);
        return count(queryWrapper) > 0;
    }

    private boolean emailExist(String email) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getEmail, email);
        return count(queryWrapper) > 0;

    }

    private boolean nickNameExist(String nickName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getNickName, nickName);
        return count(queryWrapper) > 0;
    }

    private boolean userNameExist(String userName) {
        LambdaQueryWrapper<User> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(User::getUserName, userName);
        return count(queryWrapper) > 0;
    }

}
