CREATE TABLE "public"."system_file" (
                                        "id" int8 NOT NULL,
                                        "file_name" varchar(255) COLLATE "pg_catalog"."default",
                                        "file_type" varchar(50) COLLATE "pg_catalog"."default",
                                        "file_size" int8,
                                        "file_path" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
                                        "create_time" timestamp(0),
                                        "update_time" timestamp(0),
                                        "create_user" int8,
                                        "update_user" int8,
                                        CONSTRAINT "system_file_pkey" PRIMARY KEY ("id"),
                                        CONSTRAINT "system_file_id_key" UNIQUE ("id"),
                                        CONSTRAINT "system_file_file_path_key" UNIQUE ("file_path")
)
;

ALTER TABLE "public"."system_file"
    OWNER TO "postgres";

COMMENT ON COLUMN "public"."system_file"."id" IS '主键';

COMMENT ON COLUMN "public"."system_file"."file_name" IS '文件名字';

COMMENT ON COLUMN "public"."system_file"."file_type" IS '文件类型';

COMMENT ON COLUMN "public"."system_file"."file_size" IS '文件大小';

COMMENT ON COLUMN "public"."system_file"."file_path" IS '文件路径';

COMMENT ON COLUMN "public"."system_file"."create_time" IS '创建时间';

COMMENT ON COLUMN "public"."system_file"."update_time" IS '更新时间';

COMMENT ON COLUMN "public"."system_file"."create_user" IS '创建用户';

COMMENT ON COLUMN "public"."system_file"."update_user" IS '更新用户';

COMMENT ON TABLE "public"."system_file" IS '文件存储表';

create sequence seq_file increment by 1 minvalue 1 no maxvalue start with 10;
ALTER TABLE public.system_file ALTER COLUMN id SET DEFAULT nextval('seq_file'::regclass);

