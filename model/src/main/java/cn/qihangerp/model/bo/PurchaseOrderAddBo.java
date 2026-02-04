package cn.qihangerp.model.bo;

import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 采购订单对象 scm_purchase_order
 * 
 * @author qihang
 * @date 2023-12-29
 */
@Data
public class PurchaseOrderAddBo
{
    private static final long serialVersionUID = 1L;


    /** 供应商id */
    private Long contactId;

    /** 订单编号 */
    private String orderNo;

    /** 订单日期 */
    private Date orderDate;


    /** 订单总金额 */
    private BigDecimal orderAmount;
    private String createBy;

    private List<PurchaseOrderAddItemBo> goodsList;


}
