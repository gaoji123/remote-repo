package com.gcf.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gcf.domain.entity.ArticleTag;
import com.gcf.mapper.ArticleTagMapper;
import com.gcf.service.ArticleTagService;
import org.springframework.stereotype.Service;

/**
 * 文章标签关联表(ArticleTag)表服务实现类
 *
 * @author makejava
 * @since 2024-04-20 21:12:48
 */
@Service("articleTagService")
public class ArticleTagServiceImpl extends ServiceImpl<ArticleTagMapper, ArticleTag> implements ArticleTagService {



}
