package cn.qihangerp.model.bo;

import lombok.Data;

import java.io.Serializable;

@Data
public class PddGoodsBo implements Serializable {
    /**
     * 商品数字id
     */
    private Long goodsId;
    private String title;
    private String outerGoodsId;
    private Long shopId;

    private Integer isOnsale;//状态（1售卖中 0已下架）
}
