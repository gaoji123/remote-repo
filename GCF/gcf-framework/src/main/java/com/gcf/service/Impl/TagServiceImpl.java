package com.gcf.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gcf.domain.ResponseResult;
import com.gcf.domain.dto.AddTagDto;
import com.gcf.domain.dto.TagListDto;
import com.gcf.domain.entity.Tag;
import com.gcf.domain.vo.PageVo;
import com.gcf.domain.vo.TagDetailVo;
import com.gcf.domain.vo.TagVo;
import com.gcf.enums.AppHttpCodeEnum;
import com.gcf.exception.SystemException;
import com.gcf.mapper.TagMapper;
import com.gcf.service.TagService;
import com.gcf.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * 标签(Tag)表服务实现类
 *
 * @author makejava
 * @since 2024-04-19 20:24:46
 */
@Service("tagService")
public class TagServiceImpl extends ServiceImpl<TagMapper, Tag> implements TagService {
@Autowired
private TagMapper tagMapper;
    @Override
    public ResponseResult<PageVo> pageTagList(Integer pageNum, Integer pageSize, TagListDto tagListDto) {

        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(StringUtils.hasText(tagListDto.getName()),Tag::getName,tagListDto.getName());
        wrapper.eq(StringUtils.hasText(tagListDto.getRemark()),Tag::getRemark,tagListDto.getRemark());

        Page<Tag> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,wrapper);

        PageVo pageVo = new PageVo(page.getRecords(),page.getTotal());

        return  ResponseResult.okResult(pageVo);
    }
//在这里实现新增标签
    @Override
    public ResponseResult addTag(Tag tag) {
        if (!StringUtils.hasText(tag.getName())) {
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        if (!StringUtils.hasText(tag.getRemark())) {
            throw new SystemException(AppHttpCodeEnum.CONTENT_NOT_NULL);
        }
        save(tag);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteById(Long id) {
        tagMapper.deleteById(id);
        return ResponseResult.okResult();
    }



    @Override
    public List<TagVo> listAllTag() {
        LambdaQueryWrapper<Tag> wrapper = new LambdaQueryWrapper<>();
        wrapper.select(Tag::getId,Tag::getName);
        List<Tag> list = list(wrapper);
        List<TagVo> tagVos = BeanCopyUtils.copyBeanList(list, TagVo.class);
        return tagVos;
    }

    @Override
    public ResponseResult<TagDetailVo> listTagDetail(Long id) {
        Tag tag = tagMapper.selectById(id);
        TagDetailVo tagDetailVo = BeanCopyUtils.copyBean(tag, TagDetailVo.class);
        return ResponseResult.okResult(tagDetailVo);
    }

    @Override
    public ResponseResult updateTag(AddTagDto addTag) {
        Tag tag = BeanCopyUtils.copyBean(addTag, Tag.class);
        tagMapper.updateById(tag);
        return ResponseResult.okResult();
    }
}
