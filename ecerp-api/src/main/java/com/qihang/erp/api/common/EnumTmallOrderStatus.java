package com.qihang.erp.api.common;

/**
 * 淘宝开放平台订单状态枚举
 */
public enum EnumTmallOrderStatus {
    /**
     * WAIT_BUYER_PAY：等待买家付款
     * WAIT_SELLER_SEND_GOODS：等待卖家发货
     * SELLER_CONSIGNED_PART：卖家部分发货
     * WAIT_BUYER_CONFIRM_GOODS：等待买家确认收货
     * TRADE_BUYER_SIGNED：买家已签收（货到付款专用）
     * TRADE_FINISHED：交易成功
     * TRADE_CLOSED：交易关闭
     * TRADE_CLOSED_BY_TAOBAO：交易被淘宝关闭
     * TRADE_NO_CREATE_PAY：没有创建外部交易（支付宝交易）
     * WAIT_PRE_AUTH_CONFIRM：余额宝0元购合约中
     * PAY_PENDING：外卡支付付款确认中
     * ALL_WAIT_PAY：所有买家未付款的交易（包含：WAIT_BUYER_PAY、TRADE_NO_CREATE_PAY）
     * ALL_CLOSED：所有关闭的交易（包含：TRADE_CLOSED、TRADE_CLOSED_BY_TAOBAO）
     * PAID_FORBID_CONSIGN，该状态代表订单已付款但是处于禁止发货状态。
     */
    WAIT_BUYER_PAY("等待买家付款", "WAIT_BUYER_PAY", 1L),
    WAIT_SEND_GOODS("等待卖家发货", "WAIT_SELLER_SEND_GOODS", 2L),
    SELLER_CONSIGNED_PART("卖家部分发货", "SELLER_CONSIGNED_PART", 0L),
    WAIT_BUYER_CONFIRM_GOODS("等待买家确认收货", "WAIT_BUYER_CONFIRM_GOODS", 3L),
    TRADE_BUYER_SIGNED("买家已签收（货到付款专用）", "TRADE_BUYER_SIGNED", 22L),
    TRADE_FINISHED("交易成功", "TRADE_FINISHED", 5L),
    TRADE_CLOSED("交易关闭", "TRADE_CLOSED", 11L),
    TRADE_CLOSED_BY_TAOBAO("交易被淘宝关闭", "TRADE_CLOSED_BY_TAOBAO", 11L),
    PAID_FORBID_CONSIGN("订单已付款但是处于禁止发货状态", "PAID_FORBID_CONSIGN", 99L);
    // 成员变量
    private String name;
    private String index;
    private Long status;

    // 构造方法
    private EnumTmallOrderStatus(String name, String index, Long status) {
        this.name = name;
        this.index = index;
        this.status = status;
    }

    // 普通方法
    public static Long getStatus(String index) {
        for (EnumTmallOrderStatus c : EnumTmallOrderStatus.values()) {
            if (c.getIndex().equals(index)) {
                return c.status;
            }
        }
        return null;
    }

    // 普通方法
    public static String getName(String index) {
        for (EnumTmallOrderStatus c : EnumTmallOrderStatus.values()) {
            if (c.getIndex().equals(index)) {
                return c.name;
            }
        }
        return null;
    }
    // 普通方法
    public static String getName(Long status) {
        for (EnumTmallOrderStatus c : EnumTmallOrderStatus.values()) {
            if (c.getStatus() == status) {
                return c.name;
            }
        }
        return null;
    }

    public static String getIndex(String name) {
        for (EnumTmallOrderStatus c : EnumTmallOrderStatus.values()) {
            if (c.toString().equals(name)) {
                return c.getIndex();
            }
        }
        return null;
    }

    public Long getStatus() {
        return status;
    }

    public void setStatus(Long status) {
        this.status = status;
    }

    // get set 方法
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }
}
