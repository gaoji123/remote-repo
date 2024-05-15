package com.gcf.service.Impl;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gcf.constants.SystemConstants;
import com.gcf.domain.ResponseResult;
import com.gcf.domain.dto.AddArticleDto;
import com.gcf.domain.dto.ArticleListDto;
import com.gcf.domain.entity.Article;
import com.gcf.domain.entity.ArticleTag;
import com.gcf.domain.entity.Category;
import com.gcf.domain.vo.*;
import com.gcf.mapper.ArticleMapper;
import com.gcf.mapper.ArticleTagMapper;
import com.gcf.service.ArticleService;
import com.gcf.service.ArticleTagService;
import com.gcf.service.CategoryService;
import com.gcf.utils.BeanCopyUtils;
import com.gcf.utils.RedisCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ArticleServiceImpl extends ServiceImpl<ArticleMapper, Article> implements ArticleService {
    @Autowired
    private CategoryService categoryService;
    @Autowired
    private RedisCache redisCache;
    @Autowired
    private ArticleMapper articleMapper;
    @Autowired
    private ArticleTagMapper articleTagMapper;
    @Autowired
    private ArticleTagService articleTagService;

    @Override
    public ResponseResult hotArticleList() {
        LambdaQueryWrapper<Article> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        queryWrapper.orderByDesc(Article::getViewCount);
        Page<Article> page = new Page(1, 10);
        page(page, queryWrapper);
        List<Article> records = page.getRecords();

        List<HotArticleVo> articleVos = BeanCopyUtils.copyBeanList(records, HotArticleVo.class);
        return ResponseResult.okResult(articleVos);
    }

    @Override
    public ResponseResult articleList(Integer pageNum, Integer pageSize, Long categoryId) {
        //查询条件
        LambdaQueryWrapper<Article> lambdaQueryWrapper = new LambdaQueryWrapper<>();
        // 如果 有categoryId 就要 查询时要和传入的相同
        lambdaQueryWrapper.eq(Objects.nonNull(categoryId) && categoryId > 0, Article::getCategoryId, categoryId);
        // 状态是正式发布的
        lambdaQueryWrapper.eq(Article::getStatus, SystemConstants.ARTICLE_STATUS_NORMAL);
        // 对isTop进行降序
        lambdaQueryWrapper.orderByDesc(Article::getIsTop);

        //分页查询
        Page<Article> page = new Page<>(pageNum, pageSize);
        page(page, lambdaQueryWrapper);

        List<Article> articles = page.getRecords();

        articles = articles.stream()
                .map(article -> article.setCategoryName(categoryService.getById(article.getCategoryId()).getName())).collect(Collectors.toList());
        List<ArticleListVo> articleListVos = BeanCopyUtils.copyBeanList(articles, ArticleListVo.class);

        PageVo pageVo = new PageVo(articleListVos, page.getTotal());
        return ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult getArticleDetail(Long id) {
        Article article = getById(id);
        Integer viewCount = redisCache.getCacheMapValue("article:viewCount", id.toString());
        article.setViewCount(viewCount.longValue());
        ArticleDetailVo articleDetailVo = BeanCopyUtils.copyBean(article, ArticleDetailVo.class);
        Long categoryId = articleDetailVo.getCategoryId();
        Category category = categoryService.getById(categoryId);
        if(category!=null)
        {
            articleDetailVo.setCategoryName(category.getName());
        }
        return ResponseResult.okResult(articleDetailVo);
    }

    @Override
    public ResponseResult updateViewCount(Long id) {
        //更新redis中对应 id的浏览量
        redisCache.incrementCacheMapValue("article:viewCount",id.toString(),1);
        return ResponseResult.okResult();
    }



    @Override
    @Transactional
    public ResponseResult add(AddArticleDto articleDto) {
        //添加 博客
        Article article = BeanCopyUtils.copyBean(articleDto, Article.class);
        save(article);


        List<ArticleTag> articleTags = articleDto.getTags().stream()
                .map(tagId -> new ArticleTag(article.getId(), tagId))
                .collect(Collectors.toList());

        //添加 博客和标签的关联
        articleTagService.saveBatch(articleTags);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<PageVo> pageArticleList(Integer pageNum, Integer pageSize, ArticleListDto articleListDto) {
        LambdaQueryWrapper<Article> wrapper = new LambdaQueryWrapper<>();
        wrapper.like(StringUtils.hasText(articleListDto.getTitle()),Article::getTitle,articleListDto.getTitle());
        wrapper.like(StringUtils.hasText(articleListDto.getSummary()),Article::getSummary,articleListDto.getSummary());

        Page<Article> page = new Page<>();
        page.setCurrent(pageNum);
        page.setSize(pageSize);
        page(page,wrapper);
        List<ArticleAdminListVo> articleAdminListVos = BeanCopyUtils.copyBeanList(page.getRecords(), ArticleAdminListVo.class);
        PageVo pageVo = new PageVo(articleAdminListVos,page.getTotal());
        return  ResponseResult.okResult(pageVo);
    }

    @Override
    public ResponseResult deleteById(Long id) {

        articleMapper.deleteById(id);
        articleTagMapper.deleteByArticleId(id);
        return ResponseResult.okResult();
    }

    @Override
    public ResponseResult<ArticleTagDetailVo> listArticleDetail(Long id) {
        Article article = articleMapper.selectById(id);
        LambdaQueryWrapper<ArticleTag> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(ArticleTag::getArticleId,id);
        List<ArticleTag> articleTags = articleTagMapper.selectList(wrapper);
        ArticleTagDetailVo articleTagDetailVo = BeanCopyUtils.copyBean(article, ArticleTagDetailVo.class);
        List<String> collect = articleTags.stream().map(articleTag -> articleTag.getTagId().toString()).collect(Collectors.toList());
        articleTagDetailVo.setTags(collect);
        return ResponseResult.okResult(articleTagDetailVo);
    }

    @Override
    public ResponseResult updateArticleDetail(ArticleTagDetailVo articleTagDetailVo) {
        Article article = BeanCopyUtils.copyBean(articleTagDetailVo, Article.class);
        articleMapper.updateById(article);
        List<String> tags = articleTagDetailVo.getTags();
        articleTagMapper.deleteByArticleId(articleTagDetailVo.getId());
        for(String tag :tags)
        {
            articleTagMapper.addArticleTag(articleTagDetailVo.getId(),Long.valueOf(tag).longValue());
        }
            return ResponseResult.okResult();
    }
}