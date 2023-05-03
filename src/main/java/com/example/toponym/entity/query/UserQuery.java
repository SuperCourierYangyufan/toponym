package com.example.toponym.entity.query;

import com.example.toponym.entity.bean.User;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 @author 杨宇帆
 @create 2023-04-14
 */
@Data
public class UserQuery extends User {

    @ApiModelProperty(value = "每页数据数量")
    private Long pageSize = 10L;

    @ApiModelProperty(value = "当前页码")
    private Long pageCurrent = 1L;
}