CREATE TABLE "public"."system_menu" (
                                        "id" int8 NOT NULL,
                                        "menu_name" varchar(100) COLLATE "pg_catalog"."default" NOT NULL,
                                        "menu_address" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
                                        "menu_avatar_icon" varchar(255) COLLATE "pg_catalog"."default",
                                        "menu_description" varchar(255) COLLATE "pg_catalog"."default",
                                        "parent_menu_id" int8 NOT NULL,
                                        "parent_menu_name" varchar(100) COLLATE "pg_catalog"."default",
                                        "create_time" timestamp(0),
                                        "update_time" timestamp(0),
                                        "create_user" int8,
                                        "update_user" int8,
                                        "component" varchar(200) COLLATE "pg_catalog"."default",
                                        CONSTRAINT "system_menu_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "public"."system_menu"
    OWNER TO "postgres";

COMMENT ON COLUMN "public"."system_menu"."id" IS '主键';

COMMENT ON COLUMN "public"."system_menu"."menu_name" IS '菜单名字';

COMMENT ON COLUMN "public"."system_menu"."menu_address" IS '菜单地址';

COMMENT ON COLUMN "public"."system_menu"."menu_avatar_icon" IS '菜单图标';

COMMENT ON COLUMN "public"."system_menu"."menu_description" IS '菜单备注';

COMMENT ON COLUMN "public"."system_menu"."parent_menu_id" IS '父菜单id';

COMMENT ON COLUMN "public"."system_menu"."parent_menu_name" IS '父菜单名字';

COMMENT ON COLUMN "public"."system_menu"."create_time" IS '创建时间';

COMMENT ON COLUMN "public"."system_menu"."update_time" IS '更新时间';

COMMENT ON COLUMN "public"."system_menu"."create_user" IS '创建用户';

COMMENT ON COLUMN "public"."system_menu"."update_user" IS '更新用户';

COMMENT ON COLUMN "public"."system_menu"."component" IS '文件路径';

COMMENT ON TABLE "public"."system_menu" IS '菜单信息';

create sequence seq_menu increment by 1 minvalue 1 no maxvalue start with 10;
ALTER TABLE public.system_menu ALTER COLUMN id SET DEFAULT nextval('seq_menu'::regclass);


CREATE TABLE "public"."system_notice" (
                                          "id" int8 NOT NULL,
                                          "notice_category" int2 NOT NULL,
                                          "notice_type" int2,
                                          "notice_name" varchar(255) COLLATE "pg_catalog"."default",
                                          "notice_content" text COLLATE "pg_catalog"."default",
                                          "organization_id" int8,
                                          "organization_name" varchar(255) COLLATE "pg_catalog"."default",
                                          "create_time" timestamp(0),
                                          "update_time" timestamp(0),
                                          "create_user" int8,
                                          "update_user" int8,
                                          "notice_file_id" int8,
                                          CONSTRAINT "system_notice_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "public"."system_notice"
    OWNER TO "postgres";

COMMENT ON COLUMN "public"."system_notice"."id" IS '主键';

COMMENT ON COLUMN "public"."system_notice"."notice_category" IS '1通知公告,2政策法规';

COMMENT ON COLUMN "public"."system_notice"."notice_type" IS '类别';

COMMENT ON COLUMN "public"."system_notice"."notice_name" IS '通知名称';

COMMENT ON COLUMN "public"."system_notice"."notice_content" IS '通知内容';

COMMENT ON COLUMN "public"."system_notice"."organization_id" IS '机构id';

COMMENT ON COLUMN "public"."system_notice"."organization_name" IS '机构名字';

COMMENT ON COLUMN "public"."system_notice"."create_time" IS '创建时间';

COMMENT ON COLUMN "public"."system_notice"."update_time" IS '更新时间';

COMMENT ON COLUMN "public"."system_notice"."create_user" IS '创建用户';

COMMENT ON COLUMN "public"."system_notice"."update_user" IS '更新用户';

COMMENT ON COLUMN "public"."system_notice"."notice_file_id" IS '文件id';

COMMENT ON TABLE "public"."system_notice" IS '通知表,包含政策法规和通知公告';

create sequence seq_notice increment by 1 minvalue 1 no maxvalue start with 10;
ALTER TABLE public.system_notice ALTER COLUMN id SET DEFAULT nextval('seq_notice'::regclass);



CREATE TABLE "public"."system_operate_log" (
                                               "id" int8 NOT NULL,
                                               "operate_type" int2,
                                               "operate_name" varchar(255) COLLATE "pg_catalog"."default",
                                               "login_ip" varchar(50) COLLATE "pg_catalog"."default",
                                               "create_time" timestamp(0),
                                               "create_user" int8,
                                               "create_name" varchar(20) COLLATE "pg_catalog"."default",
                                               CONSTRAINT "system_operate_log_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "public"."system_operate_log"
    OWNER TO "postgres";

COMMENT ON COLUMN "public"."system_operate_log"."id" IS '主键';

COMMENT ON COLUMN "public"."system_operate_log"."operate_type" IS '操作类型';

COMMENT ON COLUMN "public"."system_operate_log"."operate_name" IS '操作名字';

COMMENT ON COLUMN "public"."system_operate_log"."login_ip" IS '登录ip';

COMMENT ON COLUMN "public"."system_operate_log"."create_time" IS '创建时间';

COMMENT ON COLUMN "public"."system_operate_log"."create_user" IS '创建用户';

COMMENT ON COLUMN "public"."system_operate_log"."create_name" IS '创建用户名称';

COMMENT ON TABLE "public"."system_operate_log" IS '操作日志表';

create sequence seq_operate increment by 1 minvalue 1 no maxvalue start with 10;
ALTER TABLE public.system_operate_log ALTER COLUMN id SET DEFAULT nextval('seq_operate'::regclass);

CREATE TABLE "public"."system_organization" (
                                                "id" int8 NOT NULL,
                                                "organization_name" varchar(255) COLLATE "pg_catalog"."default" NOT NULL,
                                                "organization_level" int2,
                                                "administrative_divisions" varchar(255) COLLATE "pg_catalog"."default",
                                                "parent_id" int8,
                                                "tree_list" varchar(255) COLLATE "pg_catalog"."default",
                                                "create_time" timestamp(0),
                                                "update_time" timestamp(0),
                                                "create_user" int8,
                                                "update_user" int8,
                                                "organization_type" int2,
                                                CONSTRAINT "system_organization_pkey" PRIMARY KEY ("id"),
                                                CONSTRAINT "system_organization_id_key" UNIQUE ("id")
)
;

ALTER TABLE "public"."system_organization"
    OWNER TO "postgres";

COMMENT ON COLUMN "public"."system_organization"."id" IS '主键';

COMMENT ON COLUMN "public"."system_organization"."organization_name" IS '机构名字';

COMMENT ON COLUMN "public"."system_organization"."organization_level" IS '机构';

COMMENT ON COLUMN "public"."system_organization"."administrative_divisions" IS '行政区划';

COMMENT ON COLUMN "public"."system_organization"."parent_id" IS '父机构Id';

COMMENT ON COLUMN "public"."system_organization"."tree_list" IS '机构结构列表,逗号分割';

COMMENT ON COLUMN "public"."system_organization"."create_time" IS '创建时间';

COMMENT ON COLUMN "public"."system_organization"."update_time" IS '更新时间';

COMMENT ON COLUMN "public"."system_organization"."create_user" IS '创建用户';

COMMENT ON COLUMN "public"."system_organization"."update_user" IS '更新用户';

COMMENT ON COLUMN "public"."system_organization"."organization_type" IS '0 外部组织,1 内部组织';

COMMENT ON TABLE "public"."system_organization" IS '组织信息';

create sequence seq_organization increment by 1 minvalue 1 no maxvalue start with 10;
ALTER TABLE public.system_organization ALTER COLUMN id SET DEFAULT nextval('seq_organization'::regclass);

CREATE TABLE "public"."system_role" (
                                        "id" int8 NOT NULL,
                                        "role_name" varchar(100) COLLATE "pg_catalog"."default",
                                        "role_description" varchar(255) COLLATE "pg_catalog"."default",
                                        "role_type" int2,
                                        "create_time" timestamp(0),
                                        "update_time" timestamp(0),
                                        "create_user" int8,
                                        "update_user" int8,
                                        CONSTRAINT "system_role_pkey" PRIMARY KEY ("id"),
                                        CONSTRAINT "system_role_id_key" UNIQUE ("id")
)
;

ALTER TABLE "public"."system_role"
    OWNER TO "postgres";

COMMENT ON COLUMN "public"."system_role"."id" IS '主键';

COMMENT ON COLUMN "public"."system_role"."role_name" IS '角色名';

COMMENT ON COLUMN "public"."system_role"."role_description" IS '角色描述';

COMMENT ON COLUMN "public"."system_role"."role_type" IS ' 角色类别 1正常角色,0外部角色';

COMMENT ON COLUMN "public"."system_role"."create_time" IS '创建时间';

COMMENT ON COLUMN "public"."system_role"."update_time" IS '更新时间';

COMMENT ON COLUMN "public"."system_role"."create_user" IS '创建用户';

COMMENT ON COLUMN "public"."system_role"."update_user" IS '更新用户';

COMMENT ON TABLE "public"."system_role" IS '角色信息';

create sequence seq_role increment by 1 minvalue 1 no maxvalue start with 10;
ALTER TABLE public.system_role ALTER COLUMN id SET DEFAULT nextval('seq_role'::regclass);


CREATE TABLE "public"."system_role_menu" (
                                             "id" int8 NOT NULL,
                                             "menu_id" int8 NOT NULL,
                                             "role_id" int8 NOT NULL,
                                             "create_time" timestamp(0),
                                             "update_time" timestamp(0),
                                             "create_user" int8,
                                             "update_user" int8,
                                             CONSTRAINT "system_role_menu_pkey" PRIMARY KEY ("id"),
                                             CONSTRAINT "system_role_menu_id_key" UNIQUE ("id")
)
;

ALTER TABLE "public"."system_role_menu"
    OWNER TO "postgres";

COMMENT ON COLUMN "public"."system_role_menu"."id" IS '主键';

COMMENT ON COLUMN "public"."system_role_menu"."menu_id" IS '菜单id';

COMMENT ON COLUMN "public"."system_role_menu"."role_id" IS '角色id';

COMMENT ON COLUMN "public"."system_role_menu"."create_time" IS '创建时间';

COMMENT ON COLUMN "public"."system_role_menu"."update_time" IS '更新时间';

COMMENT ON COLUMN "public"."system_role_menu"."create_user" IS '创建用户';

COMMENT ON COLUMN "public"."system_role_menu"."update_user" IS '更新用户';

COMMENT ON TABLE "public"."system_role_menu" IS '角色菜单信息';

create sequence seq_role_menu increment by 1 minvalue 1 no maxvalue start with 10;
ALTER TABLE public.system_role_menu ALTER COLUMN id SET DEFAULT nextval('seq_role_menu'::regclass);


CREATE TABLE "public"."system_user" (
                                        "id" int8 NOT NULL,
                                        "account" varchar(50) COLLATE "pg_catalog"."default",
                                        "user_name" varchar(20) COLLATE "pg_catalog"."default",
                                        "identity_card" varchar(20) COLLATE "pg_catalog"."default",
                                        "office" varchar(50) COLLATE "pg_catalog"."default",
                                        "user_status" int2,
                                        "organization_id" int8,
                                        "organization_name" varchar(100) COLLATE "pg_catalog"."default",
                                        "create_time" timestamp(0),
                                        "update_time" timestamp(0),
                                        "create_user" int8,
                                        "update_user" int8,
                                        "user_type" int2,
                                        "password" varchar(255) COLLATE "pg_catalog"."default",
                                        "avatar_icon" varchar(255) COLLATE "pg_catalog"."default",
                                        "mobile" varchar(20) COLLATE "pg_catalog"."default",
                                        CONSTRAINT "system_user_pkey" PRIMARY KEY ("id"),
                                        CONSTRAINT "system_user_account_key" UNIQUE ("account"),
                                        CONSTRAINT "system_user_id_key" UNIQUE ("id"),
                                        CONSTRAINT "system_user_mobile_key" UNIQUE ("mobile")
)
;

ALTER TABLE "public"."system_user"
    OWNER TO "postgres";

COMMENT ON COLUMN "public"."system_user"."id" IS '主键';

COMMENT ON COLUMN "public"."system_user"."account" IS '账号';

COMMENT ON COLUMN "public"."system_user"."user_name" IS '姓名';

COMMENT ON COLUMN "public"."system_user"."identity_card" IS '身份证号';

COMMENT ON COLUMN "public"."system_user"."office" IS '职务';

COMMENT ON COLUMN "public"."system_user"."user_status" IS '账号状态,0禁用,1正常';

COMMENT ON COLUMN "public"."system_user"."organization_id" IS '组织id';

COMMENT ON COLUMN "public"."system_user"."organization_name" IS '组织名称';

COMMENT ON COLUMN "public"."system_user"."create_time" IS '创建时间';

COMMENT ON COLUMN "public"."system_user"."update_time" IS '更新时间';

COMMENT ON COLUMN "public"."system_user"."create_user" IS '创建用户';

COMMENT ON COLUMN "public"."system_user"."update_user" IS '更新用户';

COMMENT ON COLUMN "public"."system_user"."user_type" IS '用户类型';

COMMENT ON COLUMN "public"."system_user"."password" IS '密码';

COMMENT ON COLUMN "public"."system_user"."avatar_icon" IS '头像图标';

COMMENT ON COLUMN "public"."system_user"."mobile" IS '手机号';

COMMENT ON TABLE "public"."system_user" IS '用户信息';


CREATE TABLE "public"."system_user_role" (
                                             "id" int8 NOT NULL,
                                             "user_id" int8 NOT NULL,
                                             "role_id" int8 NOT NULL,
                                             "create_time" timestamp(0),
                                             "update_time" timestamp(0),
                                             "create_user" int8,
                                             "update_user" int8,
                                             CONSTRAINT "system_user_role_pkey" PRIMARY KEY ("id"),
                                             CONSTRAINT "system_user_role_id_key" UNIQUE ("id"),
                                             CONSTRAINT "system_user_role_user_id_role_id_key" UNIQUE ("user_id", "role_id")
)
;

ALTER TABLE "public"."system_user_role"
    OWNER TO "postgres";

COMMENT ON COLUMN "public"."system_user_role"."id" IS '主键';

COMMENT ON COLUMN "public"."system_user_role"."user_id" IS '用户id';

COMMENT ON COLUMN "public"."system_user_role"."role_id" IS '角色id';

COMMENT ON COLUMN "public"."system_user_role"."create_time" IS '创建时间';

COMMENT ON COLUMN "public"."system_user_role"."update_time" IS '更新时间';

COMMENT ON COLUMN "public"."system_user_role"."create_user" IS '创建用户';

COMMENT ON COLUMN "public"."system_user_role"."update_user" IS '更新用户';

COMMENT ON TABLE "public"."system_user_role" IS '用户角色关联表';

create sequence seq_user_role increment by 1 minvalue 1 no maxvalue start with 10;
ALTER TABLE public.system_user_role ALTER COLUMN id SET DEFAULT nextval('seq_user_role'::regclass);


CREATE TABLE "public"."system_process" (
                                           "id" int8 NOT NULL,
                                           "application_type" int2,
                                           "application_name" varchar(100) COLLATE "pg_catalog"."default",
                                           "application_id" int8,
                                           "process_type" int2,
                                           "process_user_id" int8,
                                           "process_username" varchar(20) COLLATE "pg_catalog"."default",
                                           "process_comment" text COLLATE "pg_catalog"."default",
                                           "create_time" timestamp(0),
                                           "update_time" timestamp(0),
                                           "create_user" int8,
                                           "update_user" int8,
                                           "create_name" varchar(20) COLLATE "pg_catalog"."default",
                                           CONSTRAINT "system_process_pkey" PRIMARY KEY ("id")
)
;

ALTER TABLE "public"."system_process"
    OWNER TO "postgres";

COMMENT ON COLUMN "public"."system_process"."id" IS '主键';

COMMENT ON COLUMN "public"."system_process"."application_type" IS '申请类型';

COMMENT ON COLUMN "public"."system_process"."application_name" IS '申请名字';

COMMENT ON COLUMN "public"."system_process"."application_id" IS '申请类型对应主键';

COMMENT ON COLUMN "public"."system_process"."process_type" IS '流程状态 1待审批 2 审批通过 3审批驳回';

COMMENT ON COLUMN "public"."system_process"."process_user_id" IS '审批人员id';

COMMENT ON COLUMN "public"."system_process"."process_username" IS '审批人员名字';

COMMENT ON COLUMN "public"."system_process"."process_comment" IS '审批意见';

COMMENT ON COLUMN "public"."system_process"."create_time" IS '创建时间';

COMMENT ON COLUMN "public"."system_process"."update_time" IS '更新时间';

COMMENT ON COLUMN "public"."system_process"."create_user" IS '创建用户';

COMMENT ON COLUMN "public"."system_process"."update_user" IS '更新用户';

COMMENT ON COLUMN "public"."system_process"."create_name" IS '创建用户名字';

COMMENT ON TABLE "public"."system_process" IS '审核管理表';


create sequence seq_process increment by 1 minvalue 1 no maxvalue start with 10;
ALTER TABLE public.system_process ALTER COLUMN id SET DEFAULT nextval('seq_process'::regclass);

INSERT INTO "public"."system_organization" ("id", "organization_name", "organization_level", "administrative_divisions", "parent_id", "tree_list", "create_time", "update_time", "create_user", "update_user", "organization_type") VALUES (1, '内部组织', 1, NULL, 1, '', '2023-04-14 18:57:58', '2023-04-14 18:58:00', 1, 1, 1);
INSERT INTO "public"."system_organization" ("id", "organization_name", "organization_level", "administrative_divisions", "parent_id", "tree_list", "create_time", "update_time", "create_user", "update_user", "organization_type") VALUES (2, '外部部组织', 1, NULL, 2, '', '2023-04-14 18:57:58', '2023-04-14 18:58:00', 1, 1, 0);
INSERT INTO "public"."system_role" ("id", "role_name", "role_description", "role_type", "create_time", "update_time", "create_user", "update_user") VALUES (1, '超级管理员', '超级管理员', 1, '2023-04-15 12:32:27', '2023-04-26 21:45:52', 1, 1);
INSERT INTO "public"."system_role" ("id", "role_name", "role_description", "role_type", "create_time", "update_time", "create_user", "update_user") VALUES (2, '访客', '访客', 0, '2023-04-17 20:57:02', '2023-04-17 20:57:05', 1, 1);
INSERT INTO "public"."system_user" ("id", "account", "user_name", "identity_card", "office", "user_status", "organization_id", "organization_name", "create_time", "update_time", "create_user", "update_user", "user_type", "password", "avatar_icon") VALUES (1, 'admin', '超级管理员', NULL, '超级管理员', 1, NULL, NULL, '2023-04-14 13:59:32', '2023-04-14 13:59:35', 1, 1, 1, '$2a$10$un03gWT1OcONlhX1cyy9Zu1/WGvoWFYfzL.Z75gk/8ONhi4se6Q8i', NULL);
INSERT INTO "public"."system_user_role" ("id", "user_id", "role_id", "create_time", "update_time", "create_user", "update_user") VALUES (1, 1, 1, '2023-04-15 12:33:13', '2023-04-15 12:33:15', 1, 1);


