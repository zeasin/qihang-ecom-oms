package com.zhijian.common.utils.file;

import com.zhijian.common.config.MinioConfig;
import com.zhijian.common.utils.DateUtils;
import io.minio.messages.Bucket;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import java.io.InputStream;
import java.util.List;

import io.minio.*;

import static java.io.File.separator;

@Component

public class MinioHelper {
    protected final Logger logger = LoggerFactory.getLogger(this.getClass());
    private MinioClient minioClient;
    private MinioConfig minioConfig;

    public MinioHelper(MinioConfig minioConfig) {
        this.minioConfig = minioConfig;
    }

    @PostConstruct
    public void init() {
        try {
//            minioClient = new MinioClient(minioConfig.getEndpoint(),minioConfig.getAccessKey(),minioConfig.getSecretKey());
            minioClient = MinioClient.builder()
                    .endpoint(minioConfig.getEndpoint())
                    .credentials(minioConfig.getAccessKey(), minioConfig.getSecretKey())
                    .build();
        } catch (Exception e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
    }



    /**
     *  上传图片文件
     * @param basePath  根目录
     * @param filename  文件名
     * @param inputStream 文件流
     * @return  文件全路径
     */
    public ObjectWriteResponse uploadImgFile(String basePath, String filename,InputStream inputStream) {

        String filePath = basePath+DateUtils.datePath()+"/"+filename;
        try {
            PutObjectArgs putObjectArgs = PutObjectArgs.builder()
                    .object(filePath)
                    .contentType(ViewContentType.getContentType(filename))
                    .bucket(minioConfig.getBucketName()).stream(inputStream,inputStream.available(),-1)
                    .build();
            ObjectWriteResponse objectWriteResponse = minioClient.putObject(putObjectArgs);
            return objectWriteResponse;
////            StringBuilder urlPath = new StringBuilder();
//            StringBuilder urlPath = new StringBuilder(minioConfig.getEndpoint());
//            urlPath.append("/"+minioConfig.getBucketName());
////            urlPath.append(separator);
//            urlPath.append(filePath);
//            return urlPath.toString();
        }catch (Exception ex){
            logger.error("minio put file error.",ex);
            throw new RuntimeException("上传文件失败");
        }
    }

    public void previewImage(String objectName, HttpServletResponse response) {
        try {
            GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .object(objectName)
                    .build();
            InputStream inputStream = minioClient.getObject(getObjectArgs);
            if(null == inputStream) return;
            response.setContentType("image/png");
            ServletOutputStream servletOutputStream = response.getOutputStream();
            int len;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer))>0){
                servletOutputStream.write(buffer,0,len);
            }
            servletOutputStream.flush();
            inputStream.close();
            servletOutputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
