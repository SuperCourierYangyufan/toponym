package com.example.toponym.controller;


import com.example.toponym.entity.bean.UserDetailsInfo;
import com.example.toponym.entity.dto.UserDTO;
import com.example.toponym.entity.query.UserQuery;
import com.example.toponym.service.UserService;
import com.example.toponym.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 杨宇帆
 * @since 2023-04-13
 */
@RequestMapping("/user")
@Api(tags = "用户控制器")
@RestController
@Slf4j
public class UserController {

    @Autowired
    private UserService userService;

    /**
     * 新增用户
     * @param userDTO
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "新增用户", notes = "新增用户")
    @PostMapping("/addUser")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "account", value = "账号", required = true, paramType = "body"),
            @ApiImplicitParam(name = "userName", value = "姓名", required = true, paramType = "body"),
            @ApiImplicitParam(name = "identityCard", value = "身份证", required = true, paramType = "body"),
            @ApiImplicitParam(name = "userStatus", value = "账号状态,0禁用,1正常", required = true, paramType = "body"),
            @ApiImplicitParam(name = "organizationId", value = "组织id", required = true, paramType = "body"),
            @ApiImplicitParam(name = "avatarIcon", value = "头像图标", required = false, paramType = "body"),
            @ApiImplicitParam(name = "office", value = "职务", required = false, paramType = "body"),
    })
    public R addUser(@RequestBody UserDTO userDTO) throws Exception {
        return R.data(userService.addUser(userDTO));
    }


    /**
     * 退出
     * @return
     */
    @ApiOperation(value = "退出", notes = "退出")
    @GetMapping("/logout")
    public R logout() throws Exception {
        userService.logout();
        return R.success();
    }

    /**
     * 用户列表
     * @param userQuery
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "用户列表", notes = "用户列表查询")
    @PostMapping("/page")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "account", value = "账号", required = false, paramType = "body"),
            @ApiImplicitParam(name = "userName", value = "姓名", required = false, paramType = "body"),
            @ApiImplicitParam(name = "organizationId", value = "组织id", required = false, paramType = "body"),
            @ApiImplicitParam(name = "pageSize", value = "每页数据数量", required = false, paramType = "body"),
            @ApiImplicitParam(name = "pageCurrent", value = "当前页码", required = false, paramType = "body"),
    })
    public R addUser(@RequestBody UserQuery userQuery) throws Exception {
        return R.data(userService.selectPage(userQuery));
    }


    /**
     * 批量修改用户状态
     * @param UserDTO
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "批量修改用户状态", notes = "批量修改用户状态")
    @PostMapping("/batchChangeUserStatus")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "idList", value = "主键集合", required = true, paramType = "body"),
            @ApiImplicitParam(name = "userStatus", value = "账号状态,0禁用,1正常", required = true, paramType = "body"),
    })
    public R batchChangeUserStatus(@RequestBody UserDTO UserDTO) throws Exception {
        userService.batchChangeUserStatus(UserDTO);
        return R.success();
    }


    /**
     * 角色添加绑定用户页面-根据角色Id,组织id获取用户列表
     * @param roleId
     * @param organizationId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "根据角色Id,组织id获取用户列表", notes = "根据角色Id,组织id获取用户列表")
    @GetMapping("/getUserListByOrganizationIdAndRoleId")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "roleId", value = "角色Id", required = true, paramType = "param"),
            @ApiImplicitParam(name = "organizationId", value = "机构Id", required = true, paramType = "body"),
    })
    public R getUserListByOrganizationIdAndRoleId(@RequestParam(required = true) Long roleId,
                                                  @RequestParam(required = true) Long organizationId) throws Exception {
        return R.data(userService.getUserListByOrganizationIdAndRoleId(roleId, organizationId));
    }


    /**
     * 角色添加绑定用户页面-获取角色下所有用户信息
     * @param roleId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取角色下所有用户", notes = "获取角色下所有用户")
    @GetMapping("/getUserListByRoleId")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "roleId", value = "角色Id", required = true, paramType = "param"),
    })
    public R getUserListByRoleId(@RequestParam(required = true) Long roleId) throws Exception {
        return R.data(userService.getUserListByRoleId(roleId));
    }


    /**
     * 获取当前用户信息
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取当前用户信息", notes = "获取当前用户信息")
    @GetMapping("/getCurrentUser")
    public R getCurrentUser() throws Exception {
        return R.data(((UserDetailsInfo) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).getUser());
    }

}

