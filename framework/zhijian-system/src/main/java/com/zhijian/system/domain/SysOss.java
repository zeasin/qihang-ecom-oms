package com.zhijian.system.domain;

import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 文件对象 sys_oss
 * 
 * @author qihang
 * @date 2024-01-15
 */
public class SysOss extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 文件id */
    private Long ossId;

    /** 文件名 */
    @Excel(name = "文件名")
    private String fileName;

    /** 原名 */
    @Excel(name = "原名")
    private String originalName;

    /** 文件后缀名 */
    @Excel(name = "文件后缀名")
    private String fileSuffix;

    /** URL地址 */
    @Excel(name = "URL地址")
    private String url;

    /** 对象名 */
    @Excel(name = "对象名")
    private String objectName;

    /** 桶名 */
    @Excel(name = "桶名")
    private String bucket;

    /** 显示顺序 */
    @Excel(name = "显示顺序")
    private Long orderNum;

    /** 状态（0正常 1停用） */
    @Excel(name = "状态", readConverterExp = "0=正常,1=停用")
    private String status;

    /** 删除标志（0代表存在 2代表删除） */
    private String delFlag;

    public void setOssId(Long ossId) 
    {
        this.ossId = ossId;
    }

    public Long getOssId() 
    {
        return ossId;
    }
    public void setFileName(String fileName) 
    {
        this.fileName = fileName;
    }

    public String getFileName() 
    {
        return fileName;
    }
    public void setOriginalName(String originalName) 
    {
        this.originalName = originalName;
    }

    public String getOriginalName() 
    {
        return originalName;
    }
    public void setFileSuffix(String fileSuffix) 
    {
        this.fileSuffix = fileSuffix;
    }

    public String getFileSuffix() 
    {
        return fileSuffix;
    }
    public void setUrl(String url) 
    {
        this.url = url;
    }

    public String getUrl() 
    {
        return url;
    }
    public void setObjectName(String objectName) 
    {
        this.objectName = objectName;
    }

    public String getObjectName() 
    {
        return objectName;
    }
    public void setBucket(String bucket) 
    {
        this.bucket = bucket;
    }

    public String getBucket() 
    {
        return bucket;
    }
    public void setOrderNum(Long orderNum) 
    {
        this.orderNum = orderNum;
    }

    public Long getOrderNum() 
    {
        return orderNum;
    }
    public void setStatus(String status) 
    {
        this.status = status;
    }

    public String getStatus() 
    {
        return status;
    }
    public void setDelFlag(String delFlag) 
    {
        this.delFlag = delFlag;
    }

    public String getDelFlag() 
    {
        return delFlag;
    }

}
