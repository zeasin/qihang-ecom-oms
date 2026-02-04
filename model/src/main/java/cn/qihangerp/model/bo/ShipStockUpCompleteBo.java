package cn.qihangerp.model.bo;

import lombok.Data;

import java.util.Date;

@Data
public class ShipStockUpCompleteBo {
    private String stockOutNum;
    private Date completeTime;
    private Long[] ids;
    private String[] orderNums;
}
