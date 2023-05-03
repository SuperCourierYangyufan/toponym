package com.example.toponym.entity.query;

import com.example.toponym.entity.bean.Process;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

/**
 @author 杨宇帆
 @create 2023-04-27
 */
@Data
public class ProcessQuery extends Process {

    @ApiModelProperty(value = "每页数据数量")
    private Long pageSize = 10L;

    @ApiModelProperty(value = "当前页码")
    private Long pageCurrent = 1L;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "是否展示全部审批信息(管理员) 1是,0否")
    private Integer showAll;
}
