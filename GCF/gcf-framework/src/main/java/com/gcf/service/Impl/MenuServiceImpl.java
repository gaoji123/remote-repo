package com.gcf.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gcf.constants.SystemConstants;
import com.gcf.domain.ResponseResult;
import com.gcf.domain.dto.AddMenuDto;
import com.gcf.domain.dto.MenuDto;
import com.gcf.domain.entity.Menu;
import com.gcf.domain.entity.RoleMenu;
import com.gcf.domain.entity.UserRole;
import com.gcf.domain.vo.MenuListVo;
import com.gcf.domain.vo.MenuTreeVo;
import com.gcf.domain.vo.MenuVo;
import com.gcf.domain.vo.TreePageVo;
import com.gcf.enums.AppHttpCodeEnum;
import com.gcf.mapper.MenuMapper;
import com.gcf.mapper.RoleMenuMapper;
import com.gcf.mapper.UserRoleMapper;
import com.gcf.service.MenuService;
import com.gcf.utils.BeanCopyUtils;
import com.gcf.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 菜单权限表(Menu)表服务实现类
 *
 * @author makejava
 * @since 2024-04-20 09:36:40
 */
@Service("menuService")
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private RoleMenuMapper roleMenuMapper;
    @Autowired
    private UserRoleMapper userRoleMapper;
    @Autowired MenuMapper menuMapper;
    @Override
    public List<String> selectPermsByUserId(Long id) {
        if (SecurityUtils.isAdmin()) {
            LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
            wrapper.in(Menu::getMenuType, SystemConstants.MENU, SystemConstants.BUTTON);
            wrapper.eq(Menu::getStatus, SystemConstants.STATUS_NORMAL);
            List<Menu> list = list(wrapper);
            List<String> perms = list.stream()
                    .map(Menu::getPerms)
                    .collect(Collectors.toList());
            return perms;
        }
        return getBaseMapper().selectPermsByUserId(id);
    }

    @Override
    public List<Menu> selectRouterMenuTreeByUserId(Long userId) {
        MenuMapper menuMapper = getBaseMapper();
        List<Menu> menus = null;
        //判断是否是管理员
        if(SecurityUtils.isAdmin()){
            //如果是 获取所有符合要求的Menu
            menus = menuMapper.selectAllRouterMenu();
        }else{
            //否则  获取当前用户所具有的Menu
            menus = menuMapper.selectRouterMenuTreeByUserId(userId);
        }

        //构建tree
        //先找出第一层的菜单  然后去找他们的子菜单设置到children属性中
        List<Menu> menuTree = builderMenuTree(menus,0L);
        return menuTree;
    }

    @Override
    public List<MenuListVo> listAllMenu(MenuDto menuDto) {
        LambdaQueryWrapper<Menu> wrapper =new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(menuDto.getMenuName()), Menu::getMenuName,menuDto.getMenuName());
        wrapper.eq(StringUtils.hasText(menuDto.getStatus()), Menu::getStatus,menuDto.getStatus());
        wrapper.orderByDesc(Menu::getParentId);
        wrapper.orderByDesc(Menu::getOrderNum);
        List<Menu> list = list(wrapper);
        List<MenuListVo> menuListVos = BeanCopyUtils.copyBeanList(list, MenuListVo.class);
        return menuListVos;
    }

    @Override
    public ResponseResult addMenu(AddMenuDto addMenuDto) {
        Menu menu = BeanCopyUtils.copyBean(addMenuDto, Menu.class);
        save(menu);
        if(!SecurityUtils.isAdmin()) {
            List<UserRole> userRoles = userRoleMapper.selectByUserId(SecurityUtils.getUserId());
            List<RoleMenu> collect = userRoles.stream().map(new Function<UserRole, RoleMenu>() {
                @Override
                public RoleMenu apply(UserRole userRole) {
                    return new RoleMenu(userRole.getRoleId(), addMenuDto.getId());
                }
            }).collect(Collectors.toList());
            for(RoleMenu roleMenu:collect)
            {
                roleMenuMapper.insert(roleMenu);
            }
        }
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult updateMenu(MenuVo menuVo) {
        if(menuVo.getParentId()==menuVo.getId())
        {
            return ResponseResult.errorResult(AppHttpCodeEnum.NOT_SELF);
        }
        else {
            Menu menu = BeanCopyUtils.copyBean(menuVo, Menu.class);
            menuMapper.updateById(menu);
            return ResponseResult.okResult();
        }
    }

    @Override
    public ResponseResult<MenuVo> listMenuDetail(Long id) {
        Menu menu = menuMapper.selectById(id);
        MenuVo menuVo = BeanCopyUtils.copyBean(menu, MenuVo.class);
        return ResponseResult.okResult(menuVo);
    }

    @Override
    public ResponseResult deleteById(Long menuId) {
        LambdaQueryWrapper<Menu> wrapper  = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId, menuId);
        List<Menu> list = list(wrapper);
        System.out.println(list);
        if(list.size()>0)
        {
            return ResponseResult.errorResult(AppHttpCodeEnum.NOT_CHILLDREN);
        }else
        {
            menuMapper.deleteById(menuId);
            return ResponseResult.okResult();
        }
    }

    @Override
    public List<MenuTreeVo> selectTree() {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId,0);
        List<Menu> menus = menuMapper.selectList(wrapper);
        List<MenuTreeVo> list =new ArrayList<>();
        for(Menu menu:menus)
        {
            MenuTreeVo menuTreeVo = new MenuTreeVo();
            menuTreeVo.setParentId(menu.getParentId());
            menuTreeVo.setId(menu.getId());
            menuTreeVo.setLabel(menu.getMenuName());
            menuTreeVo.setChildren(getSon(menu.getId()));
            list.add(menuTreeVo);
        }
        return list;
    }

    @Override
    public ResponseResult<TreePageVo> roleMenuTreeselect(Long id) {
        LambdaQueryWrapper<RoleMenu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(RoleMenu::getRoleId,id);
        List<RoleMenu> roleMenus = roleMenuMapper.selectList(wrapper);
        List<String> collect = roleMenus.stream().map(roleMenu -> roleMenu.getMenuId().toString()).collect(Collectors.toList());

        LambdaQueryWrapper<Menu> wrapper1 = new LambdaQueryWrapper<>();
        wrapper1.eq(Menu::getParentId,0);
        List<Menu> menus = menuMapper.selectList(wrapper1);
        List<MenuTreeVo> list =new ArrayList<>();
        for(Menu menu:menus)
        {
            MenuTreeVo menuTreeVo = new MenuTreeVo();
            menuTreeVo.setParentId(menu.getParentId());
            menuTreeVo.setId(menu.getId());
            menuTreeVo.setLabel(menu.getMenuName());
            menuTreeVo.setChildren(getSon(menu.getId()));
            list.add(menuTreeVo);
        }

        TreePageVo treePageVo = new TreePageVo();
        treePageVo.setCheckedKeys(collect);
        treePageVo.setMenus(list);
        return ResponseResult.okResult(treePageVo);
    }

    private List<MenuTreeVo> getSon(Long id) {
        LambdaQueryWrapper<Menu> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Menu::getParentId,id);
        List<Menu> menus = menuMapper.selectList(wrapper);
        List<MenuTreeVo>  list = new ArrayList<>();
        for (Menu menu :menus)
        {
            MenuTreeVo menuTreeVo = new MenuTreeVo();
            menuTreeVo.setParentId(menu.getParentId());
            menuTreeVo.setId(menu.getId());
            menuTreeVo.setLabel(menu.getMenuName());
            menuTreeVo.setChildren(getSon(menu.getId()));
            list.add(menuTreeVo);
        }
        return list;
    }

    private List<Menu> builderMenuTree(List<Menu> menus, Long parentId) {
        List<Menu> menuTree = menus.stream()
                .filter(menu -> menu.getParentId().equals(parentId))
                .map(menu -> menu.setChildren(getChildren(menu, menus)))
                .collect(Collectors.toList());
        return menuTree;
    }

    /**
     * 获取存入参数的 子Menu集合
     * @param menu
     * @param menus
     * @return
     */
    private List<Menu> getChildren(Menu menu, List<Menu> menus) {
        List<Menu> childrenList = menus.stream()
                .filter(m -> m.getParentId().equals(menu.getId()))
                .map(m->m.setChildren(getChildren(m,menus)))
                .collect(Collectors.toList());
        return childrenList;
    }
}
