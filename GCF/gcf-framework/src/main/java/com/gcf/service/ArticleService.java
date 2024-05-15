package com.gcf.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.gcf.domain.ResponseResult;
import com.gcf.domain.dto.AddArticleDto;
import com.gcf.domain.dto.ArticleListDto;
import com.gcf.domain.entity.Article;
import com.gcf.domain.vo.ArticleTagDetailVo;
import com.gcf.domain.vo.PageVo;

public interface ArticleService extends IService<Article> {

    ResponseResult hotArticleList();

    ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId);

    ResponseResult getArticleDetail(Long id);

    ResponseResult updateViewCount(Long id);

    ResponseResult add(AddArticleDto article);

    ResponseResult<PageVo> pageArticleList(Integer pageNum, Integer pageSize, ArticleListDto articleListDto);

    ResponseResult deleteById(Long id);

    ResponseResult<ArticleTagDetailVo> listArticleDetail(Long id);

    ResponseResult updateArticleDetail(ArticleTagDetailVo articleTagDetailVo);
}