package com.zhijian.web.controller.common;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhijian.common.utils.file.MinioHelper;
import com.zhijian.system.domain.SysOss;
import com.zhijian.system.service.ISysOssService;
import io.minio.ObjectWriteResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import com.zhijian.common.config.ZhiJianConfig;
import com.zhijian.common.constant.Constants;
import com.zhijian.common.core.domain.AjaxResult;
import com.zhijian.common.utils.StringUtils;
import com.zhijian.common.utils.file.FileUploadUtils;
import com.zhijian.common.utils.file.FileUtils;
import com.zhijian.core.config.ServerConfig;

/**
 * 通用请求处理
 * 
 * @author qihang
 */
@RestController
@RequestMapping("/common")
public class CommonController
{
    private static final Logger log = LoggerFactory.getLogger(CommonController.class);

    @Autowired
    private ServerConfig serverConfig;
    @Autowired
    private MinioHelper minioHelper;
    @Autowired
    private ISysOssService sysOssService;
    @Value("${minio.endpoint:''}")
    private String endpoint;
    private static final String FILE_DELIMETER = ",";

    /**
     * 通用下载请求
     * 
     * @param fileName 文件名称
     * @param delete 是否删除
     */
    @GetMapping("/download")
    public void fileDownload(String fileName, Boolean delete, HttpServletResponse response, HttpServletRequest request)
    {
        try
        {
            if (!FileUtils.checkAllowDownload(fileName))
            {
                throw new Exception(StringUtils.format("文件名称({})非法，不允许下载。 ", fileName));
            }
            String realFileName = System.currentTimeMillis() + fileName.substring(fileName.indexOf("_") + 1);
            String filePath = ZhiJianConfig.getDownloadPath() + fileName;

            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, realFileName);
            FileUtils.writeBytes(filePath, response.getOutputStream());
            if (delete)
            {
                FileUtils.deleteFile(filePath);
            }
        }
        catch (Exception e)
        {
            log.error("下载文件失败", e);
        }
    }
//    @GetMapping("/preview/image")
//    public void previewImage(HttpServletResponse response){
//        minioHelper.previewImage("","upload/20231208173247.png",response);
//    }

    /**
     * 通用上传请求（单个）
     */
    @PostMapping("/upload")
        public AjaxResult uploadFile(MultipartFile file) throws Exception {
        try {

            // 上传文件路径
//            String filePath = ZhiJianConfig.getUploadPath();
            // 上传并返回新文件名称
//            String fileName = FileUploadUtils.upload(filePath, file);
            String fileName = file.getOriginalFilename();
            String fileSuffix = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
//            InputStream inputStream = multipartToInputStream(file);
            InputStream inputStream = null;
            File file1 = null;
            try {
                // 创建临时文件
                file1 = File.createTempFile("temp", null);
                // 把multipartFile写入临时文件
                file.transferTo(file1);
                // 使用文件创建 inputStream 流
                inputStream = new FileInputStream(file1);

                ObjectWriteResponse resp = minioHelper.uploadImgFile("/images/", fileName, inputStream);
                // TODO: 添加到 sys_oss
                SysOss oss = new SysOss();
                oss.setOriginalName(file.getOriginalFilename());
                oss.setFileName(fileName);
                oss.setFileSuffix(fileSuffix);
                oss.setUrl(endpoint+resp.object());
                oss.setObjectName(resp.object());
                oss.setBucket(resp.bucket());
                oss.setOrderNum(0L);
                oss.setDelFlag("0");
                oss.setCreateBy("");
                oss.setCreateTime(new Date());
                sysOssService.insertSysOss(oss);
                String url = serverConfig.getUrl()+"/preview/images/" + oss.getOssId();
                AjaxResult ajax = AjaxResult.success();
                ajax.put("url", url);
                ajax.put("fileName", fileName);
                ajax.put("newFileName", FileUtils.getName(fileName));
                ajax.put("originalFilename", file.getOriginalFilename());
                return ajax;

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                // 最后记得删除文件
                file1.deleteOnExit();
                // 关闭流
                inputStream.close();
            }
            return AjaxResult.error("上传错误");
        } catch (Exception e) {
            return AjaxResult.error(e.getMessage());
        }

    }



    /**
     * 通用上传请求（多个）
     */
    @PostMapping("/uploads")
    public AjaxResult uploadFiles(List<MultipartFile> files) throws Exception
    {
        try
        {
            // 上传文件路径
            String filePath = ZhiJianConfig.getUploadPath();
            List<String> urls = new ArrayList<String>();
            List<String> fileNames = new ArrayList<String>();
            List<String> newFileNames = new ArrayList<String>();
            List<String> originalFilenames = new ArrayList<String>();
            for (MultipartFile file : files)
            {
                // 上传并返回新文件名称
                String fileName = FileUploadUtils.upload(filePath, file);
                String url = serverConfig.getUrl() + fileName;
                urls.add(url);
                fileNames.add(fileName);
                newFileNames.add(FileUtils.getName(fileName));
                originalFilenames.add(file.getOriginalFilename());
            }
            AjaxResult ajax = AjaxResult.success();
            ajax.put("urls", StringUtils.join(urls, FILE_DELIMETER));
            ajax.put("fileNames", StringUtils.join(fileNames, FILE_DELIMETER));
            ajax.put("newFileNames", StringUtils.join(newFileNames, FILE_DELIMETER));
            ajax.put("originalFilenames", StringUtils.join(originalFilenames, FILE_DELIMETER));
            return ajax;
        }
        catch (Exception e)
        {
            return AjaxResult.error(e.getMessage());
        }
    }

    /**
     * 本地资源通用下载
     */
    @GetMapping("/download/resource")
    public void resourceDownload(String resource, HttpServletRequest request, HttpServletResponse response)
            throws Exception
    {
        try
        {
            if (!FileUtils.checkAllowDownload(resource))
            {
                throw new Exception(StringUtils.format("资源文件({})非法，不允许下载。 ", resource));
            }
            // 本地资源路径
            String localPath = ZhiJianConfig.getProfile();
            // 数据库资源地址
            String downloadPath = localPath + StringUtils.substringAfter(resource, Constants.RESOURCE_PREFIX);
            // 下载名称
            String downloadName = StringUtils.substringAfterLast(downloadPath, "/");
            response.setContentType(MediaType.APPLICATION_OCTET_STREAM_VALUE);
            FileUtils.setAttachmentResponseHeader(response, downloadName);
            FileUtils.writeBytes(downloadPath, response.getOutputStream());
        }
        catch (Exception e)
        {
            log.error("下载文件失败", e);
        }
    }
}
