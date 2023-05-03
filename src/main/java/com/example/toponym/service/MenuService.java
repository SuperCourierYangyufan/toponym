package com.example.toponym.service;

import com.example.toponym.entity.bean.Menu;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.toponym.entity.dto.MenuDTO;
import java.util.List;

/**
 * <p>
 * 菜单信息 服务类
 * </p>
 *
 * @author 杨宇帆
 * @since 2023-04-15
 */
public interface MenuService extends IService<Menu> {

    /**
     * 新增菜单
     * @param menuDTO
     * @return
     * @throws Exception
     */
    Long addMenu(MenuDTO menuDTO) throws Exception;

    /**
     * 更新菜单
     * @param menuDTO
     * @throws Exception
     */
    void updateMenu(MenuDTO menuDTO) throws Exception;

    /**
     * 删除菜单
     * @param id
     * @throws Exception
     */
    void deleteMenu(Long id) throws Exception;

    /**
     * 获取菜单树
     * @param roleId
     * @param menuName
     * @return
     * @throws Exception
     */
    List<MenuDTO> getMenuTree(Long roleId, String menuName) throws Exception;

    /**
     * 将菜单转为树
     * @param menuDTOList
     * @return
     * @throws Exception
     */
    List<MenuDTO> changeTree(List<MenuDTO> menuDTOList) throws Exception;
}
