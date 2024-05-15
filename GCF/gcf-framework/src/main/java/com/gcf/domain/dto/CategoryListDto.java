package com.gcf.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class CategoryListDto {

    //分类名
    private String name;
    //状态0:正常,1禁用
    private String status;

}
