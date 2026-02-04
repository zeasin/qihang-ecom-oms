package cn.qihangerp.model.query;

import lombok.Data;

@Data
public class SearchBo {
    // 供应商id
    private Integer supplierId;
    private String orderNum;
    private String orderStatus;
    private String startTime;
    private String endTime;
}
