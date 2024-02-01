package com.qihang.erp.api.domain;

import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.util.Date;

@Data
@TableName("d_keyword")
public class Keyword {
    private String id;
    private String source;
    private String word;
    private String remark;
    private Date date;
    private Date createTime;
}
