package com.qihang.erp.api.req;

import com.qihang.erp.api.common.PageReq;
import lombok.Data;

import java.util.Date;

@Data
public class KeyWordListReq extends PageReq {
    private String word;
    private String source;
    private String date;
}
