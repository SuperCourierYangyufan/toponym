package com.example.toponym.entity.dto;

import com.example.toponym.entity.bean.Menu;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 @author 杨宇帆
 @create 2023-04-15
 */
@Data
public class MenuDTO extends Menu {

    @ApiModelProperty("子节点菜单")
    private List<MenuDTO> childMenu;

    @ApiModelProperty("查询树列表时,若传入角色Id,该值有值,1表示该角色有此菜单,0表示没有")
    private Integer hasRole;
}
