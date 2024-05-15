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
public class AddUserDto {
    private String status;
    private String userName;
    private String phonenumber;
    private String sex;
    private String email;
    private String password;
    private String nickName;
    private List<String> roleIds;
}
