package com.gcf.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 角色和菜单关联表(RoleMenu)表实体类
 *
 * @author makejava
 * @since 2024-04-21 16:49:08
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("sys_role_menu")
public class RoleMenu  {
    //角色ID
    private Long roleId;
    //菜单ID@TableId
    private Long menuId;




}
