package com.gcf.controller;


import com.gcf.domain.ResponseResult;
import com.gcf.domain.dto.AddTagDto;
import com.gcf.domain.dto.TagListDto;
import com.gcf.domain.entity.Tag;
import com.gcf.domain.vo.PageVo;
import com.gcf.domain.vo.TagDetailVo;
import com.gcf.domain.vo.TagVo;
import com.gcf.service.TagService;
import com.gcf.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/content/tag")
public class TagController {
    @Autowired
    private TagService tagService;

    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, TagListDto tagListDto){
        return tagService.pageTagList(pageNum,pageSize,tagListDto);
    }
    @PostMapping
    public ResponseResult addTag(@RequestBody AddTagDto addTag){
        Tag tag = BeanCopyUtils.copyBean(addTag, Tag.class);
        return tagService.addTag(tag);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteTagById(@PathVariable("id") Long id) {

        return tagService.deleteById(id);
    }
    @PutMapping
    public ResponseResult updateViewCount(@RequestBody AddTagDto addTag ){
        return tagService.updateTag(addTag);
    }
    @GetMapping("/listAllTag")
    public ResponseResult listAllTag(){
        List<TagVo> list = tagService.listAllTag();
        return ResponseResult.okResult(list);
    }
    @GetMapping("{id}")
    public ResponseResult<TagDetailVo> listTagDetail(@PathVariable("id") Long id){
        return tagService.listTagDetail(id);
    }
}