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
public class UpdateUserDto {
    private Long id;
    private String status;
    private String userName;
    private String sex;
    private String email;
    private String nickName;
    private List<String> roleIds;
}
