package com.gcf.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class HotArticleVo {
    private Long id;

    private String title;

    private Long viewCount;
    //是否允许评论 1是，0否


}
