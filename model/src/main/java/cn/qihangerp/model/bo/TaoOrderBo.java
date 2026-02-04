package cn.qihangerp.model.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class TaoOrderBo implements Serializable {
    private String tid;
    private Long skuId;
    private Long erpGoodsSkuId;
    private Integer shopId;
    private String status;
    private String startTime;
    private String endTime;
}
