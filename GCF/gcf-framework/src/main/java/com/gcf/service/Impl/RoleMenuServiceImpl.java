package com.gcf.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gcf.domain.entity.RoleMenu;
import com.gcf.mapper.RoleMenuMapper;
import com.gcf.service.RoleMenuService;
import org.springframework.stereotype.Service;

/**
 * 角色和菜单关联表(RoleMenu)表服务实现类
 *
 * @author makejava
 * @since 2024-04-21 16:49:08
 */
@Service("roleMenuService")
public class RoleMenuServiceImpl extends ServiceImpl<RoleMenuMapper, RoleMenu> implements RoleMenuService {

}
