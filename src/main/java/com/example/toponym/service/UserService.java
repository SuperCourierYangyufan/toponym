package com.example.toponym.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.toponym.entity.bean.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.toponym.entity.dto.UserDTO;
import com.example.toponym.entity.query.UserQuery;
import java.util.List;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author 杨宇帆
 * @since 2023-04-13
 */
public interface UserService extends IService<User> {

    /**
     * 登录
     * @param userDTO
     * @param request
     * @return
     * @throws Exception
     */
    String login(UserDTO userDTO, HttpServletRequest request) throws Exception;

    /**
     * 新增用户信息
     * @param userDTO
     * @return
     * @throws Exception
     */
    Long addUser(UserDTO userDTO) throws Exception;

    /**
     * 退出
     * @throws Exception
     */
    void logout() throws Exception;

    /**
     * 分页查询
     * @param userQuery
     * @return
     * @throws Exception
     */
    IPage<UserDTO> selectPage(UserQuery userQuery) throws Exception;

    /**
     * 批量修改用户状态
     * @param userDTO
     * @throws Exception
     */
    void batchChangeUserStatus(UserDTO userDTO) throws Exception;

    /**
     * 根据角色Id,组织id获取用户列表
     * @param roleId
     * @param organizationId
     * @throws Exception
     * @return
     */
    List<UserDTO> getUserListByOrganizationIdAndRoleId(Long roleId, Long organizationId) throws Exception;

    /**
     * 获取角色下所有用户
     * @param roleId
     * @return
     * @throws Exception
     */
    List<UserDTO> getUserListByRoleId(Long roleId) throws Exception;

    /**
     * 获取手机验证码
     * @param mobile
     * @throws Exception
     */
    void getSmsCode(String mobile) throws Exception;

    /**
     * 外部用户注册
     * @param userDTO
     * @param request
     * @return
     * @throws Exception
     */
    String register(UserDTO userDTO, HttpServletRequest request) throws Exception;
}
