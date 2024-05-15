package com.gcf.controller;

import com.gcf.domain.ResponseResult;
import com.gcf.domain.dto.AddMenuDto;
import com.gcf.domain.dto.MenuDto;
import com.gcf.domain.vo.MenuListVo;
import com.gcf.domain.vo.MenuTreeVo;
import com.gcf.domain.vo.MenuVo;
import com.gcf.domain.vo.TreePageVo;
import com.gcf.service.MenuService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/system/menu")
public class MenuController {

    @Autowired
    private MenuService menuService;
    @GetMapping("/list")
    public ResponseResult list(MenuDto menuDto){

        List<MenuListVo> list = menuService.listAllMenu(menuDto);
        return ResponseResult.okResult(list);
    }
    @PostMapping
    public ResponseResult addMenu(@RequestBody AddMenuDto addMenuDto){
        return menuService.addMenu(addMenuDto);
    }
    @GetMapping("{id}")
    public ResponseResult<MenuVo> listMenuDetail(@PathVariable("id") Long id){
        return menuService.listMenuDetail(id);
    }
    @PutMapping
    public ResponseResult updateMenu(@RequestBody MenuVo menuVo){
        return menuService.updateMenu(menuVo);
    }

    @DeleteMapping("/{menuId}")
    public ResponseResult deleteArticleById(@PathVariable("menuId") Long menuId) {

        return menuService.deleteById(menuId);
    }
    @GetMapping("/treeselect")
    public ResponseResult selectTree(){
        List<MenuTreeVo>  menuTreeVos = menuService.selectTree();
        return ResponseResult.okResult(menuTreeVos);
    }
    @GetMapping("/roleMenuTreeselect/{id}")
    public ResponseResult<TreePageVo> roleMenuTreeselect(@PathVariable("id") Long id){
        return menuService.roleMenuTreeselect(id);
    }
}
