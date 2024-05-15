package com.gcf.service.Impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.gcf.domain.entity.UserRole;
import com.gcf.mapper.UserRoleMapper;
import com.gcf.service.UserRoleService;
import org.springframework.stereotype.Service;

/**
 * 用户和角色关联表(UserRole)表服务实现类
 *
 * @author makejava
 * @since 2024-04-21 16:57:15
 */
@Service("userRoleService")
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
