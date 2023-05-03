package com.example.toponym.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.toponym.entity.bean.Process;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.toponym.entity.dto.ProcessDTO;
import com.example.toponym.entity.query.ProcessQuery;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 审核管理表 服务类
 * </p>
 *
 * @author 杨宇帆
 * @since 2023-04-27
 */
public interface ProcessService extends IService<Process> {

    /**
     * 分页列表
     * @param processQuery
     * @return
     * @throws Exception
     */
    IPage<ProcessDTO> pageList(ProcessQuery processQuery) throws Exception;

    /**
     * 执行审批
     * @param processDTO
     * @param request
     * @throws Exception
     */
    void executeProcess(ProcessDTO processDTO, HttpServletRequest request) throws Exception;

    /**
     * 取审批代办,已批准,已驳回数量
     * @return
     * @throws Exception
     */
    List<Map<String,Object>> getProcessStatusCount() throws Exception;

    /**
     * 获取审批详情
     * @return
     * @throws Exception
     * @param id
     */
    Object getProcessDetail(Long id) throws Exception;

    /**
     * 重新提交流程
     * @param id
     * @throws Exception
     */
    void resubmit(Long id) throws Exception;
}
