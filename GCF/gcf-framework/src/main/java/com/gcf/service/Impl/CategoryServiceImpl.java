package com.gcf.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gcf.constants.SystemConstants;
import com.gcf.domain.ResponseResult;
import com.gcf.domain.dto.AddCategoryDto;
import com.gcf.domain.dto.CategoryListDto;
import com.gcf.domain.entity.Article;
import com.gcf.domain.entity.Category;
import com.gcf.domain.vo.CategoryListVo;
import com.gcf.domain.vo.CategoryVo;
import com.gcf.domain.vo.PageVo;
import com.gcf.mapper.CategoryMapper;
import com.gcf.service.ArticleService;
import com.gcf.service.CategoryService;
import com.gcf.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service("categoryService")
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category> implements CategoryService {
    @Autowired
    private ArticleService articleService;
    @Autowired
    private CategoryMapper categoryMapper;

    @Override
    public ResponseResult getCategoryList() {

        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        List<Article> articleList = articleService.list(queryWrapper);
        Set<Long> categoryIds = articleList.stream().map(Article::getCategoryId).collect(Collectors.toSet());
        List<Category> categories = listByIds(categoryIds);
        categories = categories.stream().filter(category -> SystemConstants.STATUS_NORMAL.equals(category.getStatus())).collect(Collectors.toList());
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(categories, CategoryVo.class);
        return ResponseResult.okResult(categoryVos);
    }

    @Override
    public List<CategoryVo> listAllCategory() {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Category::getStatus, SystemConstants.NORMAL);
        List<Category> list = list(wrapper);
        List<CategoryVo> categoryVos = BeanCopyUtils.copyBeanList(list, CategoryVo.class);
        return categoryVos;
    }

    @Override
    public ResponseResult<PageVo> pageCategoryList(Integer pageNum, Integer pageSize, CategoryListDto categoryListDto) {
        LambdaQueryWrapper<Category> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(categoryListDto.getName()), Category::getName, categoryListDto.getName());
        wrapper.eq(StringUtils.hasText(categoryListDto.getStatus()), Category::getStatus, categoryListDto.getStatus());
        Page<Category> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, wrapper);
        List<CategoryListVo> categoryListVos = BeanCopyUtils.copyBeanList(page.getRecords(), CategoryListVo.class);
        PageVo pageVo = new PageVo(categoryListVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addCategory(AddCategoryDto addCategoryDto) {
        Category category = BeanCopyUtils.copyBean(addCategoryDto, Category.class);
        categoryMapper.insert(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<CategoryListVo> listCategoryDetail(Long id) {
        Category category = categoryMapper.selectById(id);
        CategoryListVo categoryListVo = BeanCopyUtils.copyBean(category, CategoryListVo.class);
        return ResponseResult.okResult(categoryListVo);
    }

    @Override
    public ResponseResult updateCategory(CategoryListVo categoryListVo) {
        Category category = BeanCopyUtils.copyBean(categoryListVo, Category.class);
        categoryMapper.updateById(category);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteCategoryById(Long id) {
        categoryMapper.deleteById(id);
        return ResponseResult.okResult();
    }


}
