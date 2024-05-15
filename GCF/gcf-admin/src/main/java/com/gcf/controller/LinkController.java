package com.gcf.controller;

import com.gcf.domain.ResponseResult;
import com.gcf.domain.dto.AddLinkDto;
import com.gcf.domain.dto.LinkListDto;
import com.gcf.domain.vo.LinkListVo;
import com.gcf.domain.vo.PageVo;
import com.gcf.service.LinkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/link")
public class LinkController {
    @Autowired
    private LinkService linkService;

    @GetMapping("/list")
    public ResponseResult<PageVo> pageLinkList(Integer pageNum, Integer pageSize, LinkListDto linkListDto){
        return linkService.pageLinkList(pageNum,pageSize,linkListDto);
    }

    @PostMapping
    public ResponseResult addLink(@RequestBody AddLinkDto addLinkDto){
        return linkService.addLink(addLinkDto);
    }
    @GetMapping("{id}")
    public ResponseResult<LinkListVo> listLinkDetail(@PathVariable("id") Long id){
        return linkService.listLinkDetail(id);
    }

    @PutMapping
    public ResponseResult updateLink(@RequestBody LinkListVo linkListVo){
        return linkService.updateLink(linkListVo);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteLinkById(@PathVariable("id") Long id) {

        return linkService.deleteLinkById(id);
    }
}
