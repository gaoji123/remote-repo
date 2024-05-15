package com.gcf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gcf.domain.entity.Role;

import java.util.List;


/**
 * 角色信息表(Role)表数据库访问层
 *
 * @author makejava
 * @since 2024-04-20 09:43:38
 */
public interface RoleMapper extends BaseMapper<Role> {

    List<String> selectRoleKeyByUserId(Long userId);

}

