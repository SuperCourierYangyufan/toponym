package com.example.toponym.entity.bean;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.activerecord.Model;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import java.util.Date;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 * 审核管理表
 * </p>
 *
 * @author 杨宇帆
 * @since 2023-04-27
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Accessors(chain = true)
@TableName("system_process")
@ApiModel(value="Process对象", description="审核管理表")
public class Process extends Model<Process> {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    @TableId(value = "id",type = IdType.AUTO)
    private Long id;

    @ApiModelProperty(value = "申请类型")
    private Integer applicationType;

    @ApiModelProperty(value = "申请名字")
    private String applicationName;

    @ApiModelProperty(value = "申请类型对应主键")
    private Long applicationId;

    @ApiModelProperty(value = "流程状态 1待审批 2 审批通过 3审批驳回")
    private Integer processType;

    @ApiModelProperty(value = "审批人员id")
    private Long processUserId;

    @ApiModelProperty(value = "审批人员名字")
    private String processUserName;

    @ApiModelProperty(value = "审批意见")
    private String processComment;

    @ApiModelProperty(value = "创建时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "更新时间")
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private Date updateTime;

    @ApiModelProperty(value = "创建用户")
    private Long createUser;

    @ApiModelProperty(value = "更新用户")
    private Long updateUser;

    @ApiModelProperty(value = "创建用户名字")
    private String createName;


    @Override
    protected Serializable pkVal() {
        return this.id;
    }

}
