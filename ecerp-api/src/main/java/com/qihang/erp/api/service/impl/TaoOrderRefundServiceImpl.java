package com.qihang.erp.api.service.impl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;
import java.util.List;

import com.qihang.erp.api.common.EnumResultVo;
import com.qihang.erp.api.domain.*;
import com.qihang.erp.api.mapper.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.PreparedStatementCreator;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Service;
import com.qihang.erp.api.service.ITaoOrderRefundService;
import org.springframework.transaction.annotation.Transactional;

/**
 * 淘宝退款订单Service业务层处理
 * 
 * @author qihang
 * @date 2024-01-13
 */
@Service
public class TaoOrderRefundServiceImpl implements ITaoOrderRefundService 
{
    @Autowired
    private TaoOrderRefundMapper taoOrderRefundMapper;
    @Autowired
    private TaoOrderMapper taoOrderMapper;
    @Autowired
    private GoodsSpecMapper goodsSpecMapper;
    @Autowired
    private ErpOrderReturnedMapper erpOrderReturnedMapper;

    @Autowired
    private ErpOrderMapper erpOrderMapper;

    /**
     * 查询淘宝退款订单
     * 
     * @param id 淘宝退款订单主键
     * @return 淘宝退款订单
     */
    @Override
    public TaoOrderRefund selectTaoOrderRefundById(Long id)
    {
        return taoOrderRefundMapper.selectTaoOrderRefundById(id);
    }

    /**
     * 查询淘宝退款订单列表
     * 
     * @param taoOrderRefund 淘宝退款订单
     * @return 淘宝退款订单
     */
    @Override
    public List<TaoOrderRefund> selectTaoOrderRefundList(TaoOrderRefund taoOrderRefund)
    {
        return taoOrderRefundMapper.selectTaoOrderRefundList(taoOrderRefund);
    }

    /**
     * 新增淘宝退款订单
     * 
     * @param taoOrderRefund 淘宝退款订单
     * @return 结果
     */
    @Transactional
    @Override
    public int insertTaoOrderRefund(TaoOrderRefund taoOrderRefund)
    {
        // 查询
        TaoOrderItem taoOrderItem = taoOrderMapper.selectOrderItemBySubItemIdId(taoOrderRefund.getOid());
        if(taoOrderItem == null) return -1;
        else if(taoOrderItem.getRefundStatus()!=0) return -2;
        TaoOrder taoOrder = taoOrderMapper.selectTaoOrderById(taoOrderItem.getOrderId());
        // 插入数据
        TaoOrderRefund refund = new TaoOrderRefund();
        refund.setRefundId(taoOrderRefund.getRefundId());
        refund.setAfterSalesType(taoOrderRefund.getAfterSalesType());
        refund.setShopId(taoOrder.getShopId());
        refund.setTid(taoOrderRefund.getTid());
        refund.setOid(taoOrderRefund.getOid());
        refund.setRefundFee(taoOrderRefund.getRefundFee());
        refund.setCreated(System.currentTimeMillis() / 1000);
        refund.setStatus("1");
        refund.setNum(taoOrderRefund.getNum());
        refund.setAuditStatus(0L);
        refund.setProductId(taoOrderItem.getProductId());
        refund.setSkuId(taoOrderItem.getSkuId());
        refund.setGoodsTitle(taoOrderItem.getGoodsTitle());
        refund.setGoodsNumber(taoOrderItem.getGoodsNumber());
        refund.setSpecNumber(taoOrderItem.getSpecNumber());
        refund.setProductImgUrl(taoOrderItem.getProductImgUrl());
        refund.setSkuInfo(taoOrderItem.getSkuInfo());
        refund.setCreateTime(new Date());
        refund.setCreateBy(taoOrderRefund.getCreateBy());

        taoOrderRefundMapper.insertTaoOrderRefund(refund);

        // 更新 order_item 状态
        TaoOrderItem up = new TaoOrderItem();
        up.setId(taoOrderItem.getId());
        up.setRefundStatus(1L);
        up.setUpdateBy(taoOrderRefund.getCreateBy());
        up.setUpdateTime(new Date());
        taoOrderMapper.updateTaoOrderItem(up);

        return 1;
    }

