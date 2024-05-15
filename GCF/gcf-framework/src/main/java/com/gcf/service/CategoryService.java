package com.gcf.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.gcf.domain.ResponseResult;
import com.gcf.domain.dto.AddCategoryDto;
import com.gcf.domain.dto.CategoryListDto;
import com.gcf.domain.entity.Category;
import com.gcf.domain.vo.CategoryListVo;
import com.gcf.domain.vo.CategoryVo;
import com.gcf.domain.vo.PageVo;

import java.util.List;

/**
 * 分类表(Category)表服务接口
 *
 * @author makejava
 * @since 2024-04-17 14:03:24
 */
public interface CategoryService extends IService<Category> {

    ResponseResult getCategoryList();


    List<CategoryVo> listAllCategory();

    ResponseResult<PageVo> pageCategoryList(Integer pageNum, Integer pageSize, CategoryListDto categoryListDto);

    ResponseResult addCategory(AddCategoryDto addCategoryDto);

    ResponseResult<CategoryListVo> listCategoryDetail(Long id);

    ResponseResult updateCategory(CategoryListVo categoryListVo);

    ResponseResult deleteCategoryById(Long id);
}
