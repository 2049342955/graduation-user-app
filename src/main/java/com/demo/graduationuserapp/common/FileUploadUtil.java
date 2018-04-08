package com.demo.graduationuserapp.common;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class FileUploadUtil {
    //获取ip地址
    @Value("${FTP_ADDRESS}")
    private String FTP_ADDRESS;
    //端口号
    @Value("${FTP_PORT}")
    private String FTP_PORT;
    //用户名
    @Value("${FTP_USERNAME}")
    private String FTP_USERNAME;
    //密码
    @Value("${FTP_PASSWORD}")
    private String FTP_PASSWORD;

    //下载地址地基础url
    @Value("${IMAGE_BASE_URL}")
    private String IMAGE_BASE_URL;

    public Boolean uploadFile(MultipartFile uploadFile, String newName, String basePath) {
        try {
            // 生成一个文件名
            // 获取旧的名字
            String oldName = uploadFile.getOriginalFilename();
            //新名字
//            newName = newName + oldName.substring(oldName.lastIndexOf("."));
            //端口号
            int port = 21;
            System.out.println(basePath);
            //调用方法，上传文件
            boolean result = FtpFileUtil.uploadFile("120.79.143.237", port,
                    "zhftp", "123456", basePath,
                    newName, uploadFile.getInputStream());
            return result;

        } catch (IOException e) {
            return false;
        }
    }
}