    /**
     * 修改淘宝退款订单
     * 
     * @param taoOrderRefund 淘宝退款订单
     * @return 结果
     */
    @Override
    public int confirmRefund(TaoOrderRefund taoOrderRefund)
    {
        TaoOrderRefund refund = taoOrderRefundMapper.selectTaoOrderRefundById(taoOrderRefund.getId());
        if (refund == null) return -1;
        else if(refund.getAuditStatus() != 0) return -2;

        // 查询erp_order_item
        ErpOrderItem select = new ErpOrderItem();
        select.setOrderItemNum(refund.getOid().toString());
        select.setShopId(refund.getShopId().intValue());
        ErpOrderItem erpOrderItem = erpOrderMapper.selectOrderItemByOrderItemNum(select);
        if(erpOrderItem == null) return -21;

//        // 查询erp_goods_spec
//        GoodsSpec goodsSpec = new GoodsSpec();
//        goodsSpec.setSpecNum(taoOrderRefund.getSpecNumber());
//        List<GoodsSpec> goodsSpecs = goodsSpecMapper.selectGoodsSpecList(goodsSpec);
//        if(goodsSpecs==null || goodsSpecs.size() ==0) return -11;

        // 插入到erp_order_returned
        ErpOrderReturned returned = new ErpOrderReturned();
        returned.setReturnedNum(refund.getRefundId());
        if(refund.getAfterSalesType() == 1){
            returned.setReturnedType(1L);
        }else if(refund.getAfterSalesType() == 3){
            returned.setReturnedType(2L);
        }
        returned.setOrderNum(refund.getTid()+"");
        returned.setOrderId(erpOrderItem.getOrderId());
        returned.setOrderItemId(erpOrderItem.getId());
        returned.setShopId(refund.getShopId());
        returned.setShopType(4L);
        returned.setGoodsId(erpOrderItem.getGoodsId());
        returned.setSpecId(erpOrderItem.getSpecId());
        returned.setGoodsNum(refund.getGoodsNumber());
        returned.setSpecNum(refund.getSpecNumber());
        returned.setGoodsName(refund.getGoodsTitle());
        returned.setGoodsSpec(refund.getSkuInfo());
        returned.setGoodsImage(refund.getProductImgUrl());
        returned.setQuantity(refund.getNum());
        returned.setLogisticsCompany(taoOrderRefund.getLogisticsCompany());
        returned.setLogisticsCode(taoOrderRefund.getLogisticsCode());
        returned.setRemark(taoOrderRefund.getRemark());
        returned.setStatus(1L);
        returned.setCreateBy(taoOrderRefund.getUpdateBy());
        returned.setCreateTime(new Date());
        erpOrderReturnedMapper.insertErpOrderReturned(returned);

        // 更新自己
        TaoOrderRefund up = new TaoOrderRefund();
        up.setId(taoOrderRefund.getId());
        up.setAuditStatus(1L);
        up.setAuditTime(new Date());
        up.setUpdateBy(taoOrderRefund.getUpdateBy());
        up.setUpdateTime(new Date());
        up.setLogisticsCompany(taoOrderRefund.getLogisticsCompany());
        up.setLogisticsCode(taoOrderRefund.getLogisticsCode());
        up.setSendTime(taoOrderRefund.getSendTime());
        taoOrderRefundMapper.updateTaoOrderRefund(up);

        return 1;
    }

    /**
     * 批量删除淘宝退款订单
     * 
     * @param ids 需要删除的淘宝退款订单主键
     * @return 结果
     */
    @Override
    public int deleteTaoOrderRefundByIds(Long[] ids)
    {
        return taoOrderRefundMapper.deleteTaoOrderRefundByIds(ids);
    }

    /**
     * 删除淘宝退款订单信息
     * 
     * @param id 淘宝退款订单主键
     * @return 结果
     */
    @Override
    public int deleteTaoOrderRefundById(Long id)
    {
        return taoOrderRefundMapper.deleteTaoOrderRefundById(id);
    }

