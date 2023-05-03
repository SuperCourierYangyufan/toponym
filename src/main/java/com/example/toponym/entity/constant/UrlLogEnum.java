package com.example.toponym.entity.constant;

import org.apache.commons.lang3.StringUtils;

/**
 @author 杨宇帆
 @create 2023-04-26
 */
public enum UrlLogEnum {
    /**
     * 登录
     */
    LOGIN(1, "/api/auth/login", "登录系统"),
    /**
     * 新增用户
     */
    ADD_USER(2, "/api/user/addUser", "新增用户"),
    /**
     * 新增角色
     */
    ADD_ROLE(3, "/api/role/addRole", "新增角色"),
    /**
     * 获取用户列表
     */
    GET_USER_LIST(4, "/api/user/page", "获取用户列表"),
    /**
     * 同意审批
     */
    PROCESS_AGREE(5, "", "同意审批"),
    /**
     * 驳回审批
     */
    PROCESS_OVERRULE(5, "", "驳回审批"),
    ;
    private Integer operateType;
    private String url;
    private String operateName;


    public Integer getOperateType() {
        return operateType;
    }

    public void setOperateType(Integer operateType) {
        this.operateType = operateType;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getOperateName() {
        return operateName;
    }

    public void setOperateName(String operateName) {
        this.operateName = operateName;
    }

    UrlLogEnum(Integer operateType, String url, String operateName) {
        this.operateType = operateType;
        this.url = url;
        this.operateName = operateName;
    }

    public static UrlLogEnum checkUrl(String url) {
        if (StringUtils.isEmpty(url)) {
            return null;
        }
        for (UrlLogEnum urlLogEnum : values()) {
            if (urlLogEnum.getUrl().equals(url)) {
                return urlLogEnum;
            }
        }
        return null;
    }
}
