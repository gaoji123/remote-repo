package com.gcf.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.Date;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class ArticleDetailVo {

    private Long id;
    //标题
    private String title;
    //文章摘要
    private String summary;
    //所属分类名
    private String categoryName;
    //分类ID
    private Long categoryId;
    //评论
    private String content;
    //缩略图
    private String thumbnail;
    //访问量
    private Long viewCount;

    private Date createTime;


}