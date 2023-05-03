package com.example.toponym.entity.dto;

import com.example.toponym.entity.bean.Notice;
import io.swagger.annotations.ApiModelProperty;
import java.util.List;
import lombok.Data;

/**
 @author 杨宇帆
 @create 2023-04-22
 */
@Data
public class NoticeDTO extends Notice {
    @ApiModelProperty("主键集合")
    private List<Long> idList;
}
