package cn.qihangerp.api.tao;


import cn.qihangerp.model.entity.TaoOrder;
import cn.qihangerp.model.entity.TaoOrderItem;
import cn.qihangerp.open.tao.response.TaoOrderListResponse;
import org.springframework.util.StringUtils;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class OrderAssembleHelper {
    /**
     * 组织订单
     * @param trade
     * @return
     */
    public static TaoOrder assembleOrder(TaoOrderListResponse trade) {

        TaoOrder order = new TaoOrder();
        order.setTid(trade.getTid());
        order.setTitle(trade.getTitle());
        order.setType(trade.getType());
        order.setSellerFlag(trade.getSeller_flag() + "");
        order.setHasBuyerMessage(trade.isHas_buyer_message() + "");
        order.setCreditCardFee(trade.getCredit_card_fee());
        order.setCreated(trade.getCreated());
        order.setModified(trade.getModified());
        order.setPayTime(trade.getPay_time());
//            order.setPrice(Double.parseDouble(trade.getPrice()));
        try {
            order.setTotalFee(StringUtils.hasText(trade.getTotal_fee()) ? Double.parseDouble(trade.getTotal_fee()) : null);
            order.setAdjustFee(StringUtils.hasText(trade.getAdjust_fee()) ? Double.parseDouble(trade.getAdjust_fee()) : null);
            order.setPostFee(StringUtils.hasText(trade.getPost_fee()) ? BigDecimal.valueOf(Double.parseDouble(trade.getPost_fee())) : null);
            order.setDiscountFee(StringUtils.hasText(trade.getDiscount_fee()) ? BigDecimal.valueOf(Double.parseDouble(trade.getDiscount_fee())) : null);
            order.setPayment(BigDecimal.valueOf(Double.parseDouble(trade.getPayment())));
            order.setReceivedPayment(StringUtils.hasText(trade.getDiscount_fee()) ? BigDecimal.valueOf(Double.parseDouble(trade.getReceived_payment())) : null);
        } catch (Exception ee) {
            ee.printStackTrace();
        }
        order.setBuyerNick(trade.getBuyer_nick());
        order.setBuyerOpenUid(trade.getBuyer_open_uid());
        order.setAlipayNo(trade.getAlipay_no());
//            order.setBuyerAlipayNo(trade.getBuyerAlipayNo());
//            order.setBuyerEmail(trade.getBuyerEmail());
//        order.setBuyerMemo(trade.getBuyerMemo());
//        order.setBuyerMessage(trade.getBuyerMessage());
//        order.setMarkDesc(trade.getMarkDesc());
//        order.setSellerMemo(trade.getSellerMemo());

//        order.setReceiverCountry(trade.getReceiverCountry());
        order.setReceiverState(trade.getReceiver_state());
        order.setReceiverCity(trade.getReceiver_city());
        order.setReceiverDistrict(trade.getReceiver_district());
        order.setReceiverTown(trade.getReceiver_town());
        order.setReceiverAddress(trade.getReceiver_address());
        order.setReceiverName(trade.getReceiver_name());
        order.setReceiverMobile(trade.getReceiver_mobile());
//        order.setReceiverPhone(trade.getReceiverPhone());
        order.setSid(trade.getSid());
//        order.setYfxFee(trade.getYfxFee());
        order.setHasYfx(trade.isHas_yfx() + "");
//            order.setLogisticsInvoiceNo(trade.getinvoice);
//            order.setNumIid(trade.getNumIid() + "");
//            order.setNum(trade.getNum().intValue());
        order.setStatus(trade.getStatus());
//        order.setConsignTime(trade.getConsignTime());
//        order.setEndTime(trade.getEndTime());
        order.setOaid(trade.getOaid());
        List<TaoOrderItem> items = new ArrayList<>();
        for (var item : trade.getOrders()) {
            TaoOrderItem orderItem = new TaoOrderItem();
            orderItem.setTitle(item.getTitle());
                orderItem.setTid(Long.parseLong(trade.getTid()));
                orderItem.setOid(Long.parseLong(item.getOid()));
                orderItem.setTotalFee(StringUtils.hasText(item.getTotalFee()) ? BigDecimal.valueOf(Double.parseDouble(item.getTotalFee())): BigDecimal.valueOf(0));
                orderItem.setDiscountFee(StringUtils.hasText(item.getDiscountFee()) ? BigDecimal.valueOf(Double.parseDouble(item.getDiscountFee())): BigDecimal.valueOf(0));
                orderItem.setAdjustFee(StringUtils.hasText(item.getAdjustFee()) ?BigDecimal.valueOf(Double.parseDouble(item.getAdjustFee())): BigDecimal.valueOf(0));
                orderItem.setDivideOrderFee( StringUtils.hasText(item.getDivideOrderFee()) ? Double.parseDouble(item.getDivideOrderFee()):null);
                orderItem.setPartMjzDiscount(StringUtils.hasText(item.getPartMjzDiscount()) ? Double.parseDouble(item.getPartMjzDiscount()):0.0);
                orderItem.setPayment(StringUtils.hasText(item.getPayment()) ? Double.parseDouble(item.getPayment()):0.0);
                orderItem.setPrice(StringUtils.hasText(item.getPrice()) ? BigDecimal.valueOf(Double.parseDouble(item.getPrice())): BigDecimal.valueOf(0));
                orderItem.setPicPath(item.getPicPath());
                orderItem.setNumIid(Long.parseLong(item.getNumIid()));
                orderItem.setSkuId(item.getSkuId());
//                orderItem.setOuterIid(item.getOuterIid());
                orderItem.setOuterSkuId(item.getOuterSkuId());
                orderItem.setSkuPropertiesName(item.getSkuPropertiesName());
//                orderItem.setItemMealId(item.getItemMealId() == null ? "" : item.getItemMealId() + "");
//                orderItem.setItemMealName(item.getItemMealName());
                orderItem.setNum(item.getNum());
                orderItem.setRefundStatus(item.getRefundStatus());
                orderItem.setStatus(item.getStatus());
//                orderItem.setBuyerRate(item.getBuyerRate() + "");
//                orderItem.setSellerRate(item.getSellerRate() + "");
//                orderItem.setRefundId(item.getRefundId());
                orderItem.setSellerType("");
                orderItem.setCid(item.getCid());
                orderItem.setEndTime(item.getEndTime());
                orderItem.setConsignTime(item.getConsignTime());
                orderItem.setShippingType("");
                orderItem.setLogisticsCompany(item.getLogisticsCompany());
                orderItem.setInvoiceNo(item.getLogisticsCode());

            items.add(orderItem);
        }
        order.setItems(items);
        return order;

    }
}
