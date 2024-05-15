package com.gcf.domain.entity;


import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
/**
 * 文章标签关联表(ArticleTag)表实体类
 *
 * @author makejava
 * @since 2024-04-20 21:12:47
 */
@SuppressWarnings("serial")
@Data
@AllArgsConstructor
@NoArgsConstructor
@TableName("gcf_article_tag")
public class ArticleTag  {
    //文章id
    private Long articleId;
    //标签id@TableId
    private Long tagId;




}
