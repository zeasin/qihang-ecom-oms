package com.qihang.erp.api.service.impl;

import java.beans.Transient;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.LongStream;

import com.qihang.erp.api.domain.*;
import com.qihang.erp.api.domain.bo.PurchaseOrderAddBo;
import com.qihang.erp.api.domain.bo.PurchaseOrderOptionBo;
import com.qihang.erp.api.mapper.*;
import com.zhijian.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qihang.erp.api.service.IScmPurchaseOrderService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 采购订单Service业务层处理
 * 
 * @author qihang
 * @date 2023-12-29
 */
@Service
public class ScmPurchaseOrderServiceImpl implements IScmPurchaseOrderService 
{
    @Autowired
    private ScmPurchaseOrderMapper scmPurchaseOrderMapper;
    @Autowired
    private ScmPurchaseOrderItemMapper scmPurchaseOrderItemMapper;
    @Autowired
    private ScmPurchaseOrderCostMapper costMapper;
    @Autowired
    private ScmPurchaseOrderShipMapper shipMapper;
    @Autowired
    private ScmSupplierMapper  supplierMapper;
    @Autowired
    private FmsPayablePurchaseMapper  fmsPayablePurchaseMapper;

    /**
     * 查询采购订单
     * 
     * @param id 采购订单主键
     * @return 采购订单
     */
    @Override
    public ScmPurchaseOrder selectScmPurchaseOrderById(Long id)
    {
        return scmPurchaseOrderMapper.selectScmPurchaseOrderById(id);
    }

    /**
     * 查询采购订单列表
     * 
     * @param scmPurchaseOrder 采购订单
     * @return 采购订单
     */
    @Override
    public List<ScmPurchaseOrder> selectScmPurchaseOrderList(ScmPurchaseOrder scmPurchaseOrder)
    {
        return scmPurchaseOrderMapper.selectScmPurchaseOrderList(scmPurchaseOrder);
    }

    /**
     * 新增采购订单
     * 
     * @param PurchaseOrderAddBo 采购订单
     * @return 结果
     */
    @Transactional
    @Override
    public int insertScmPurchaseOrder(PurchaseOrderAddBo addBo)
    {
        if(addBo.getGoodsList() == null || addBo.getGoodsList().isEmpty()) return -1;
        // 添加主表
        ScmPurchaseOrder scmPurchaseOrder = new ScmPurchaseOrder();
        scmPurchaseOrder.setOrderNo("PUR"+DateUtils.parseDateToStr("yyyyMMddHHmmss",new Date()));
        scmPurchaseOrder.setOrderAmount(addBo.getOrderAmount());
        scmPurchaseOrder.setCreateTime(DateUtils.getNowDate());
        scmPurchaseOrder.setOrderDate(addBo.getOrderDate());
        scmPurchaseOrder.setContactId(addBo.getContactId());
        scmPurchaseOrder.setOrderTime(System.currentTimeMillis()/1000);
        scmPurchaseOrder.setCreateBy(addBo.getCreateBy());
        scmPurchaseOrder.setStatus(0);
        scmPurchaseOrderMapper.insertScmPurchaseOrder(scmPurchaseOrder);

        // 添加子表
        for (var item:addBo.getGoodsList()) {
            ScmPurchaseOrderItem orderItem = new ScmPurchaseOrderItem();
            orderItem.setOrderDate(addBo.getOrderDate());
            orderItem.setOrderId(scmPurchaseOrder.getId());
            orderItem.setOrderNo(scmPurchaseOrder.getOrderNo());
            orderItem.setAmount(item.getAmount().doubleValue());
            orderItem.setGoodsId(item.getGoodsId());
            orderItem.setGoodsNum(item.getNumber());
            orderItem.setIsDelete(0);
            orderItem.setPrice(item.getPurPrice());
            orderItem.setQuantity(item.getQty());
            orderItem.setSpecId(item.getId());
            orderItem.setSpecNum(item.getSpecNum());
            orderItem.setStatus(0);
            orderItem.setTransType("Purchase");
            orderItem.setGoodsName(item.getName());
            orderItem.setColorValue(item.getColorValue());
            orderItem.setColorImage(item.getColorImage());
            orderItem.setSizeValue(item.getSizeValue());
            orderItem.setStyleValue(item.getStyleValue());

            scmPurchaseOrderItemMapper.insertScmPurchaseOrderItem(orderItem);
        }
        return 1;
    }

