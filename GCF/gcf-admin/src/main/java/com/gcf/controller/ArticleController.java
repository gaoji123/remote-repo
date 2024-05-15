package com.gcf.controller;

import com.gcf.domain.ResponseResult;
import com.gcf.domain.dto.AddArticleDto;
import com.gcf.domain.dto.ArticleListDto;
import com.gcf.domain.vo.ArticleTagDetailVo;
import com.gcf.domain.vo.PageVo;
import com.gcf.service.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/content/article")
public class ArticleController {

    @Autowired
    private ArticleService articleService;

    @PostMapping
    public ResponseResult addArticle(@RequestBody AddArticleDto article){
        return articleService.add(article);
    }
    @GetMapping("/list")
    public ResponseResult<PageVo> list(Integer pageNum, Integer pageSize, ArticleListDto articleListDto){
        return articleService.pageArticleList(pageNum,pageSize,articleListDto);
    }
    @DeleteMapping("/{id}")
    public ResponseResult deleteArticleById(@PathVariable("id") Long id) {

        return articleService.deleteById(id);
    }
    @GetMapping("{id}")
    public ResponseResult<ArticleTagDetailVo> listArticleDetail(@PathVariable("id") Long id){
        return articleService.listArticleDetail(id);
    }
    @PutMapping
    public ResponseResult updateArticleDetail(@RequestBody ArticleTagDetailVo articleTagDetailVo){
        return articleService.updateArticleDetail(articleTagDetailVo);
    }
}