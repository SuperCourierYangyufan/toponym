package com.example.toponym.controller;


import com.example.toponym.entity.query.OperateLogQuery;
import com.example.toponym.service.OperateLogService;
import com.example.toponym.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * <p>
 * 操作日志表 前端控制器
 * </p>
 *
 * @author 杨宇帆
 * @since 2023-04-26
 */
@Api(tags = "操作日志控制器")
@RestController
@Slf4j
@RequestMapping("/operateLog")
public class OperateLogController {

    @Autowired
    private OperateLogService operateLogService;

    /**
     *  操作日志列表
     * @param operateLogQuery
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "操作日志列表", notes = "操作日志列表")
    @PostMapping("/page")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = false, paramType = "param"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = false, paramType = "param"),
            @ApiImplicitParam(name = "operateName", value = "操作人员名字", required = false, paramType = "param"),
            @ApiImplicitParam(name = "pageSize", value = "每页数据数量", required = false, paramType = "param"),
            @ApiImplicitParam(name = "pageCurrent", value = "当前页码", required = false, paramType = "param")})
    public R page(@RequestBody OperateLogQuery operateLogQuery) throws Exception {
        return R.data(operateLogService.selectPage(operateLogQuery));
    }

}

