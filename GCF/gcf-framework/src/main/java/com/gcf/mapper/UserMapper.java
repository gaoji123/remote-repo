package com.gcf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gcf.domain.entity.User;
import org.apache.ibatis.annotations.Select;


/**
 * 用户表(User)表数据库访问层
 *
 * @author makejava
 * @since 2024-04-17 18:19:07
 */
public interface UserMapper extends BaseMapper<User> {
@Select("select * from sys_user where user_name = #{userName}")
    User selectByUserName(String userName);
}

