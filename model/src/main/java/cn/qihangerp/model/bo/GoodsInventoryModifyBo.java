package cn.qihangerp.model.bo;

import lombok.Data;

@Data
public class GoodsInventoryModifyBo {
    private Long id;//库存主键id
    private Integer qty;
    private Integer type;//1 增加库存 2 减少库存
    private String remark;
}
