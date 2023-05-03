package com.example.toponym.entity.dto;

import com.example.toponym.entity.bean.Organization;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 @author 杨宇帆
 @create 2023-04-14
 */
@Data
public class OrganizationDTO extends Organization {

    /**
     * 子节点
     */
    @ApiModelProperty("树列表返回子节点")
    private List<OrganizationDTO> child;
}
