package com.example.toponym.service.impl;

import com.example.toponym.entity.bean.UserRole;
import com.example.toponym.mapper.UserRoleMapper;
import com.example.toponym.service.UserRoleService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户角色关联表 服务实现类
 * </p>
 *
 * @author 杨宇帆
 * @since 2023-04-15
 */
@Service
public class UserRoleServiceImpl extends ServiceImpl<UserRoleMapper, UserRole> implements UserRoleService {

}
