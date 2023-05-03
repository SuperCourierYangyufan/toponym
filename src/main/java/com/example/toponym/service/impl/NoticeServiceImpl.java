package com.example.toponym.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.example.toponym.entity.bean.Notice;
import com.example.toponym.entity.bean.User;
import com.example.toponym.entity.bean.UserDetailsInfo;
import com.example.toponym.entity.dto.NoticeDTO;
import com.example.toponym.entity.dto.UserDTO;
import com.example.toponym.entity.query.NoticeQuery;
import com.example.toponym.exception.ServiceException;
import com.example.toponym.mapper.NoticeMapper;
import com.example.toponym.service.FileService;
import com.example.toponym.service.NoticeService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.toponym.service.UserService;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * <p>
 * 通知表,包含政策法规和通知公告 服务实现类
 * </p>
 *
 * @author 杨宇帆
 * @since 2023-04-22
 */
@Service
public class NoticeServiceImpl extends ServiceImpl<NoticeMapper, Notice> implements NoticeService {

    @Autowired
    private FileService fileService;
    @Autowired
    private UserService userService;

    @Override
    public Long addNotice(NoticeDTO noticeDTO) throws Exception {
        checkParam(noticeDTO);
        noticeDTO.setCreateTime(new Date());
        noticeDTO.setUpdateTime(new Date());
        UserDTO currentUser = ((UserDetailsInfo) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).getUser();
        noticeDTO.setCreateUser(currentUser.getId());
        noticeDTO.setUpdateUser(currentUser.getId());
        noticeDTO.setOrganizationId(currentUser.getOrganizationId());
        noticeDTO.setOrganizationName(currentUser.getOrganizationName());
        Notice notice = BeanUtil.copyProperties(noticeDTO, Notice.class);
        super.save(notice);
        return notice.getId();
    }

    @Override
    public void updateNotice(NoticeDTO noticeDTO) throws Exception {
        checkParam(noticeDTO);
        if (noticeDTO.getId() == null) {
            throw new ServiceException("主键不能为空");
        }
        noticeDTO.setUpdateTime(new Date());
        UserDTO currentUser = ((UserDetailsInfo) SecurityContextHolder.getContext().getAuthentication()
                .getPrincipal()).getUser();
        noticeDTO.setUpdateUser(currentUser.getId());
        Notice notice = BeanUtil.copyProperties(noticeDTO, Notice.class);
        super.updateById(notice);
    }

    @Override
    @Transactional(rollbackFor = {ServiceException.class, Exception.class})
    public void deleteNotice(NoticeDTO noticeDTO) throws Exception {
        if (noticeDTO == null || CollectionUtil.isEmpty(noticeDTO.getIdList())) {
            throw new ServiceException("入参集合不能为空");
        }
        List<Notice> list = super.listByIds(noticeDTO.getIdList());
        if (!CollectionUtil.isEmpty(list)) {
            Set<Long> fileIdSet = list.stream().map(Notice::getNoticeFileId)
                    .filter(Objects::nonNull).collect(
                            Collectors.toSet());
            for (Long fileId : fileIdSet) {
                fileService.removeFile(fileId);
            }
            super.removeByIds(noticeDTO.getIdList());
        }
    }

    @Override
    public IPage<NoticeDTO> pageList(NoticeQuery noticeQuery) throws Exception {
        if (noticeQuery == null || noticeQuery.getNoticeCategory() == null) {
            throw new ServiceException("通知类别不能为空");
        }
        List<Long> userIdList = new ArrayList<>();
        if (!StringUtils.isEmpty(noticeQuery.getCreateUserName())) {
            List<User> userList = userService.list(
                    new LambdaQueryWrapper<User>().like(User::getUserName, noticeQuery.getCreateUserName()));
            if (CollectionUtil.isEmpty(userList)) {
                return new Page(noticeQuery.getPageCurrent(), noticeQuery.getPageSize(), 0L);
            }
            userIdList = userList.stream().map(User::getId).collect(Collectors.toList());
        }
        Page page = super.page(new Page<>(noticeQuery.getPageCurrent(), noticeQuery.getPageSize()),
                               new LambdaQueryWrapper<Notice>()
                                       .eq(Notice::getNoticeCategory, noticeQuery.getNoticeCategory())
                                       .eq(noticeQuery.getNoticeType() != null, Notice::getNoticeType,
                                           noticeQuery.getNoticeType())
                                       .like(!StringUtils.isEmpty(noticeQuery.getNoticeName()), Notice::getNoticeName,
                                             noticeQuery.getNoticeName())
                                       .in(!CollectionUtil.isEmpty(userIdList), Notice::getCreateUser, userIdList)
                                       .ge(noticeQuery.getStartTime() != null, Notice::getCreateTime,
                                           noticeQuery.getStartTime())
                                       .le(noticeQuery.getEndTime() != null, Notice::getCreateTime,
                                           noticeQuery.getEndTime()));
        if (page.getTotal() <= 0) {
            return page;
        }
        page.setRecords(BeanUtil.copyToList(page.getRecords(), NoticeDTO.class));
        return page;
    }

    private void checkParam(NoticeDTO noticeDTO) throws Exception {
        if (noticeDTO == null) {
            throw new ServiceException("入参不能为空");
        }
        if (noticeDTO.getNoticeCategory() == null) {
            throw new ServiceException("消息类别不能为空");
        }
        if (noticeDTO.getNoticeType() == null) {
            throw new ServiceException("消息类型不能为空");
        }
        if (StringUtils.isEmpty(noticeDTO.getNoticeName())) {
            throw new ServiceException("通知名称不能为空");
        }
        if (StringUtils.isEmpty(noticeDTO.getNoticeContent())) {
            throw new ServiceException("通知内容不能为空");
        }
    }
}
