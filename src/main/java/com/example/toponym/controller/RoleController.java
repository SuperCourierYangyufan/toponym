package com.example.toponym.controller;


import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.example.toponym.entity.bean.Role;
import com.example.toponym.entity.dto.RoleDTO;
import com.example.toponym.service.RoleService;
import com.example.toponym.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 角色信息 前端控制器
 * </p>
 *
 * @author 杨宇帆
 * @since 2023-04-15
 */
@RequestMapping("/role")
@Api(tags = "角色控制器")
@RestController
@Slf4j
public class RoleController {

    @Autowired
    private RoleService roleService;

    /**
     * 获得所有角色信息
     * @return
     */
    @ApiOperation(value = "获得所有角色信息", notes = "获得所有角色信息")
    @GetMapping("/gerAllRole")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "roleName", value = "角色名称", required = false, paramType = "param"),
    })
    public R gerAllRole(@RequestParam(required = false) String roleName) throws Exception {
        List<Role> roleList = roleService.list(
                new LambdaQueryWrapper<Role>().like(!StringUtils.isEmpty(roleName), Role::getRoleName, roleName));
        return R.data(BeanUtil.copyToList(roleList, RoleDTO.class));
    }


    /**
     * 新增角色
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "新增角色", notes = "新增角色信息")
    @PostMapping("/addRole")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "roleName", value = "角色名称", required = true, paramType = "body"),
            @ApiImplicitParam(name = "roleDescription", value = "角色描述", required = false, paramType = "body"),
            @ApiImplicitParam(name = "roleType", value = "角色类别 1正常角色,0外部角色", required = true, paramType = "body"),
            @ApiImplicitParam(name = "menuDTOList", value = "菜单列表集合,勾选的所有菜单列表,包含勾选的父节点,只需要一层,后台会处理,只需要传入id", required = false, paramType = "body")
    })
    public R addRole(@RequestBody RoleDTO roleDTO) throws Exception {
        return R.data(roleService.addRole(roleDTO));
    }

    /**
     * 更新角色
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "更新角色", notes = "更新角色信息")
    @PostMapping("/updateRole")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "主键", required = true, paramType = "body"),
            @ApiImplicitParam(name = "roleName", value = "角色名称", required = true, paramType = "body"),
            @ApiImplicitParam(name = "roleDescription", value = "角色描述", required = false, paramType = "body"),
            @ApiImplicitParam(name = "roleType", value = "角色类别 1正常角色,0外部角色", required = true, paramType = "body"),
            @ApiImplicitParam(name = "menuDTOList", value = "菜单列表集合,勾选的所有菜单列表,包含勾选的父节点,只需要一层,后台会处理,只需要传入id", required = false, paramType = "body")
    })
    public R updateRole(@RequestBody RoleDTO roleDTO) throws Exception {
        roleService.updateRole(roleDTO);
        return R.success();
    }


    /**
     * 角色绑定用户
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "角色绑定用户", notes = "角色菜单绑定多个用户")
    @PostMapping("/roleAddUserList")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "角色主键", required = true, paramType = "body"),
            @ApiImplicitParam(name = "userDTOList", value = "用户列表集合", required = true, paramType = "body")
    })
    public R roleAddUserList(@RequestBody RoleDTO roleDTO) throws Exception {
        roleService.roleAddUserList(roleDTO);
        return R.success();
    }

    /**
     * 删除角色下的用户列表
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "删除角色下的用户列表", notes = "删除角色下的用户列表")
    @PostMapping("/removeUserRoleInfo")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "roleId", value = "角色的id", required = true, paramType = "body"),
            @ApiImplicitParam(name = "userIdList", value = "用户Id列表集合", required = true, paramType = "body")
    })
    public R removeUserRoleInfo(@RequestBody RoleDTO roleDTO) throws Exception {
        roleService.removeUserRoleInfo(roleDTO);
        return R.success();
    }


    /**
     * 删除角色
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "删除角色", notes = "删除角色")
    @GetMapping("/removeRole")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "角色的id", required = true, paramType = "param"),
    })
    public R removeRole(Long id) throws Exception {
        roleService.removeRole(id);
        return R.success();
    }
}

