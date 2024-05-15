package com.gcf.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class RoleVo {
    private Long id;
    private String roleKey;
    private String roleName;
    private Integer roleSort;
    private String status;
}