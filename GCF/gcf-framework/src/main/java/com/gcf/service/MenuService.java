package com.gcf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gcf.domain.ResponseResult;
import com.gcf.domain.dto.AddMenuDto;
import com.gcf.domain.dto.MenuDto;
import com.gcf.domain.entity.Menu;
import com.gcf.domain.vo.MenuListVo;
import com.gcf.domain.vo.MenuTreeVo;
import com.gcf.domain.vo.MenuVo;
import com.gcf.domain.vo.TreePageVo;

import java.util.List;

public interface MenuService extends IService<Menu> {

    List<String> selectPermsByUserId(Long id);

    List<Menu> selectRouterMenuTreeByUserId(Long userId);

    List<MenuListVo> listAllMenu(MenuDto menuDto);

    ResponseResult addMenu(AddMenuDto addMenuDto);

    ResponseResult updateMenu(MenuVo menuVo);

    ResponseResult<MenuVo> listMenuDetail(Long id);

    ResponseResult deleteById(Long menuId);

    List<MenuTreeVo> selectTree();

    ResponseResult<TreePageVo> roleMenuTreeselect(Long id);
}