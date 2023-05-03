package com.example.toponym.listener;

import com.alibaba.fastjson.JSONObject;
import com.example.toponym.entity.bean.Process;
import com.example.toponym.entity.bean.ProcessEvent;
import com.example.toponym.entity.dto.UserDTO;
import com.example.toponym.exception.ServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

/**
 @author 杨宇帆
 @create 2023-04-29
 */
@Slf4j
@Component
public class ProcessListener implements ApplicationListener<ProcessEvent> {

    @Override
    public void onApplicationEvent(ProcessEvent event) {
        Process process = event.getProcess();
        UserDTO userDTO = event.getUserDTO();
        log.info("流程信息:{}", JSONObject.toJSONString(process));
        log.info("用户信息:{}", JSONObject.toJSONString(userDTO));
        switch (process.getApplicationType()){
            case 1:
                //TODO 根据applicationType,applicationId 获取数据做后续逻辑处理
                break;
            case 2:
                //TODO 根据applicationType,applicationId 获取数据做后续逻辑处理
                break;
            default:
                throw new ServiceException("未找到对应应用类型数据,无法处理");
        }
    }
}
