package com.example.toponym.entity.dto;

import com.example.toponym.entity.bean.User;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 @author 杨宇帆
 @create 2023-04-13
 */
@Data
public class UserDTO extends User {

    @ApiModelProperty("主键集合")
    private List<Long> idList;

    @ApiModelProperty("角色信息实体")
    private List<RoleDTO> roleList;

    @ApiModelProperty("角色Id列表")
    private List<Long> roleIdList;

    @ApiModelProperty("根据角色Id,和机构Id查询时,返回结果=1表示该用户在已有该角色")
    private Integer hasRole;

    @ApiModelProperty("菜单列表")
    private List<MenuDTO> menuList;

    @ApiModelProperty("手机号验证码")
    private String smsCode;
}
