package com.example.toponym.mapper;

import com.example.toponym.entity.bean.Process;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 审核管理表 Mapper 接口
 * </p>
 *
 * @author 杨宇帆
 * @since 2023-04-27
 */
public interface ProcessMapper extends BaseMapper<Process> {

    /**
     * 取审批代办,已批准,已驳回数量
     * @return
     * @throws Exception
     */
    List<Map<String, Object>> getProcessStatusCount() throws Exception;
}
