package cn.qihangerp.model.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class PddGoodsSkuBo implements Serializable {
    /**
     * 商品数字id
     */
    private String goodsId;
    private String skuId;
    private String outerId;
    private Long erpSkuId;
    private Long shopId;

    private Integer isSkuOnsale;//状态（1售卖中 0已下架）
}
