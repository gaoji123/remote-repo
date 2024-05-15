package com.gcf.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class AddRoleDto {
    private String roleName;
    private String roleKey;
    private Integer roleSort;
    private String status;
    private List<String> menuIds;
    private String remark;
}
