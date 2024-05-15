package com.gcf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gcf.domain.entity.Menu;

import java.util.List;


/**
 * 菜单权限表(Menu)表数据库访问层
 *
 * @author makejava
 * @since 2024-04-20 09:36:37
 */
public interface MenuMapper extends BaseMapper<Menu> {


    List<Menu> selectAllRouterMenu();

    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    List<String> selectPermsByUserId(Long username);
}

