package com.gcf.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.gcf.domain.entity.UserRole;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Select;

import java.util.List;


/**
 * 用户和角色关联表(UserRole)表数据库访问层
 *
 * @author makejava
 * @since 2024-04-21 16:57:13
 */
public interface UserRoleMapper extends BaseMapper<UserRole> {
    @Delete("delete from sys_user_role where user_id = #{id}")
    void deleteByUserId(Long id);

@Select("select * from sys_user_role where user_id = #{id}")
    List<UserRole> selectByUserId(Long id);
}

