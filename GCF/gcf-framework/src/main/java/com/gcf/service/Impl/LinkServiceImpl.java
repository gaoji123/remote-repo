package com.gcf.service.Impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gcf.constants.SystemConstants;
import com.gcf.domain.ResponseResult;
import com.gcf.domain.dto.AddLinkDto;
import com.gcf.domain.dto.LinkListDto;
import com.gcf.domain.entity.Link;
import com.gcf.domain.vo.LinkListVo;
import com.gcf.domain.vo.LinkVo;
import com.gcf.domain.vo.PageVo;
import com.gcf.mapper.LinkMapper;
import com.gcf.service.LinkService;
import com.gcf.utils.BeanCopyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

@Service("linkService")
public class LinkServiceImpl extends ServiceImpl<LinkMapper, Link> implements LinkService {
    @Autowired
    private LinkMapper linkMapper;

    @Override
    public ResponseResult getAllLink() {
        //查询所有审核通过的
        LambdaQueryWrapper<Link> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Link::getStatus, SystemConstants.LINK_STATUS_NORMAL);
        List<Link> links = list(queryWrapper);
        //转换成vo
        List<LinkVo> linkVos = BeanCopyUtils.copyBeanList(links, LinkVo.class);
        //封装返回
        return ResponseResult.okResult(linkVos);
    }

    @Override
    public ResponseResult<PageVo> pageLinkList(Integer pageNum, Integer pageSize, LinkListDto linkListDto) {
        LambdaQueryWrapper<Link> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(linkListDto.getName()), Link::getName, linkListDto.getName());
        wrapper.eq(StringUtils.hasText(linkListDto.getStatus()), Link::getStatus, linkListDto.getStatus());
        Page<Link> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page, wrapper);
        List<LinkListVo> linkListVos = BeanCopyUtils.copyBeanList(page.getRecords(), LinkListVo.class);
        PageVo pageVo = new PageVo(linkListVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult addLink(AddLinkDto addLinkDto) {
        Link link = BeanCopyUtils.copyBean(addLinkDto, Link.class);
        linkMapper.insert(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<LinkListVo> listLinkDetail(Long id) {
        Link link = linkMapper.selectById(id);
        LinkListVo linkListVo = BeanCopyUtils.copyBean(link, LinkListVo.class);
        return ResponseResult.okResult(linkListVo);
    }

    @Override
    public ResponseResult updateLink(LinkListVo linkListVo) {
        Link link = BeanCopyUtils.copyBean(linkListVo, Link.class);
        linkMapper.updateById(link);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult deleteLinkById(Long id) {
        linkMapper.deleteById(id);
        return ResponseResult.okResult();
    }
}