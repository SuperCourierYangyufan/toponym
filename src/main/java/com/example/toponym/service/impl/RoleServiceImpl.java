package com.example.toponym.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.toponym.entity.bean.Menu;
import com.example.toponym.entity.bean.Role;
import com.example.toponym.entity.bean.RoleMenu;
import com.example.toponym.entity.bean.UserDetailsInfo;
import com.example.toponym.entity.bean.UserRole;
import com.example.toponym.entity.constant.OtherInfoConstant;
import com.example.toponym.entity.dto.MenuDTO;
import com.example.toponym.entity.dto.RoleDTO;
import com.example.toponym.entity.dto.UserDTO;
import com.example.toponym.exception.ServiceException;
import com.example.toponym.mapper.RoleMapper;
import com.example.toponym.service.MenuService;
import com.example.toponym.service.RoleMenuService;
import com.example.toponym.service.RoleService;
import com.example.toponym.service.UserRoleService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 角色信息 服务实现类
 * </p>
 *
 * @author 杨宇帆
 * @since 2023-04-15
 */
@Service
@Slf4j
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {

    @Autowired
    private MenuService menuService;

    @Autowired
    private RoleMenuService roleMenuService;

    @Autowired
    private UserRoleService userRoleService;

    @Override
    @Transactional(rollbackFor = {ServiceException.class, Exception.class})
    public Long addRole(RoleDTO roleDTO) throws Exception {
        checkParam(roleDTO);

        Role role = BeanUtil.copyProperties(roleDTO, Role.class);
        role.setCreateTime(new Date());
        role.setUpdateTime(new Date());
        role.setCreateUser(
                ((UserDetailsInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser()
                        .getId());
        role.setUpdateUser(
                ((UserDetailsInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser()
                        .getId());
        super.save(role);

        if (!CollectionUtil.isEmpty(roleDTO.getMenuDTOList())) {
            //角色绑定菜单
            roleBindingMenu(role.getId(), roleDTO.getMenuDTOList());
        }

        return role.getId();
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, ServiceException.class})
    public void roleAddUserList(RoleDTO roleDTO) throws Exception {
        if (roleDTO.getId() == null) {
            throw new ServiceException("角色id不能为空");
        }
        if (CollectionUtil.isEmpty(roleDTO.getUserDTOList())) {
            throw new ServiceException("选择的用户信息不能为空");
        }
        //删除老数据
        userRoleService.remove(new LambdaQueryWrapper<UserRole>().eq(UserRole::getRoleId, roleDTO.getId()));
        //添加新数据
        List<UserRole> userRoleList = new ArrayList<>();
        for (UserDTO userDTO : roleDTO.getUserDTOList()) {
            UserRole userRole = new UserRole();
            userRole.setUserId(userDTO.getId());
            userRole.setRoleId(roleDTO.getId());
            userRole.setCreateTime(new Date());
            userRole.setUpdateTime(new Date());
            userRole.setCreateUser(
                    ((UserDetailsInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser()
                            .getId());
            userRole.setUpdateUser(
                    ((UserDetailsInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser()
                            .getId());
            userRoleList.add(userRole);
        }
        userRoleService.saveBatch(userRoleList);
    }

    @Override
    public void removeUserRoleInfo(RoleDTO roleDTO) throws Exception {
        if (roleDTO.getId() == null || CollectionUtil.isEmpty(roleDTO.getUserIdList())) {
            throw new ServiceException("入参不能为空");
        }
        userRoleService.remove(new LambdaQueryWrapper<UserRole>().eq(UserRole::getRoleId, roleDTO.getId())
                                       .in(UserRole::getUserId, roleDTO.getUserIdList()));
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, ServiceException.class})
    public void updateRole(RoleDTO roleDTO) throws Exception {
        checkParam(roleDTO);
        if (roleDTO.getId() == null) {
            throw new ServiceException("主键不能为空");
        }
        Role role = BeanUtil.copyProperties(roleDTO, Role.class);
        role.setUpdateTime(new Date());
        role.setUpdateUser(
                ((UserDetailsInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser()
                        .getId());

        //删除关系
        roleMenuService.remove(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, roleDTO.getId()));
        //新增关系
        if (!CollectionUtil.isEmpty(roleDTO.getMenuDTOList())) {
            //角色绑定菜单
            roleBindingMenu(role.getId(), roleDTO.getMenuDTOList());
        }
        super.updateById(role);
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, ServiceException.class})
    public void removeRole(Long id) throws Exception {
        //删除关系
        roleMenuService.remove(new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, id));
        //删除角色用户信息
        userRoleService.remove(new LambdaQueryWrapper<UserRole>().eq(UserRole::getRoleId, id));
        //本体
        super.removeById(id);
    }

    private void roleBindingMenu(Long roleId, List<MenuDTO> menuDTOList) throws Exception {
        List<Long> menuIdList = menuDTOList.stream().map(MenuDTO::getId).filter(Objects::nonNull)
                .collect(Collectors.toList());
        if (CollectionUtil.isEmpty(menuIdList)) {
            return;
        }
        List<Menu> menuList = menuService.listByIds(menuIdList);
        if (CollectionUtil.isEmpty(menuList)) {
            throw new ServiceException("未找到对应菜单");
        }
        List<RoleMenu> roleMenuList = new ArrayList<>();
        Map<Long, Menu> menuIdMap = menuList.stream().collect(Collectors.toMap(Menu::getId, Function.identity()));
        for (Menu menu : menuList) {
            //检查是否有父节点
            if (!OtherInfoConstant.PARENT_MENU_ID.equals(menu.getParentMenuId())
                    && menuIdMap.get(menu.getParentMenuId()) == null) {
                //不为根节点且未选父节点
                throw new ServiceException("菜单:[" + menu.getMenuName() + "]未选父节点,请勾选");
            }
            RoleMenu roleMenu = new RoleMenu();
            roleMenu.setMenuId(menu.getId());
            roleMenu.setRoleId(roleId);
            roleMenu.setCreateTime(new Date());
            roleMenu.setUpdateTime(new Date());
            roleMenu.setCreateUser(
                    ((UserDetailsInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser()
                            .getId());
            roleMenu.setUpdateUser(
                    ((UserDetailsInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser()
                            .getId());
            roleMenuList.add(roleMenu);
        }
        roleMenuService.saveBatch(roleMenuList);
    }

    private void checkParam(RoleDTO roleDTO) throws ServiceException {
        if (roleDTO == null) {
            throw new ServiceException("入参不能为空");
        }
        if (StringUtils.isEmpty(roleDTO.getRoleName())) {
            throw new ServiceException("角色名称不能为空");
        }
        if (roleDTO.getRoleType() == null) {
            throw new ServiceException("角色类型不能为空");
        }
    }
}
