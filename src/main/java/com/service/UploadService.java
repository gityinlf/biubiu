package com.service;

import com.utils.COSUtil;
import com.utils.VideoUtils;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.util.UUID;

public class UploadService {

    public static String upload(File file, String fileName, String tmp_path, String cosPathPrefix) throws Exception {
        //扩展名
        //abc.jpg
        String fileExtend = fileName.substring(fileName.lastIndexOf("."));
        String uploadFileName = UUID.randomUUID().toString() + fileExtend;

        File fileDir = new File(tmp_path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(tmp_path, uploadFileName);

        FileUtils.copyFile(file, targetFile);//复制到服务器
        
        File cosFile = new File(cosPathPrefix,uploadFileName);
        FileUtils.copyFile(file,cosFile);

        /****Cos注释
        //Boolean aBoolean = COSUtil.uploadFile(cosPathPrefix + uploadFileName, targetFile.getAbsolutePath());
        //if (!aBoolean) {
        //    throw new Exception("COS上传失败");
        //}
        //已经上传到Cos
        //targetFile.delete();


        //  /img/ -> img/
        //return cosPathPrefix.substring(1, cosPathPrefix.length()) + targetFile.getName();、
         ***/
        return targetFile.getName();
    }


    public static String extractAndUplaod(File video, String thumbnailExtend, String tmp_path, String cosPathPrefix) throws Exception {
        String uploadFileName = UUID.randomUUID().toString() + thumbnailExtend;
        File fileDir = new File(tmp_path);
        if (!fileDir.exists()) {
            fileDir.setWritable(true);
            fileDir.mkdirs();
        }
        File targetFile = new File(tmp_path, uploadFileName);

        boolean extractOk = VideoUtils.extractThumbnail(video, targetFile.getAbsolutePath());
        if (extractOk) {
            System.out.println("提取成功");
            
            File cosFile = new File(cosPathPrefix,uploadFileName);
            FileUtils.copyFile(targetFile,cosFile);
            /* Cos 注释掉
            //Boolean aBoolean = COSUtil.uploadFile(cosPathPrefix + uploadFileName, targetFile.getAbsolutePath());
            //if (!aBoolean) {
            //    throw new Exception("COS上传失败");
            //}
            */
        } else {
        	System.out.println(targetFile.getAbsolutePath());
            throw new Exception("提取失败");
        }

        return targetFile.getName();
        /* Cos 注释掉
        //已经上传到Cos
        //targetFile.delete();
        //return cosPathPrefix.substring(1, cosPathPrefix.length()) + targetFile.getName();
        */
    }
}
