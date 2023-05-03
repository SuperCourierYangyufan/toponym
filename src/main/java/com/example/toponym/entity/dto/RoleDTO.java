package com.example.toponym.entity.dto;

import com.example.toponym.entity.bean.Role;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 @author 杨宇帆
 @create 2023-04-15
 */
@Data
public class RoleDTO extends Role {
    @ApiModelProperty("角色菜单列表")
    private List<MenuDTO> menuDTOList;

    @ApiModelProperty("用户信息列表")
    private List<UserDTO> userDTOList;

    @ApiModelProperty("删除用户角色关联时,传入的用户id列表")
    private List<Long> userIdList;

}
