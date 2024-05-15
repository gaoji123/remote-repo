package com.gcf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gcf.domain.ResponseResult;
import com.gcf.domain.dto.AddLinkDto;
import com.gcf.domain.dto.LinkListDto;
import com.gcf.domain.entity.Link;
import com.gcf.domain.vo.LinkListVo;
import com.gcf.domain.vo.PageVo;

public interface LinkService extends IService<Link> {

    ResponseResult getAllLink();

    ResponseResult<PageVo> pageLinkList(Integer pageNum, Integer pageSize, LinkListDto linkListDto);

    ResponseResult addLink(AddLinkDto addLinkDto);


    ResponseResult<LinkListVo> listLinkDetail(Long id);

    ResponseResult updateLink(LinkListVo linkListVo);

    ResponseResult deleteLinkById(Long id);
}