package com.example.toponym.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.toponym.entity.bean.OperateLog;
import com.example.toponym.entity.bean.Process;
import com.example.toponym.entity.constant.ProcessConstant;
import com.example.toponym.entity.constant.UrlLogEnum;
import com.example.toponym.entity.dto.OperateLogDTO;
import com.example.toponym.entity.dto.UserDTO;
import com.example.toponym.entity.query.OperateLogQuery;
import com.example.toponym.mapper.OperateLogMapper;
import com.example.toponym.service.OperateLogService;
import com.example.toponym.utils.IpUtil;
import java.util.Date;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 操作日志表 服务实现类
 * </p>
 *
 * @author 杨宇帆
 * @since 2023-04-26
 */
@Service
@Slf4j
public class OperateLogServiceImpl extends ServiceImpl<OperateLogMapper, OperateLog> implements OperateLogService {

    @Override
    public IPage<OperateLogDTO> selectPage(OperateLogQuery operateLogQuery) throws Exception {
        Page page = super.page(new Page<>(operateLogQuery.getPageCurrent(), operateLogQuery.getPageSize()),
                               new LambdaQueryWrapper<OperateLog>()
                                       .like(!StringUtils.isEmpty(operateLogQuery.getOperateName()),
                                             OperateLog::getOperateName, operateLogQuery.getOperateName())
                                       .ge(operateLogQuery.getStartTime() != null,
                                           OperateLog::getCreateTime, operateLogQuery.getStartTime())
                                       .le(operateLogQuery.getEndTime() != null, OperateLog::getCreateTime,
                                           operateLogQuery.getEndTime()));
        if (page.getTotal() <= 0) {
            return page;
        }
        page.setRecords(BeanUtil.copyToList(page.getRecords(), OperateLogDTO.class));
        return page;
    }

    @Override
    public void addLog(HttpServletRequest request, UserDTO userObj) {
        UrlLogEnum urlLogEnum = UrlLogEnum.checkUrl(request.getRequestURI());
        if (urlLogEnum != null) {
            OperateLog operateLog = new OperateLog();
            operateLog.setCreateUser(userObj.getId());
            operateLog.setCreateName(userObj.getUserName());
            operateLog.setCreateTime(new Date());
            operateLog.setLoginIp(IpUtil.getIpAddr(request));
            operateLog.setOperateType(urlLogEnum.getOperateType());
            operateLog.setOperateName(urlLogEnum.getOperateName());
            super.save(operateLog);
        }

    }

    @Override
    public void addLoginLog(UserDTO user, HttpServletRequest request) {
        OperateLog operateLog = new OperateLog();
        operateLog.setCreateUser(user.getId());
        operateLog.setCreateName(user.getUserName());
        operateLog.setCreateTime(new Date());
        operateLog.setLoginIp(IpUtil.getIpAddr(request));
        operateLog.setOperateType(UrlLogEnum.LOGIN.getOperateType());
        operateLog.setOperateName(UrlLogEnum.LOGIN.getOperateName());
        super.save(operateLog);
    }

    @Override
    public void addProcessLog(Process process, HttpServletRequest request) {
        OperateLog operateLog = new OperateLog();
        operateLog.setCreateUser(process.getUpdateUser());
        operateLog.setCreateName(process.getProcessUserName());
        operateLog.setCreateTime(new Date());
        operateLog.setLoginIp(IpUtil.getIpAddr(request));
        if (ProcessConstant.AGREE.equals(process.getProcessType())) {
            operateLog.setOperateType(UrlLogEnum.PROCESS_AGREE.getOperateType());
            operateLog.setOperateName(UrlLogEnum.PROCESS_AGREE.getOperateName() + ":" + process.getApplicationName());
        } else {
            operateLog.setOperateType(UrlLogEnum.PROCESS_OVERRULE.getOperateType());
            operateLog.setOperateName(
                    UrlLogEnum.PROCESS_OVERRULE.getOperateName() + ":" + process.getApplicationName());
        }
        super.save(operateLog);
    }
}
