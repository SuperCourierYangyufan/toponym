package com.example.toponym.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.example.toponym.entity.bean.Notice;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.toponym.entity.dto.NoticeDTO;
import com.example.toponym.entity.query.NoticeQuery;

/**
 * <p>
 * 通知表,包含政策法规和通知公告 服务类
 * </p>
 *
 * @author 杨宇帆
 * @since 2023-04-22
 */
public interface NoticeService extends IService<Notice> {

    /**
     * 新增通知
     * @param noticeDTO
     * @return
     * @throws Exception
     */
    Long addNotice(NoticeDTO noticeDTO) throws Exception;

    /**
     * 更新通知
     * @param noticeDTO
     * @throws Exception
     */
    void updateNotice(NoticeDTO noticeDTO) throws Exception;

    /**
     * 删除通知
     * @param noticeDTO
     * @throws Exception
     */
    void deleteNotice(NoticeDTO noticeDTO) throws Exception;

    /**
     * 分页查询
     * @param noticeQuery
     * @return
     * @throws Exception
     */
    IPage<NoticeDTO> pageList(NoticeQuery noticeQuery) throws Exception;
}
