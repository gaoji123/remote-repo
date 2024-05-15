package com.gcf.domain.vo;


import com.gcf.domain.entity.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Accessors(chain = true)
public class PageVo1 {
    private List<String> roleIds;
    private List<Role> roles;
    private UserSelfVo user;
}
