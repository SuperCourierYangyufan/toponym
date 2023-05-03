package com.example.toponym.controller;


import com.example.toponym.entity.dto.MenuDTO;
import com.example.toponym.service.MenuService;
import com.example.toponym.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 菜单信息 前端控制器
 * </p>
 *
 * @author 杨宇帆
 * @since 2023-04-15
 */
@Api(tags = "菜单信息")
@RestController
@Slf4j
@RequestMapping("/menu")
public class MenuController {
    @Autowired
    private MenuService menuService;

    /**
     * 新增菜单
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "新增菜单", notes = "新增菜单信息")
    @PostMapping("/addMenu")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "menuName",value = "菜单名字",required = true,paramType = "body"),
            @ApiImplicitParam(name = "menuAddress",value = "菜单地址",required = true,paramType = "body"),
            @ApiImplicitParam(name = "menuAvatarIcon",value = "菜单图标",required = false,paramType = "body"),
            @ApiImplicitParam(name = "menuDescription",value = "菜单备注",required = false,paramType = "body"),
            @ApiImplicitParam(name = "parentMenuId",value = "父菜单id",required = false,paramType = "body"),
            @ApiImplicitParam(name = "component",value = "文件路径",required = true,paramType = "body"),
    })
    public R addMenu(@RequestBody MenuDTO menuDTO) throws Exception {
        return R.data(menuService.addMenu(menuDTO));
    }

    /**
     * 更新菜单
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "更新菜单", notes = "更新菜单信息")
    @PostMapping("/updateMenu")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id",value = "主键",required = true,paramType = "body"),
            @ApiImplicitParam(name = "menuName",value = "菜单名字",required = true,paramType = "body"),
            @ApiImplicitParam(name = "menuAddress",value = "菜单地址",required = true,paramType = "body"),
            @ApiImplicitParam(name = "menuAvatarIcon",value = "菜单图标",required = false,paramType = "body"),
            @ApiImplicitParam(name = "menuDescription",value = "菜单备注",required = false,paramType = "body"),
            @ApiImplicitParam(name = "parentMenuId",value = "父菜单id",required = false,paramType = "body"),
            @ApiImplicitParam(name = "component",value = "文件路径",required = true,paramType = "body"),
    })
    public R updateMenu(@RequestBody MenuDTO menuDTO) throws Exception {
        menuService.updateMenu(menuDTO);
        return R.success();
    }

    /**
     * 删除菜单
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "删除菜单", notes = "删除菜单")
    @GetMapping("/deleteMenu")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id",value = "主键",required = true,paramType = "param"),
    })
    public R deleteMenu(Long id) throws Exception {
        menuService.deleteMenu(id);
        return R.success();
    }


    /**
     * 获取菜单树
     * @param roleId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取菜单树", notes = "获取菜单树")
    @GetMapping("/getMenuTree")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "roleId",value = "角色id",required = false,paramType = "body"),
            @ApiImplicitParam(name = "menuName",value = "菜单名字",required = false,paramType = "body")
    })
    public R getMenuTree(@RequestParam(required = false) Long roleId,
                         @RequestParam(required = false) String menuName) throws Exception {
        return R.data(menuService.getMenuTree(roleId,menuName));
    }
}

