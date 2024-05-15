package com.gcf.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gcf.constants.SystemConstants;
import com.gcf.domain.ResponseResult;
import com.gcf.domain.dto.AddRoleDto;
import com.gcf.domain.dto.AddRoleDto1;
import com.gcf.domain.dto.RoleDto;
import com.gcf.domain.entity.Role;
import com.gcf.domain.entity.RoleMenu;
import com.gcf.domain.vo.PageVo;
import com.gcf.domain.vo.RoleAllListVo;
import com.gcf.domain.vo.RoleDetailVo;
import com.gcf.domain.vo.RoleVo;
import com.gcf.mapper.RoleMapper;
import com.gcf.mapper.RoleMenuMapper;
import com.gcf.service.RoleService;
import com.gcf.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 角色信息表(Role)表服务实现类
 *
 * @author makejava
 * @since 2024-04-20 09:43:39
 */
@Service("roleService")
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    private RoleMapper roleMapper;
    @Autowired
    private RoleMenuMapper roleMenuMapper;


    @Override
    public List<String> selectRoleKeyByUserId(Long id) {
        if (id == 1) {
            List<String> roleKeys = new ArrayList<>();
            roleKeys.add("admin");
            return roleKeys;
        }
        return getBaseMapper().selectRoleKeyByUserId(id);
    }

    @Override
    public ResponseResult<PageVo> pageRoleList(Integer pageNum, Integer pageSize, RoleDto roleDto) {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(roleDto.getRoleName()), Role::getRoleName, roleDto.getRoleName());
        wrapper.eq(StringUtils.hasText(roleDto.getStatus()), Role::getStatus, roleDto.getStatus());
        wrapper.orderByDesc(Role::getRoleSort);
        Page<Role> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, wrapper);
        List<RoleVo> roleVos = BeanCopyUtils.copyBeanList(page.getRecords(), RoleVo.class);
        PageVo pageVo = new PageVo(roleVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult updateStatus(RoleDto roleDto) {
        Role role = roleMapper.selectById(roleDto.getRoleId());
        if (roleDto.getStatus().equals("1")) {
            role.setStatus("1");
        } else {
            role.setStatus("0");
        }
        roleMapper.updateById(role);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult addRole(AddRoleDto addRoleDto) {
        Role role = BeanCopyUtils.copyBean(addRoleDto, Role.class);
        roleMapper.insert(role);
        List<String> menuIds = addRoleDto.getMenuIds();
        for(String menu:menuIds)
        {
            RoleMenu roleMenu = new RoleMenu(role.getId(), Long.valueOf(menu).longValue());
            roleMenuMapper.insert(roleMenu);
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<RoleDetailVo> listRoleDetail(Long id) {
        Role role = roleMapper.selectById(id);
        RoleDetailVo roleDetailVo = BeanCopyUtils.copyBean(role, RoleDetailVo.class);
        return ResponseResult.okResult(roleDetailVo);
    }

    @Override
    public ResponseResult updateRole(AddRoleDto1 addRoleDto1) {
        Role role = BeanCopyUtils.copyBean(addRoleDto1, Role.class);
        roleMapper.updateById(role);
        roleMenuMapper.deleteByRoleId(role.getId());
        List<String> menuIds = addRoleDto1.getMenuIds();
        List<RoleMenu> collect = menuIds.stream().map(s -> new RoleMenu(role.getId(), Long.valueOf(s).longValue())).collect(Collectors.toList());
        for(RoleMenu roleMenu:collect){
            roleMenuMapper.insert(roleMenu);
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteRoleById(Long id) {
        roleMenuMapper.deleteByRoleId(id);
        roleMapper.deleteById(id);
        return ResponseResult.okResult();
    }

    @Override
    public List<Role> listAllRole() {
        LambdaQueryWrapper<Role> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Role::getStatus, SystemConstants.NORMAL);
        List<Role> roles = roleMapper.selectList(wrapper);

        return roles;
    }
}
