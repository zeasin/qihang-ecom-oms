package cn.qihangerp.model.request;

import lombok.Data;

import java.util.List;

@Data
public class StockOutCreateRequest {
    private String outNum;
    private Integer type;
    private String sourceNo;
    private String operator;
    private String remark;
    private List<GoodsSkuInventoryVo> itemList;
}
