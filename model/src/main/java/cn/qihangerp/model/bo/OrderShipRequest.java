package cn.qihangerp.model.bo;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderShipRequest {
    private String Id;//订单id
    private Double length;
    private Double width;
    private Double height;
    private Double weight;
    private Double volume;
    private BigDecimal shippingCost;//物流费用
    private BigDecimal packageAmount;//包装费用
    private String shippingCompany;//发货公司
    private String shippingNumber;//发货单号
    private String shippingMan;//发货人
    private String remark;
}
