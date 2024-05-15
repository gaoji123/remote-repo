package com.gcf.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
@AllArgsConstructor
@NoArgsConstructor
public class UserSelfVo {
    private Long id;
    private String nickName;
    private String email;
    private String status;
    private String sex;
    private String userName;
}
