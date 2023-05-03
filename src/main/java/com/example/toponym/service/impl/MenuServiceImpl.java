package com.example.toponym.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.toponym.entity.bean.Menu;
import com.example.toponym.entity.bean.RoleMenu;
import com.example.toponym.entity.bean.UserDetailsInfo;
import com.example.toponym.entity.constant.OtherInfoConstant;
import com.example.toponym.entity.constant.YesOrNoConstant;
import com.example.toponym.entity.dto.MenuDTO;
import com.example.toponym.exception.ServiceException;
import com.example.toponym.mapper.MenuMapper;
import com.example.toponym.service.MenuService;
import com.example.toponym.service.RoleMenuService;
import com.google.common.collect.Lists;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 菜单信息 服务实现类
 * </p>
 *
 * @author 杨宇帆
 * @since 2023-04-15
 */
@Service
public class MenuServiceImpl extends ServiceImpl<MenuMapper, Menu> implements MenuService {

    @Autowired
    private RoleMenuService roleMenuService;

    @Override
    public Long addMenu(MenuDTO menuDTO) throws Exception {
        checkMenu(menuDTO);

        fillParentInfo(menuDTO);

        menuDTO.setCreateTime(new Date());
        menuDTO.setUpdateTime(new Date());
        menuDTO.setCreateUser(
                ((UserDetailsInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser()
                        .getId());
        menuDTO.setUpdateUser(
                ((UserDetailsInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser()
                        .getId());

        Menu menu = BeanUtil.copyProperties(menuDTO, Menu.class);
        super.save(menu);
        return menu.getId();
    }

    @Override
    @Transactional(rollbackFor = {ServiceException.class, Exception.class})
    public void updateMenu(MenuDTO menuDTO) throws Exception {
        checkMenu(menuDTO);
        if (menuDTO.getId() == null) {
            throw new ServiceException("主键不能为空");
        }
        Menu oldMenu = super.getById(menuDTO.getId());
        if (oldMenu == null) {
            throw new ServiceException("原始信息不存在");
        }
        fillParentInfo(menuDTO);
        //节点下所有子节点更新
        if (!oldMenu.getMenuName().equals(menuDTO.getMenuName())) {
            List<Menu> childMenuList = super.list(
                    new LambdaQueryWrapper<Menu>().eq(Menu::getParentMenuId, oldMenu.getId()));
            if (!CollectionUtil.isEmpty(childMenuList)) {
                childMenuList.forEach(child -> child.setParentMenuName(menuDTO.getMenuName()));
                super.updateBatchById(childMenuList);
            }
        }
        menuDTO.setUpdateTime(new Date());
        menuDTO.setUpdateUser(
                ((UserDetailsInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser()
                        .getId());
        Menu menu = BeanUtil.copyProperties(menuDTO, Menu.class);
        super.updateById(menu);
    }

    @Override
    public void deleteMenu(Long id) throws Exception {
        Menu menu = super.getById(id);
        List<Long> removeIdList = new ArrayList<Long>() {{
            add(menu.getId());
        }};
        List<Long> parentIdList = new ArrayList<Long>() {{
            add(menu.getId());
        }};
        while (!CollectionUtil.isEmpty(parentIdList)) {
            List<Menu> childMenuList = super.list(
                    new LambdaQueryWrapper<Menu>().in(Menu::getParentMenuId, parentIdList));
            if (!CollectionUtil.isEmpty(childMenuList)) {
                parentIdList = childMenuList.stream().map(Menu::getId).collect(Collectors.toList());
                removeIdList.addAll(parentIdList);
            } else {
                parentIdList = null;
            }
        }
        super.removeByIds(removeIdList);
    }

    @Override
    public List<MenuDTO> getMenuTree(Long roleId, String menuName) throws Exception {
        List<Menu> menuList = getMenuList(menuName);
        if (CollectionUtil.isEmpty(menuList)) {
            return Lists.newLinkedList();
        }

        List<MenuDTO> list = BeanUtil.copyToList(menuList, MenuDTO.class);
        //判断角色有没有该菜单
        List<Long> hasRoleMenuIdList = new ArrayList<>();
        if (roleId != null) {
            List<RoleMenu> roleMenuList = roleMenuService.list(
                    new LambdaQueryWrapper<RoleMenu>().eq(RoleMenu::getRoleId, roleId));
            if (!CollectionUtil.isEmpty(roleMenuList)) {
                hasRoleMenuIdList.addAll(roleMenuList.stream().map(RoleMenu::getMenuId).collect(Collectors.toSet()));
            }
        }
        //节点树内含有搜索menuName的父节点集合
        //从根节点往下找
        Map<Long, List<MenuDTO>> menuMap = list.stream().collect(Collectors.groupingBy(MenuDTO::getParentMenuId));
        List<MenuDTO> parentMenu = menuMap.get(OtherInfoConstant.PARENT_MENU_ID);
        //根节点的最大父节点为自己
        List<MenuDTO> result = parentMenu;
        List<MenuDTO> childMenu = new ArrayList<>();
        while (!CollectionUtil.isEmpty(parentMenu)) {
            Iterator<MenuDTO> iter = parentMenu.iterator();
            while (iter.hasNext()){
                MenuDTO parent = iter.next();
                //判断该节点是否在角色中
                if (hasRoleMenuIdList.contains(parent.getId())) {
                    parent.setHasRole(YesOrNoConstant.YES);
                } else {
                    iter.remove();
                    continue;
                }
                List<MenuDTO> child = menuMap.get(parent.getId());
                if (!CollectionUtil.isEmpty(child)) {
                    List<MenuDTO> menuDTOList = parent.getChildMenu();
                    if (CollectionUtil.isEmpty(menuDTOList)) {
                        parent.setChildMenu(child);
                    } else {
                        menuDTOList.addAll(child);
                    }
                    childMenu.addAll(child);
                }
            }
            //子节点边父节点重新递归
            parentMenu = childMenu;
            childMenu = new ArrayList<>();
        }
        return result;
    }

    @Override
    public List<MenuDTO> changeTree(List<MenuDTO> menuList) throws Exception {
        if (CollectionUtil.isEmpty(menuList)) {
            return Lists.newLinkedList();
        }
        Map<Long, List<MenuDTO>> menuMap = menuList.stream()
                .collect(Collectors.groupingBy(MenuDTO::getParentMenuId));
        List<MenuDTO> parentList = menuMap.get(OtherInfoConstant.PARENT_MENU_ID);
        if (CollectionUtil.isEmpty(parentList)) {
            throw new ServiceException("未找到菜单根节点");
        }
        List<MenuDTO> parentMenu = menuMap.get(OtherInfoConstant.PARENT_MENU_ID);
        //根节点的最大父节点为自己
        List<MenuDTO> result = parentMenu;
        List<MenuDTO> childMenu = new ArrayList<>();
        while (!CollectionUtil.isEmpty(parentMenu)) {
            for (MenuDTO parent : parentMenu) {
                List<MenuDTO> child = menuMap.get(parent.getId());
                if (!CollectionUtil.isEmpty(child)) {
                    List<MenuDTO> menuDTOList = parent.getChildMenu();
                    if (CollectionUtil.isEmpty(menuDTOList)) {
                        parent.setChildMenu(child);
                    } else {
                        menuDTOList.addAll(child);
                    }
                    childMenu.addAll(child);
                }
            }
            //子节点边父节点重新递归
            parentMenu = childMenu;
            childMenu = new ArrayList<>();
        }
        return result;
    }

    private List<Menu> getMenuList(String menuName) throws Exception {
        if (StringUtils.isEmpty(menuName)) {
            return super.list();
        }
        List<Menu> menuList = super.list(new LambdaQueryWrapper<Menu>().like(Menu::getMenuName, menuName));
        if (CollectionUtil.isEmpty(menuList)) {
            return Lists.newLinkedList();
        }
        List<Menu> hasParentMenu = menuList.stream()
                .filter(menu -> !menu.getParentMenuId().equals(OtherInfoConstant.PARENT_MENU_ID)).collect(
                        Collectors.toList());
        if (CollectionUtil.isEmpty(hasParentMenu)) {
            return menuList;
        }
        List<Menu> allMenu = hasParentMenu;
        //递归查询父节点
        getAllMent(allMenu, hasParentMenu.stream().map(Menu::getParentMenuId).collect(Collectors.toSet()),
                   allMenu.stream().map(Menu::getId).collect(
                           Collectors.toList()));
        return allMenu.stream().sorted(Comparator.comparing(Menu::getId)).collect(Collectors.toList());
    }

    private void getAllMent(List<Menu> allMenu, Set<Long> parentIdList, List<Long> menuIdList) throws Exception {
        if (CollectionUtil.isEmpty(parentIdList)) {
            return;
        }
        List<Menu> parentList = super.listByIds(parentIdList);
        if (!CollectionUtil.isEmpty(parentList)) {
            Iterator<Menu> iterator = parentList.iterator();
            while (iterator.hasNext()) {
                Menu menu = iterator.next();
                if (menuIdList.contains(menu.getId())) {
                    iterator.remove();
                    continue;
                } else {
                    menuIdList.add(menu.getId());
                }
            }
            if (!CollectionUtil.isEmpty(parentList)) {
                allMenu.addAll(parentList);
                getAllMent(allMenu, parentList.stream().map(Menu::getParentMenuId).collect(Collectors.toSet()),
                           menuIdList);
            }
        }
    }

    private void fillParentInfo(MenuDTO menuDTO) throws Exception {
        if (OtherInfoConstant.PARENT_MENU_ID.equals(menuDTO.getParentMenuId())) {
            menuDTO.setParentMenuId(OtherInfoConstant.PARENT_MENU_ID);
            menuDTO.setParentMenuName(OtherInfoConstant.PARENT_MENU_NAME);
            return;
        }
        if (menuDTO.getParentMenuId() != null) {
            Menu parentMenu = super.getById(menuDTO.getParentMenuId());
            if (parentMenu == null) {
                throw new ServiceException("该父节点菜单不存在");
            }
            menuDTO.setParentMenuName(parentMenu.getMenuName());
        } else {
            menuDTO.setParentMenuId(OtherInfoConstant.PARENT_MENU_ID);
            menuDTO.setParentMenuName(OtherInfoConstant.PARENT_MENU_NAME);
        }
    }

    private void checkMenu(MenuDTO menuDTO) throws Exception {
        if (menuDTO == null) {
            throw new ServiceException("入参不能为空");
        }
        if (StringUtils.isEmpty(menuDTO.getMenuName())) {
            throw new ServiceException("菜单名字不能为空");
        }
        if (StringUtils.isEmpty(menuDTO.getMenuAddress())) {
            throw new ServiceException("菜单地址不能为空");
        }
        if (StringUtils.isEmpty(menuDTO.getComponent())) {
            throw new ServiceException("菜单文件路径不能为空");
        }
    }
}
