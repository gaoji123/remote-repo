package com.gcf.controller;

import com.gcf.domain.ResponseResult;
import com.gcf.domain.dto.AddRoleDto;
import com.gcf.domain.dto.AddRoleDto1;
import com.gcf.domain.dto.RoleDto;
import com.gcf.domain.entity.Role;
import com.gcf.domain.vo.PageVo;
import com.gcf.domain.vo.RoleDetailVo;
import com.gcf.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/role")
public class RoleController {
    @Autowired
    private RoleService roleService;
    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, RoleDto roleDto){
        return roleService.pageRoleList(pageNum,pageSize,roleDto);
    }
    @PutMapping("/changeStatus")
    public ResponseResult updateStatus(@RequestBody RoleDto roleDto){
        return roleService.updateStatus(roleDto);
    }
    @PostMapping
    public ResponseResult addRole(@RequestBody AddRoleDto addRoleDto){
        return roleService.addRole(addRoleDto);
    }
    @GetMapping("{id}")
    public ResponseResult<RoleDetailVo> listRoleDetail(@PathVariable("id") Long id){
        return roleService.listRoleDetail(id);
    }
    @PutMapping
    public ResponseResult updateRole(@RequestBody AddRoleDto1 addRoleDto1){
        return roleService.updateRole(addRoleDto1);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteRoleById(@PathVariable("id") Long id) {

        return roleService.deleteRoleById(id);
    }

    @GetMapping("/listAllRole")
    public ResponseResult listAllRole(){
        List<Role> list = roleService.listAllRole();
        return ResponseResult.okResult(list);
    }
}
