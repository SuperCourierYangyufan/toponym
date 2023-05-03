package com.example.toponym.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.toponym.entity.bean.OperateLog;
import com.example.toponym.entity.bean.Process;
import com.example.toponym.entity.dto.OperateLogDTO;
import com.example.toponym.entity.dto.UserDTO;
import com.example.toponym.entity.query.OperateLogQuery;
import javax.servlet.http.HttpServletRequest;

/**
 * <p>
 * 操作日志表 服务类
 * </p>
 *
 * @author 杨宇帆
 * @since 2023-04-26
 */
public interface OperateLogService extends IService<OperateLog> {

    /**
     * 操作日志列表
     * @param operateLogQuery
     * @return
     * @throws Exception
     */
    IPage<OperateLogDTO> selectPage(OperateLogQuery operateLogQuery) throws Exception;

    /**
     * 添加日志
     * @param request
     * @param userObj
     * @throws Exception
     */
    void addLog(HttpServletRequest request, UserDTO userObj);

    /**
     * 添加登录系统日志
     * @param user
     * @param request
     */
    void addLoginLog(UserDTO user, HttpServletRequest request);

    /**
     * 添加审批日志
     * @param process
     * @param request
     */
    void addProcessLog(Process process, HttpServletRequest request);
}
