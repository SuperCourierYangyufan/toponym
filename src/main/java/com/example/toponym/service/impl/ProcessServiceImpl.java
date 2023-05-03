package com.example.toponym.service.impl;

import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.toponym.entity.bean.Process;
import com.example.toponym.entity.bean.ProcessEvent;
import com.example.toponym.entity.bean.UserDetailsInfo;
import com.example.toponym.entity.constant.ProcessConstant;
import com.example.toponym.entity.constant.YesOrNoConstant;
import com.example.toponym.entity.dto.ProcessDTO;
import com.example.toponym.entity.dto.UserDTO;
import com.example.toponym.entity.query.ProcessQuery;
import com.example.toponym.exception.ServiceException;
import com.example.toponym.mapper.ProcessMapper;
import com.example.toponym.service.OperateLogService;
import com.example.toponym.service.ProcessService;
import java.util.Date;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 审核管理表 服务实现类
 * </p>
 *
 * @author 杨宇帆
 * @since 2023-04-27
 */
@Service
@Slf4j
public class ProcessServiceImpl extends ServiceImpl<ProcessMapper, Process> implements ProcessService {

    @Autowired
    private OperateLogService operateLogService;

    @Autowired
    private ApplicationContext applicationContext;

    @Override
    public IPage<ProcessDTO> pageList(ProcessQuery processQuery) throws Exception {
        if (processQuery.getShowAll() == null) {
            throw new ServiceException("流程可见返回不能为空");
        }
        Page page = super.page(new Page<>(processQuery.getPageCurrent(), processQuery.getPageSize()),
                               new LambdaQueryWrapper<Process>()
                                       .eq(YesOrNoConstant.NO.equals(processQuery.getShowAll()), Process::getCreateUser,
                                           (((UserDetailsInfo) SecurityContextHolder.getContext().getAuthentication()
                                                   .getPrincipal()).getUser()).getId())
                                       .like(!StringUtils.isEmpty(processQuery.getCreateName()), Process::getCreateName,
                                             processQuery.getCreateName())
                                       .eq(processQuery.getApplicationType() != null, Process::getApplicationType,
                                           processQuery.getApplicationType())
                                       .eq(processQuery.getProcessType() != null, Process::getProcessType,
                                           processQuery.getProcessType())
                                       .ge(processQuery.getStartTime() != null, Process::getCreateTime,
                                           processQuery.getStartTime())
                                       .le(processQuery.getEndTime() != null, Process::getCreateTime,
                                           processQuery.getEndTime()));
        if (page.getTotal() <= 0) {
            return page;
        }
        page.setRecords(BeanUtil.copyToList(page.getRecords(), ProcessDTO.class));
        return page;
    }

    @Override
    @Transactional(rollbackFor = {Exception.class, ServiceException.class})
    public void executeProcess(ProcessDTO processDTO, HttpServletRequest request) throws Exception {
        //检查流程
        Process process = checkProcess(processDTO);
        //填充数据
        UserDTO currentUser = ((UserDetailsInfo) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).getUser();
        process.setProcessType(processDTO.getProcessType());
        process.setProcessComment(processDTO.getProcessComment());
        process.setUpdateTime(new Date());
        process.setUpdateUser(currentUser.getId());
        process.setProcessUserId(currentUser.getId());
        process.setProcessUserName(currentUser.getUserName());
        super.updateById(process);
        //添加日志
        operateLogService.addProcessLog(process, request);
        //发送通知,完成后续操作
        applicationContext.publishEvent(new ProcessEvent(this, process, currentUser));
    }

    @Override
    public List<Map<String, Object>> getProcessStatusCount() throws Exception {
        return super.baseMapper.getProcessStatusCount();
    }

    @Override
    public Object getProcessDetail(Long id) throws Exception {
        Process process = super.getById(id);
        if (process == null) {
            throw new ServiceException("该流程信息不存在");
        }
        //TODO 据applicationType,applicationId 获取业务数据返回

        return null;
    }

    @Override
    public void resubmit(Long id) throws Exception {
        Process process = super.getById(id);
        if (process == null) {
            throw new ServiceException("该流程信息不存在");
        }
        if (!ProcessConstant.OVERRULE.equals(process.getProcessType())) {
            throw new ServiceException("流程只有驳回状态才可以重新提交审批");
        }
        super.update(new LambdaUpdateWrapper<Process>()
                                 .eq(Process::getId, id)
                                 .set(Process::getProcessType, ProcessConstant.AWAIT)
                                 .set(Process::getUpdateTime, new Date())
                                 .set(Process::getUpdateUser,
                                      (((UserDetailsInfo) SecurityContextHolder.getContext().getAuthentication()
                                              .getPrincipal()).getUser()).getId())
                                 .set(Process::getProcessUserId, null)
                                 .set(Process::getProcessUserName, null)
                                 .set(Process::getProcessComment, null));
    }

    private Process checkProcess(ProcessDTO processDTO) throws ServiceException {
        if (processDTO.getId() == null) {
            throw new ServiceException("流程id不能为空");
        }
        if (processDTO.getProcessType() == null) {
            throw new ServiceException("流程状态不能为空");
        }
        Process process = super.getById(processDTO.getId());
        if (process == null) {
            throw new ServiceException("该流程不存在");
        }
        if (!ProcessConstant.AWAIT.equals(process.getProcessType())) {
            throw new ServiceException("该流程不是等待审批状态");
        }
        return process;
    }
}
