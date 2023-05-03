package com.example.toponym.entity.constant;

/**
 @author 杨宇帆
 @create 2023-04-13
 */
public class OtherInfoConstant {

    /**
     * 用户默认缓存时间
     */
    public static final Integer CACHE_USER_TOKEN_TIME = 60 * 8;

    /**
     *  jwt超时时间
     */
    public static final Long JWT_OUT_TIME = 60 * 60 * 8 * 1000L;

    /**
     *  jwt超时时间
     */
    public static final String JWT_SECRET = "toponym";

    /**
     * http 默认 token名字
     */
    public static final String HEAD_TOKEN_NAME = "token";

    /**
     * 菜单根节点Id
     */
    public static final Long PARENT_MENU_ID = 0L;

    /**
     * 菜单根节点名字
     */
    public static final String PARENT_MENU_NAME = "根节点";

    /**
     * 外部组织
     */
    public static final Long EXTERNAL_ORGANIZATION = 2L;

    /**
     * 外部角色
     */
    public static final Long EXTERNAL_ROLE = 2L;
}
