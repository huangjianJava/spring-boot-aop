package com.henry.controller;

import com.henry.utils.QiniuUtil;
import org.apache.commons.io.FilenameUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author huangj
 * @title: 文件上传测试例子
 * @description:
 * @date 2018/12/6
 */
@RestController
@RequestMapping("/file")
public class FileController {

    @PostMapping(value = "/images/upload")
    public String upload(@RequestParam(value = "file") MultipartFile file) throws IOException {
        String extension = FilenameUtils.getExtension(file.getOriginalFilename());
        InputStream inputStream = file.getInputStream();
        return QiniuUtil.simpleUpload2(inputStream, extension);
    }

}
