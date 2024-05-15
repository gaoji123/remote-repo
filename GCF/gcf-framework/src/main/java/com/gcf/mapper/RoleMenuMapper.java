package com.gcf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gcf.domain.entity.RoleMenu;
import org.apache.ibatis.annotations.Delete;


/**
 * 角色和菜单关联表(RoleMenu)表数据库访问层
 *
 * @author makejava
 * @since 2024-04-21 16:49:06
 */
public interface RoleMenuMapper extends BaseMapper<RoleMenu> {

@Delete("delete from sys_role_menu where role_id = #{id}")
    void deleteByRoleId(Long id);
}

