package com.gcf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gcf.domain.ResponseResult;
import com.gcf.domain.dto.AddUserDto;
import com.gcf.domain.dto.UpdateUserDto;
import com.gcf.domain.dto.UserDto;
import com.gcf.domain.entity.User;
import com.gcf.domain.vo.PageVo;
import com.gcf.domain.vo.PageVo1;


/**
 * 用户表(User)表服务接口
 *
 * @author makejava
 * @since 2024-04-18 20:42:32
 */
public interface UserService extends IService<User> {

    ResponseResult userInfo();

    ResponseResult updateUserInfo(User user);

    ResponseResult register(User user);

    ResponseResult<PageVo> pageUserList(Integer pageNum, Integer pageSize, UserDto userDto);

    ResponseResult addUser(AddUserDto addUserDto);

    ResponseResult deleteUserById(Long id);

    ResponseResult<PageVo1> listPageVo1(Long id);

    ResponseResult updateUser(UpdateUserDto updateUserDto);
}
