package com.gcf.controller;

import com.gcf.domain.ResponseResult;
import com.gcf.domain.dto.AddUserDto;
import com.gcf.domain.dto.UpdateUserDto;
import com.gcf.domain.dto.UserDto;
import com.gcf.domain.vo.PageVo;
import com.gcf.domain.vo.PageVo1;
import com.gcf.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("system/user")
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/list")
    public ResponseResult<PageVo> pageUserList(Integer pageNum, Integer pageSize, UserDto userDto){
        return userService.pageUserList(pageNum,pageSize,userDto);
    }
    @PostMapping
    public ResponseResult addUser(@RequestBody AddUserDto addUserDto){
        return userService.addUser(addUserDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteUserById(@PathVariable("id") Long id) {

        return userService.deleteUserById(id);
    }
    @GetMapping("{id}")
    public ResponseResult<PageVo1> listPageVo1(@PathVariable("id") Long id){
        return userService.listPageVo1(id);
    }
    @PutMapping
    public ResponseResult updateUser(@RequestBody UpdateUserDto updateUserDto){
        return userService.updateUser(updateUserDto);
    }
}
