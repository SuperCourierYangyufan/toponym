package com.example.toponym.controller;


import com.example.toponym.entity.dto.NoticeDTO;
import com.example.toponym.entity.query.NoticeQuery;
import com.example.toponym.service.NoticeService;
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
 * 通知表,包含政策法规和通知公告 前端控制器
 * </p>
 *
 * @author 杨宇帆
 * @since 2023-04-22
 */
@Api(tags = "通知信息(公告管理|政策法规管理|地名文化管理)控制器")
@Slf4j
@RestController
@RequestMapping("/notice")
public class NoticeController {
    @Autowired
    private NoticeService noticeService;

    /**
     * 新增通知
     * @param noticeDTO
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "新增通知", notes = "新增通知")
    @PostMapping("/addNotice")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "noticeCategory", value = "1通知公告,2政策法规,3地名文化管理", required = true, paramType = "body"),
            @ApiImplicitParam(name = "noticeType", value = "类别", required = true, paramType = "body"),
            @ApiImplicitParam(name = "noticeName", value = "通知名称", required = true, paramType = "body"),
            @ApiImplicitParam(name = "noticeContent", value = "通知内容", required = true, paramType = "body"),
            @ApiImplicitParam(name = "noticeFileId", value = "文件id", required = false, paramType = "body"),
    })
    public R addUser(@RequestBody NoticeDTO noticeDTO) throws Exception {
        return R.data(noticeService.addNotice(noticeDTO));
    }



    /**
     * 更新通知
     * @param noticeDTO
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "更新通知", notes = "更新通知")
    @PostMapping("/updateNotice")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "id", value = "主键", required = true, paramType = "body"),
            @ApiImplicitParam(name = "noticeCategory", value = "1通知公告,2政策法规", required = true, paramType = "body"),
            @ApiImplicitParam(name = "noticeType", value = "类别", required = true, paramType = "body"),
            @ApiImplicitParam(name = "noticeName", value = "通知名称", required = true, paramType = "body"),
            @ApiImplicitParam(name = "noticeContent", value = "通知内容", required = true, paramType = "body"),
            @ApiImplicitParam(name = "noticeFileId", value = "文件id", required = false, paramType = "body"),
    })
    public R updateNotice(@RequestBody NoticeDTO noticeDTO) throws Exception {
        noticeService.updateNotice(noticeDTO);
        return R.success();
    }



    /**
     * 获取通知公告管理|政策法规管理页面列表
     * @param noticeQuery
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "获取通知公告管理|政策法规管理页面列表", notes = "获取通知公告管理|政策法规管理页面列表")
    @PostMapping("/page")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "noticeCategory", value = "1通知公告,2政策法规", required = true, paramType = "param"),
            @ApiImplicitParam(name = "noticeType", value = "类别", required = false, paramType = "param"),
            @ApiImplicitParam(name = "noticeName", value = "通知名称", required = false, paramType = "param"),
            @ApiImplicitParam(name = "pageSize", value = "每页数据数量", required = false, paramType = "param"),
            @ApiImplicitParam(name = "pageCurrent", value = "当前页码", required = false, paramType = "param"),
            @ApiImplicitParam(name = "startTime", value = "开始时间", required = false, paramType = "param"),
            @ApiImplicitParam(name = "endTime", value = "结束时间", required = false, paramType = "param"),
            @ApiImplicitParam(name = "createUserName", value = "创建人用户名", required = false, paramType = "param"),

    })
    public R page(@RequestBody NoticeQuery noticeQuery) throws Exception {
        return R.data(noticeService.pageList(noticeQuery));
    }

    /**
     * 删除通知
     * @param noticeDTO
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "删除通知", notes = "删除通知")
    @PostMapping("/deleteNotice")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "idList", value = "主键集合", required = true, paramType = "body"),
    })
    public R deleteNotice(@RequestBody NoticeDTO noticeDTO) throws Exception {
        noticeService.deleteNotice(noticeDTO);
        return R.success();
    }
}

