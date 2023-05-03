package com.example.toponym.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.codec.Base64;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileTypeUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.example.toponym.entity.bean.File;
import com.example.toponym.entity.bean.UserDetailsInfo;
import com.example.toponym.entity.dto.FileDTO;
import com.example.toponym.exception.ServiceException;
import com.example.toponym.mapper.FileMapper;
import com.example.toponym.service.FileService;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 文件存储表 服务实现类
 * </p>
 *
 * @author 杨宇帆
 * @since 2023-04-22
 */
@Service
@Slf4j
public class FileServiceImpl extends ServiceImpl<FileMapper, File> implements FileService {

    @Value("${file.upload.path}")
    private String uploadFilePath;

    @Override
    public FileDTO upload(MultipartFile file) throws Exception {
        FileDTO fileDTO = new FileDTO();
        fileDTO.setFileName(file.getOriginalFilename());
        fileDTO.setFileSize(file.getSize());
        fileDTO.setCreateTime(new Date());
        fileDTO.setUpdateTime(new Date());
        fileDTO.setFileType(getFileType(file));
        fileDTO.setCreateUser(
                ((UserDetailsInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser()
                        .getId());
        fileDTO.setUpdateUser(
                ((UserDetailsInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser()
                        .getId());
        //判断目录是否存在并生成文件路径
        String path = checkFileDirectory() + "/" + IdUtil.randomUUID();
        fileDTO.setFilePath(path);
        FileUtil.writeBytes(file.getBytes(), path);
        //保存结果
        File fileBean = BeanUtil.copyProperties(fileDTO, File.class);
        super.save(fileBean);
        fileDTO.setId(fileBean.getId());
        return fileDTO;
    }

    @Override
    public void removeFile(Long fileId) throws Exception {
        File file = super.getById(fileId);
        if (file == null) {
            throw new ServiceException("未找到对应文件信息");
        }
        FileUtil.del(file.getFilePath());
        super.removeById(fileId);
    }

    @Override
    public String base64(MultipartFile file) throws Exception {
        return Base64.encode(file.getBytes());
    }

    private String getFileType(MultipartFile file) throws Exception {
        String type = FileTypeUtil.getType(file.getInputStream());
        if (!StringUtils.isEmpty(type)) {
            return type;
        }
        if (!StringUtils.isEmpty(file.getOriginalFilename())) {
            String[] split = file.getOriginalFilename().split("\\.");
            return split[split.length - 1];
        }
        return null;
    }

    private String checkFileDirectory() throws Exception {
        String path = uploadFilePath + DateUtil.format(DateUtil.date(), "yyyyMMdd");
        FileUtil.mkdir(path);
        return path;
    }
}
