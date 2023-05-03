package com.example.toponym.entity.bean;

import com.example.toponym.entity.dto.UserDTO;
import org.springframework.context.ApplicationEvent;

/**
 @author 杨宇帆
 @create 2023-04-29
 */
public class ProcessEvent extends ApplicationEvent {

    /**
     * 流程信息
     */
    private Process process;

    /**
     * 用户信息
     */
    private UserDTO userDTO;

    public ProcessEvent(Object source, Process process, UserDTO userDTO) {
        super(source);
        this.process = process;
        this.userDTO = userDTO;
    }

    public Process getProcess() {
        return process;
    }

    public void setProcess(Process process) {
        this.process = process;
    }

    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }
}
