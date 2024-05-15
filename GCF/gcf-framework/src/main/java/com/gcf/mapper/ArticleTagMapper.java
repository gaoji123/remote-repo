package com.gcf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gcf.domain.entity.ArticleTag;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;


/**
 * 文章标签关联表(ArticleTag)表数据库访问层
 *
 * @author makejava
 * @since 2024-04-20 21:12:46
 */
public interface ArticleTagMapper extends BaseMapper<ArticleTag> {

    @Delete("delete from gcf_article_tag where article_id=#{id}")
    void deleteByArticleId(Long id);

    @Insert("insert into gcf_article_tag (article_id, tag_id) VALUES (#{articleId},#{tagId})")
    void addArticleTag(@Param("articleId") Long articleId, @Param("tagId") Long tagId);
}

