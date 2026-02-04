package cn.qihangerp.model.bo;

import java.math.BigDecimal;
import java.util.Date;

public class PurchaseOrderOptionBo {
    private Long id;
    private String optionType;//操作类型（audit：审核；confirm：确认（和供应商确认成功）；SupplierShip：供应商发货）
    private String remark;
    private String auditUser;

    private String confirmUser;//采购单确认人

    // 供应商发货
    private Date supplierDeliveryTime;
    private String shipCompany;
    private String shipNo;

    private BigDecimal shipCost;
    private BigDecimal totalAmount;

    private String updateBy;

    public String getConfirmUser() {
        return confirmUser;
    }

    public void setConfirmUser(String confirmUser) {
        this.confirmUser = confirmUser;
    }

    public BigDecimal getShipCost() {
        return shipCost;
    }

    public void setShipCost(BigDecimal shipCost) {
        this.shipCost = shipCost;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public Date getSupplierDeliveryTime() {
        return supplierDeliveryTime;
    }

    public void setSupplierDeliveryTime(Date supplierDeliveryTime) {
        this.supplierDeliveryTime = supplierDeliveryTime;
    }

    public String getShipCompany() {
        return shipCompany;
    }

    public void setShipCompany(String shipCompany) {
        this.shipCompany = shipCompany;
    }

    public String getShipNo() {
        return shipNo;
    }

    public void setShipNo(String shipNo) {
        this.shipNo = shipNo;
    }

    public String getUpdateBy() {
        return updateBy;
    }

    public void setUpdateBy(String updateBy) {
        this.updateBy = updateBy;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getOptionType() {
        return optionType;
    }

    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getAuditUser() {
        return auditUser;
    }

    public void setAuditUser(String auditUser) {
        this.auditUser = auditUser;
    }
}
