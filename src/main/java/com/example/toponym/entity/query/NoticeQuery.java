package com.example.toponym.entity.query;

import com.example.toponym.entity.bean.Notice;
import io.swagger.annotations.ApiModelProperty;
import java.util.Date;
import lombok.Data;

/**
 @author 杨宇帆
 @create 2023-04-22
 */
@Data
public class NoticeQuery extends Notice {
    @ApiModelProperty(value = "每页数据数量")
    private Long pageSize = 10L;

    @ApiModelProperty(value = "当前页码")
    private Long pageCurrent = 1L;

    @ApiModelProperty(value = "开始时间")
    private Date startTime;

    @ApiModelProperty(value = "结束时间")
    private Date endTime;

    @ApiModelProperty(value = "创建人用户名搜索")
    private String createUserName;
}