    /**
     * 修改采购订单
     * 
     * @param PurchaseOrderOptionBo 采购订单
     * @return 结果
     */
    @Transactional
    @Override
    public int updateScmPurchaseOrder(PurchaseOrderOptionBo bo)
    {
        ScmPurchaseOrder order = scmPurchaseOrderMapper.selectScmPurchaseOrderById(bo.getId());
        if(order == null) return -1;


        if(bo.getOptionType().equals("audit")){
            if(order.getStatus() !=0){
                // 状态不是待审核的
                return -1;
            }
            ScmPurchaseOrder scmPurchaseOrder = new ScmPurchaseOrder();
            scmPurchaseOrder.setId(order.getId());
            scmPurchaseOrder.setUpdateBy(bo.getUpdateBy());
            scmPurchaseOrder.setUpdateTime(DateUtils.getNowDate());
            scmPurchaseOrder.setAuditUser(bo.getAuditUser());
            scmPurchaseOrder.setAuditTime(System.currentTimeMillis()/1000);
            scmPurchaseOrder.setRemark(bo.getRemark());
            scmPurchaseOrder.setStatus(1);
            return scmPurchaseOrderMapper.updateScmPurchaseOrder(scmPurchaseOrder);
        }else if (bo.getOptionType().equals("confirm")) {
            if(order.getStatus() !=1){
                // 状态不是已审核的不能确认
                return -1;
            }
//            // 查询数据
//            ScmPurchaseOrderItem oi = new ScmPurchaseOrderItem();
//            oi.setOrderId(order.getId());
//            List<ScmPurchaseOrderItem> items = scmPurchaseOrderItemMapper.selectScmPurchaseOrderItemList(oi);
//            Map<Long, List<ScmPurchaseOrderItem>> goodsGroup = items.stream().collect(Collectors.groupingBy(x -> x.getGoodsId()));
//            Long total = items.stream().mapToLong(ScmPurchaseOrderItem::getQuantity).sum();
//            // 生成费用信息
//            ScmPurchaseOrderCost cost = new ScmPurchaseOrderCost();
//            cost.setId(order.getId());
//            cost.setOrderNo(order.getOrderNo());
//            cost.setOrderDate(order.getOrderDate());
//            cost.setOrderGoodsUnit(goodsGroup.size());
//            cost.setOrderSpecUnit(items.size());
//            cost.setOrderSpecUnitTotal(total);
//            cost.setOrderAmount(order.getOrderAmount());
//            cost.setActualAmount(bo.getTotalAmount());
//            cost.setFreight(BigDecimal.ZERO);
//            cost.setConfirmUser(bo.getConfirmUser());
//            cost.setConfirmTime(new Date());
//            cost.setCreateBy(bo.getUpdateBy());
//            cost.setPayAmount(BigDecimal.ZERO);
//            cost.setPayCount(0L);
//            cost.setStatus(0L);
//            costMapper.insertScmPurchaseOrderCost(cost);

            // 更新主表
            ScmPurchaseOrder scmPurchaseOrder = new ScmPurchaseOrder();
            scmPurchaseOrder.setId(order.getId());
            scmPurchaseOrder.setUpdateBy(bo.getUpdateBy());
            scmPurchaseOrder.setUpdateTime(DateUtils.getNowDate());
            scmPurchaseOrder.setStatus(101);
            scmPurchaseOrder.setSupplierConfirmTime(new Date());
            scmPurchaseOrderMapper.updateScmPurchaseOrder(scmPurchaseOrder);
        }
        else if (bo.getOptionType().equals("SupplierShip")) {
            if(order.getStatus() !=101){
                // 状态不是已确认的不能发货
                return -1;
            }
            // 查询数据
            ScmPurchaseOrderItem oi = new ScmPurchaseOrderItem();
            oi.setOrderId(order.getId());
            List<ScmPurchaseOrderItem> items = scmPurchaseOrderItemMapper.selectScmPurchaseOrderItemList(oi);
            Map<Long, List<ScmPurchaseOrderItem>> goodsGroup = items.stream().collect(Collectors.groupingBy(x -> x.getGoodsId()));
            Long total = items.stream().mapToLong(ScmPurchaseOrderItem::getQuantity).sum();

            // 生成物流信息
            ScmPurchaseOrderShip ship = new ScmPurchaseOrderShip();
            ship.setId(order.getId());
            ship.setOrderNo(order.getOrderNo());
            ship.setOrderDate(order.getOrderDate());
            ship.setOrderGoodsUnit(goodsGroup.size());
            ship.setOrderSpecUnit(items.size());
            ship.setOrderSpecUnitTotal(total);
            ship.setShipCompany(bo.getShipCompany());
            ship.setShipNo(bo.getShipNo());
            ship.setFreight(bo.getShipCost());
            ship.setShipTime(bo.getSupplierDeliveryTime());
            ship.setCreateBy(bo.getUpdateBy());
            ship.setCreateTime(new Date());
            ship.setStatus(0L);
            ship.setBackCount(0L);
            ship.setStockInCount(0L);
            shipMapper.insertScmPurchaseOrderShip(ship);
//            // 更新费用表
//            ScmPurchaseOrderCost cost = new ScmPurchaseOrderCost();
//            cost.setId(order.getId());
//            cost.setFreight(bo.getShipCost());
//            costMapper.updateScmPurchaseOrderCost(cost);

            ScmSupplier scmSupplier = supplierMapper.selectScmSupplierById(order.getContactId());
            // 生成应付信息fms_payable_purchase
            FmsPayablePurchase fmsPP = new FmsPayablePurchase();
            fmsPP.setSupplierId(order.getContactId());
            fmsPP.setSupplierName(scmSupplier!=null ? scmSupplier.getName():"数据库未找到供应商信息");
            fmsPP.setAmount(order.getOrderAmount().add(bo.getShipCost()));
            fmsPP.setDate(new Date());
            fmsPP.setPurchaseOrderNo(order.getOrderNo());
            fmsPP.setPurchaseDesc("{采购商品总数量:"+total+",不同款式:"+goodsGroup.size()+",不同SKU:"+items.size()+",商品总价:"+order.getOrderAmount()+",运费:"+bo.getShipCost()+"}");
            fmsPP.setStatus(0L);
            fmsPP.setCreateTime(new Date());
            fmsPP.setCreateBy(bo.getUpdateBy());
            fmsPayablePurchaseMapper.insertFmsPayablePurchase(fmsPP);

            // 更新主表
            ScmPurchaseOrder scmPurchaseOrder = new ScmPurchaseOrder();
            scmPurchaseOrder.setId(order.getId());
            scmPurchaseOrder.setUpdateBy(bo.getUpdateBy());
            scmPurchaseOrder.setUpdateTime(DateUtils.getNowDate());
            scmPurchaseOrder.setStatus(102);
            scmPurchaseOrder.setSupplierDeliveryTime(new Date());
            scmPurchaseOrder.setShipAmount(bo.getShipCost());
            scmPurchaseOrderMapper.updateScmPurchaseOrder(scmPurchaseOrder);
        }
        return 1;
    }

    /**
     * 批量删除采购订单
     * 
     * @param ids 需要删除的采购订单主键
     * @return 结果
     */
    @Override
    public int deleteScmPurchaseOrderByIds(Long[] ids)
    {
        return scmPurchaseOrderMapper.deleteScmPurchaseOrderByIds(ids);
    }

    /**
     * 删除采购订单信息
     * 
     * @param id 采购订单主键
     * @return 结果
     */
    @Override
    public int deleteScmPurchaseOrderById(Long id)
    {
        return scmPurchaseOrderMapper.deleteScmPurchaseOrderById(id);
    }
}
