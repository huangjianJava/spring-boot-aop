package com.henry.utils;

import com.qiniu.common.QiniuException;
import com.qiniu.common.Zone;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.UploadManager;
import com.qiniu.util.Auth;
import com.qiniu.util.StringMap;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.InputStream;

/**
 * @author huangj
 * @title:
 * @description:
 * @date 2018/12/6
 */
public class QiniuUtil {

    private static final String ACCESS_KEY = "WUpo2KKyda2QHV5jRF3RBgqPA4P56hamBQMpOgWG";

    private static final String SECRET_KEY = "ehW-Czj6A3d3rPj0QPv6vxQzljT8AcBDcjZh0q__";

    private static final String BUCKET = "hcloud-zone";

    private static final String IMAGES_TEST_PATH = "D:\\1.png";

    private static Auth auth = Auth.create(ACCESS_KEY, SECRET_KEY);

    private static String getFileKey() {
        return "images/member/" + String.valueOf(System.currentTimeMillis()) + ".jpg";
    }

    /**
     * 获取上传凭证
     *
     * @return
     */
    private static String getUploadToken() {
        StringMap putPolicy = new StringMap();
        putPolicy.put("returnBody", "{\"key\":\"$(key)\",\"hash\":\"$(etag)\",\"bucket\":\"$(bucket)\",\"fsize\":$(fsize)}");
        long expireSeconds = 3600;
        return auth.uploadToken(BUCKET, null, expireSeconds, putPolicy);
    }

    /**
     * 简单上传
     */
    public static void simpleUpload() {
        // 构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        UploadManager uploadManager = new UploadManager(cfg);
        try {
            Response response = uploadManager.put(IMAGES_TEST_PATH, getFileKey(), getUploadToken());
            if (response.isOK()) {
                String responseInfo = response.bodyString();
                System.out.println("response:" + responseInfo);
            } else {
                System.out.println("上传请求失败");
            }
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
        }
    }

    public static String simpleUpload2(InputStream in, String extension) {
        // 构造一个带指定Zone对象的配置类
        Configuration cfg = new Configuration(Zone.zone2());
        UploadManager uploadManager = new UploadManager(cfg);
        try {
            String fileKey = "images/member/" + String.valueOf(System.currentTimeMillis()) + "." + extension;
            Response response = uploadManager.put(in, fileKey, getUploadToken(), null, null);
            if (!response.isOK()) {
                return "上传失败";
            }
            return response.bodyString();
        } catch (QiniuException ex) {
            Response r = ex.response;
            System.err.println(r.toString());
            try {
                System.err.println(r.bodyString());
            } catch (QiniuException ex2) {
                //ignore
            }
            return "上传失败";
        }
    }


    public static void main(String[] args) throws Exception {
        File file = new File(IMAGES_TEST_PATH);
        String extension = FilenameUtils.getExtension(file.getName());
        InputStream in = FileUtils.openInputStream(file);
        simpleUpload2(in, extension);
        System.out.println("=== 上传完毕 ===");

        /*String fileName = file.getName();
        System.out.println("fileName:" + fileName);

        String extension = FilenameUtils.getExtension(fileName);
        System.out.println("extension:" + extension);*/

        /*QiniuUtil.simpleUpload();
        System.out.println("=== 上传完毕 ===");*/
    }

}











