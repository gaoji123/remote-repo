package com.gcf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gcf.domain.ResponseResult;
import com.gcf.domain.dto.AddTagDto;
import com.gcf.domain.dto.TagListDto;
import com.gcf.domain.entity.Tag;
import com.gcf.domain.vo.PageVo;
import com.gcf.domain.vo.TagDetailVo;
import com.gcf.domain.vo.TagVo;

import java.util.List;

public interface TagService extends IService<Tag> {

    ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto);

    ResponseResult addTag(Tag tag);

    ResponseResult deleteById(Long id);



    List<TagVo> listAllTag();


    ResponseResult<TagDetailVo> listTagDetail(Long id);

    ResponseResult updateTag(AddTagDto addTag);
}
