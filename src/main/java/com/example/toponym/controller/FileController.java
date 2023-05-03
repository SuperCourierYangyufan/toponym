package com.example.toponym.controller;


import cn.hutool.core.io.FileUtil;
import com.example.toponym.entity.bean.File;
import com.example.toponym.exception.ServiceException;
import com.example.toponym.service.FileService;
import com.example.toponym.utils.R;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import java.io.OutputStream;
import java.net.URLEncoder;
import javax.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * <p>
 * 文件存储表 前端控制器
 * </p>
 *
 * @author 杨宇帆
 * @since 2023-04-22
 */
@Api(tags = "文件前端控制器")
@Slf4j
@RestController
@RequestMapping("/file")
public class FileController {

    @Autowired
    private FileService fileService;


    /**
     * 上传文件
     * @param file
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "上传文件", notes = "上传文件")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "file", value = "文件", required = true, paramType = "param"),
    })
    @PostMapping("/upload")
    public R upload(@RequestParam(required = true) MultipartFile file) throws Exception {
        return R.data(fileService.upload(file));
    }

    /**
     * base64编码文件
     * @param file
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "base64编码文件", notes = "base64编码文件")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "file", value = "文件", required = true, paramType = "param"),
    })
    @PostMapping("/base64")
    public R base64(@RequestParam(required = true) MultipartFile file) throws Exception {
        return R.data(fileService.base64(file));
    }


    /**
     * 下载文件
     * @param fileId
     * @param response
     * @throws Exception
     */
    @ApiOperation(value = "下载文件", notes = "下载文件")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "fileId", value = "文件Id", required = true, paramType = "param"),
    })
    @GetMapping("/download")
    public void download(@RequestParam(required = true) Long fileId, HttpServletResponse response) throws Exception {
        File file = fileService.getById(fileId);
        if (file == null) {
            throw new ServiceException("该文件不存在");
        }
        byte[] bytes = FileUtil.readBytes(file.getFilePath());
        if (bytes != null) {
            response.addHeader("Content-Disposition",
                               "attachment;filename=" + URLEncoder.encode(file.getFileName(), "UTF-8"));
            response.setContentType("application/octet-stream");
            OutputStream os = response.getOutputStream();
            os.write(bytes);
            os.flush();
            os.close();
        }

    }



    /**
     * 删除文件
     * @param fileId
     * @return
     * @throws Exception
     */
    @ApiOperation(value = "删除文件", notes = "删除文件")
    @ApiImplicitParams(value = {
            @ApiImplicitParam(name = "fileId", value = "文件Id", required = true, paramType = "param"),
    })
    @GetMapping("/remove")
    public R remove(@RequestParam(required = true) Long fileId) throws Exception {
        fileService.removeFile(fileId);
        return R.success();
    }

}

