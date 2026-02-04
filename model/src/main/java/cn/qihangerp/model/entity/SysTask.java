package cn.qihangerp.model.entity;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 后台任务配置表
 * @TableName sys_task
 */
@Data
public class SysTask implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 
     */
    private String taskName;

    /**
     * 
     */
    private String cron;

    /**
     * 
     */
    private String method;

    /**
     * 
     */
    private String remark;

    /**
     * 
     */
    private Date createTime;
    private Integer status;

    private static final long serialVersionUID = 1L;
}