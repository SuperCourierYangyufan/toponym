package com.example.toponym.controller;


import com.example.toponym.entity.dto.ProcessDTO;
import com.example.toponym.entity.query.ProcessQuery;
import com.example.toponym.service.ProcessService;
import com.example.toponym.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 审核管理表 前端控制器
 * </p>
 *
 * @author 杨宇帆
 * @since 2023-04-27
 */
@Api(tags = "审核管理信息")
@RestController
@Slf4j
@RequestMapping("/process")
public class ProcessController {

    @Autowired
    private ProcessService processService;

    /**
     * 获取审批列表
     * @param processQuery
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取审批列表", notes = "获取审批列表")
    @PostMapping("/page")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "showAll", value = "是否展示全部审批信息(管理员) 1是,0否", required = true, paramType = "body"),
            @ApiImplicitParam(name = "createName", value = "申请人名字", required = false, paramType = "body"),
            @ApiImplicitParam(name = "applicationType", value = "申请类型", required = false, paramType = "body"),
            @ApiImplicitParam(name = "processType", value = "流程状态 1待审批 2 审批通过 3审批驳回", required = false, paramType = "body"),
            @ApiImplicitParam(name = "pageSize", value = "每页数据数量", required = false, paramType = "body"),
            @ApiImplicitParam(name = "pageCurrent", value = "当前页码", required = false, paramType = "body"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = false, paramType = "body"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = false, paramType = "body"),

    })
    public R page(@RequestBody ProcessQuery processQuery) throws Exception {
        return R.data(processService.pageList(processQuery));
    }

    /**
     * 完成审批
     * @param processDTO
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "完成审批", notes = "完成审批")
    @PostMapping("/executeProcess")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "主键", required = true, paramType = "body"),
            @ApiImplicitParam(name = "processType", value = "流程状态 2 审批通过 3审批驳回", required = true, paramType = "body"),
            @ApiImplicitParam(name = "processComment", value = "审批意见", required = false, paramType = "body"),

    })
    public R executeProcess(@RequestBody ProcessDTO processDTO, HttpServletRequest request) throws Exception {
        processService.executeProcess(processDTO, request);
        return R.success();
    }

    /**
     * 取审批代办,已批准,已驳回数量
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取审批代办,已批准,已驳回数量", notes = "取审批代办,已批准,已驳回数量")
    @GetMapping("/getProcessStatusCount")
    @ApiImplicitParams(value = {
    })
    public R getProcessStatusCount() throws Exception {
        return R.data(processService.getProcessStatusCount());
    }

    /**
     * 获取审批详情
     * @param id
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取审批详情[等待后续业务自己补充]", notes = "获取审批详情[等待后续业务自己补充]")
    @GetMapping("/getProcessDetail")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "主键", required = true, paramType = "param"),
    })
    public R getProcessDetail(Long id) throws Exception {
        return R.data(processService.getProcessDetail(id));
    }


    /**
     * 重新提交流程
     * @param id
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "重新提交流程", notes = "重新提交流程")
    @GetMapping("/resubmit")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "主键", required = true, paramType = "param"),
    })
    public R resubmit(Long id) throws Exception {
        processService.resubmit(id);
        return R.success();
    }
}

