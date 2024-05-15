package com.gcf.controller;


import com.alibaba.excel.EasyExcel;
import com.alibaba.fastjson.JSON;
import com.gcf.domain.ResponseResult;
import com.gcf.domain.dto.AddCategoryDto;
import com.gcf.domain.dto.CategoryListDto;
import com.gcf.domain.entity.Category;
import com.gcf.domain.vo.*;
import com.gcf.enums.AppHttpCodeEnum;
import com.gcf.service.CategoryService;
import com.gcf.utils.BeanCopyUtils;
import com.gcf.utils.WebUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.List;

@RestController
@RequestMapping("/content/category")
public class CategoryController {
    @Autowired
    private CategoryService categoryService;

    @GetMapping("/listAllCategory")
    public ResponseResult listAllCategory (){
        List<CategoryVo> list = categoryService.listAllCategory();
        return ResponseResult.okResult(list);
    }

@PreAuthorize("@ps.hasPermission('content:category:export')")
    @GetMapping("f")
    public void export(HttpServletResponse response){
        try {
            //设置下载文件的请求头
            WebUtils.setDownLoadHeader("分类.xlsx",response);
            //获取需要导出的数据
            List<Category> categoryVos = categoryService.list();

            List<ExcelCategoryVo> excelCategoryVos = BeanCopyUtils.copyBeanList(categoryVos, ExcelCategoryVo.class);
            //把数据写入到Excel中
            EasyExcel.write(response.getOutputStream(), ExcelCategoryVo.class).autoCloseStream(Boolean.FALSE).sheet("分类导出")
                    .doWrite(excelCategoryVos);

        } catch (Exception e) {
            //如果出现异常也要响应json
            ResponseResult result = ResponseResult.errorResult(AppHttpCodeEnum.SYSTEM_ERROR);
            WebUtils.renderString(response, JSON.toJSONString(result));
        }
    }

    @GetMapping("/list")
    public ResponseResult<PageVo> pageCategoryList(Integer pageNum, Integer pageSize, CategoryListDto categoryListDto){
        return categoryService.pageCategoryList(pageNum,pageSize,categoryListDto);
    }

    @PostMapping
    public ResponseResult addCategory(@RequestBody AddCategoryDto addCategoryDto){
        return categoryService.addCategory(addCategoryDto);
    }

    @GetMapping("{id}")
    public ResponseResult<CategoryListVo> listRoleCategoryDetail(@PathVariable("id") Long id){
        return categoryService.listCategoryDetail(id);
    }
    @PutMapping
    public ResponseResult updateCategory(@RequestBody CategoryListVo categoryListVo){
        return categoryService.updateCategory(categoryListVo);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteCategoryById(@PathVariable("id") Long id) {

        return categoryService.deleteCategoryById(id);
    }
}