    @Override
    public Integer updOrderRefund(Long shopId, TaoOrderRefund refund) {
//        try {
//            var order = jdbcTemplate.queryForObject("SELECT * FROM dc_tmall_order_refund WHERE refund_id=? AND shopId=?", new BeanPropertyRowMapper<>(DcTmallOrderRefundEntity.class), ore.getRefund_id(), shopId);
//            //存在，修改
//            String updSQL = "UPDATE dc_tmall_order_refund SET total_fee=?,payment=?,refund_fee=?,modified=?,order_status=?,status=?,good_status=?,has_good_return=?,logisticsCompany=?,logisticsCode=?,num=?,refund_phase=? WHERE id=?";
//            jdbcTemplate.update(updSQL, ore.getTotal_fee(),ore.getPayment(), ore.getRefund_fee(),
//                    ore.getModified(), ore.getOrder_status(), ore.getStatus(), ore.getGood_status(), ore.getHas_good_return(),
//                    ore.getLogisticsCompany(), ore.getLogisticsCode(), ore.getNum(), ore.getRefundPhase(), order.getId());
//            return EnumResultVo.DataExist.getIndex();//数据存在，更新
//        } catch (Exception e) {
//            //不存在，新增
//            String sql = "INSERT INTO dc_tmall_order_refund (" +
//                    "refund_id,tid,oid,buyer_nick,total_fee,payment,refund_fee" +
//                    ",created,modified,order_status,status,good_status,has_good_return" +
//                    ",reason,`desc`,logisticsCompany,logisticsCode,auditStatus,createOn" +
//                    ",num,refund_phase,shopId)" +
//                    " VALUE (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
//
//            jdbcTemplate.update(sql,
//                    ore.getRefund_id(), ore.getTid(), ore.getOid(), ore.getBuyer_nick(), ore.getTotal_fee(),ore.getPayment(), ore.getRefund_fee()
//                    , ore.getCreated(), ore.getModified(), ore.getOrder_status(), ore.getStatus(), ore.getGood_status(), ore.getHas_good_return()
//                    , ore.getReason(), ore.getDesc(), ore.getLogisticsCompany(), ore.getLogisticsCode(), 0, System.currentTimeMillis() / 1000,
//                    ore.getNum(), ore.getRefundPhase(), shopId);
//
//            //退款状态。可选值WAIT_SELLER_AGREE(买家已经申请退款，等待卖家同意)
//            // WAIT_BUYER_RETURN_GOODS(卖家已经同意退款，等待买家退货)
//            // WAIT_SELLER_CONFIRM_GOODS(买家已经退货，等待卖家确认收货)
//            // SELLER_REFUSE_BUYER(卖家拒绝退款)
//            // CLOSED(退款关闭)
//            // SUCCESS(退款成功)
//            if(ore.getStatus().equals("WAIT_BUYER_RETURN_GOODS")) {
//                //卖家已经同意退款，等待买家退货  状态的退款单，插入到数据
//                Long salesOrderId = 0l;
//                try {
//                    var salesOrder = jdbcTemplate.queryForObject("SELECT * FROM erp_sales_order WHERE orderNum=? AND shopId=?", new BeanPropertyRowMapper<>(ErpSalesOrderEntity.class), ore.getTid(), shopId);
//                    salesOrderId = salesOrder.getId();
//                } catch (Exception e11) {
//                }
//
//                /***********************新增rp_sales_order_refund*************************/
//
//                StringBuilder inserSalesOrderRefundSQL = new StringBuilder();
//                inserSalesOrderRefundSQL.append("INSERT INTO erp_sales_order_refund");
//                inserSalesOrderRefundSQL.append(" set refundNum=?,");
//                inserSalesOrderRefundSQL.append(" orderId=?,");
//                inserSalesOrderRefundSQL.append(" orderNum=?,");
//                inserSalesOrderRefundSQL.append(" buyerUserId=?,");
//                inserSalesOrderRefundSQL.append(" refundApplyTime=?,");
//                inserSalesOrderRefundSQL.append(" createOn=?,");
//                inserSalesOrderRefundSQL.append(" status=?,");
//                inserSalesOrderRefundSQL.append(" refundReason=?,");
//                inserSalesOrderRefundSQL.append(" type=?,");
//                inserSalesOrderRefundSQL.append(" orderSaleType=?,");
//                inserSalesOrderRefundSQL.append(" logisticsCompany=?, ");
//                inserSalesOrderRefundSQL.append(" logisticsCode=?, ");
//                inserSalesOrderRefundSQL.append(" shopId=? ");
//
//                //添加平台退货信息：erp_sales_order_refund
//                KeyHolder keyHolder = new GeneratedKeyHolder();
//                Long finalSalesOrderId = salesOrderId;
//                jdbcTemplate.update(new PreparedStatementCreator() {
//                    @Override
//                    public PreparedStatement createPreparedStatement(Connection connection) throws SQLException {
//                        PreparedStatement ps = connection.prepareStatement(inserSalesOrderRefundSQL.toString(), Statement.RETURN_GENERATED_KEYS);
//                        ps.setString(1, ore.getRefund_id());
//                        ps.setLong(2, finalSalesOrderId);
//                        ps.setString(3, String.valueOf(ore.getTid()));//订单编号
//                        ps.setInt(4, 0);//用户id
//                        ps.setLong(5, ore.getCreated());//申请退货时间
//                        ps.setLong(6, System.currentTimeMillis() / 1000);//创建时间
//                        ps.setInt(7, 2);//退款状态 -1取消申请；0拒绝退货；1申请中(待审核)；2等待买家发货；3买家已发货(待收货)；4已收货（完成）
//                        ps.setString(8, ore.getReason());//退货原因
//                        ps.setInt(9, ore.getHas_good_return() == 1 ? 0 : 1);//退款类型0:退货退款，1仅退款
//                        ps.setInt(10, 1);//销售类型0:样品;1:实售
//                        ps.setString(11, ore.getLogisticsCompany());//退款快递公司
//                        ps.setString(12, ore.getLogisticsCode());//退款快递单号
//                        ps.setInt(13,shopId);
//                        return ps;
//                    }
//                }, keyHolder);
//                Long salesOrderRefundId = keyHolder.getKey().longValue();
//
//                /*******添加erp_sales_order_refund_item *******/
//
//                //erp订单itemId
//                Long orderItemId = jdbcTemplate.queryForObject("SELECT IFNULL((select id from erp_sales_order_item where orderId=? and originOrderItemId=? limit 1),0) id ",Long.class,salesOrderId,ore.getOid());
//
//                String addErpSalesOrderRefundItemSQL = "insert erp_sales_order_refund_item set refundId=?,orderId=?,orderItemId=?,quantity=?,buyAmount=?,refundAmount=?";
//
//                jdbcTemplate.update(addErpSalesOrderRefundItemSQL, salesOrderRefundId, salesOrderId, orderItemId, ore.getNum(), ore.getPayment(), ore.getRefund_fee());
//
//
//            }
//            return EnumResultVo.SUCCESS.getIndex();//新增成功
        return EnumResultVo.SUCCESS.getIndex();
    }
}
