package com.example.toponym.service;

import com.example.toponym.entity.bean.Role;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.toponym.entity.dto.RoleDTO;

/**
 * <p>
 * 角色信息 服务类
 * </p>
 *
 * @author 杨宇帆
 * @since 2023-04-15
 */
public interface RoleService extends IService<Role> {

    /**
     * 添加角色
     * @param roleDTO
     * @return
     * @throws Exception
     */
    Long addRole(RoleDTO roleDTO) throws Exception;

    /**
     * 角色绑定用户
     * @param roleDTO
     */
    void roleAddUserList(RoleDTO roleDTO) throws Exception;

    /**
     * 删除角色下的用户列表
     * @param roleDTO
     * @throws Exception
     */
    void removeUserRoleInfo(RoleDTO roleDTO) throws Exception;

    /**
     * 更新角色信息
     * @param roleDTO
     * @throws Exception
     */
    void updateRole(RoleDTO roleDTO) throws Exception;

    /**
     * 删除角色
     * @param id
     * @throws Exception
     */
    void removeRole(Long id) throws Exception;
}
