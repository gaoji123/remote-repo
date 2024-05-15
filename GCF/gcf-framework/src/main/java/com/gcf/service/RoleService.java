package com.gcf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gcf.domain.ResponseResult;
import com.gcf.domain.dto.AddRoleDto;
import com.gcf.domain.dto.AddRoleDto1;
import com.gcf.domain.dto.RoleDto;
import com.gcf.domain.entity.Role;
import com.gcf.domain.vo.PageVo;
import com.gcf.domain.vo.RoleDetailVo;

import java.util.List;


/**
 * 角色信息表(Role)表服务接口
 *
 * @author makejava
 * @since 2024-04-20 09:43:39
 */
public interface RoleService extends IService<Role> {

    List<String> selectRoleKeyByUserId(Long id);

    ResponseResult<PageVo> pageRoleList(Integer pageNum, Integer pageSize, RoleDto roleDto);

    ResponseResult updateStatus(RoleDto roleDto);


    ResponseResult addRole(AddRoleDto addRoleDto);

    ResponseResult<RoleDetailVo> listRoleDetail(Long id);

    ResponseResult updateRole(AddRoleDto1 addRoleDto1);

    ResponseResult deleteRoleById(Long id);

    List<Role> listAllRole();

}
