package com.example.toponym.service;

import com.example.toponym.entity.bean.File;
import com.baomidou.mybatisplus.extension.service.IService;
import com.example.toponym.entity.dto.FileDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 文件存储表 服务类
 * </p>
 *
 * @author 杨宇帆
 * @since 2023-04-22
 */
public interface FileService extends IService<File> {

    /**
     * 上传文件
     * @param file
     * @return
     * @throws Exception
     */
    FileDTO upload(MultipartFile file) throws Exception;

    /**
     * 删除文件
     * @param fileId
     * @throws Exception
     */
    void removeFile(Long fileId) throws Exception;

    /**
     * base64编码文件
     * @param file
     * @return
     * @throws Exception
     */
    String base64(MultipartFile file) throws Exception;
}
