package cn.qihangerp.module.service.impl;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.common.utils.DateUtils;
import cn.qihangerp.mapper.goods.OGoodsInventoryBatchMapper;
import cn.qihangerp.mapper.goods.OGoodsInventoryMapper;
import cn.qihangerp.mapper.goods.OGoodsSkuMapper;
import cn.qihangerp.model.bo.ShipStockUpBo;
import cn.qihangerp.model.bo.ShipStockUpCompleteBo;
import cn.qihangerp.model.entity.*;
import cn.qihangerp.mapper.OOrderShipListMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.module.service.OOrderShipListItemService;
import cn.qihangerp.mapper.OOrderShipListItemMapper;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;

/**
* @author qilip
* @description 针对表【o_order_ship_list_item(发货-备货表（打单加入备货清单）)】的数据库操作Service实现
* @createDate 2025-06-01 23:07:24
*/
@Slf4j
@AllArgsConstructor
@Service
public class OOrderShipListItemServiceImpl extends ServiceImpl<OOrderShipListItemMapper, OOrderShipListItem>
    implements OOrderShipListItemService{
    private final OOrderShipListMapper oOrderShipListMapper;
    private final OGoodsSkuMapper goodsSkuMapper;
    private final OGoodsInventoryMapper goodsInventoryMapper;
    private final OGoodsInventoryBatchMapper goodsInventoryBatchMapper;

    @Override
    public PageResult<OOrderShipListItem> queryPageList(ShipStockUpBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<OOrderShipListItem> queryWrapper = new LambdaQueryWrapper<OOrderShipListItem>()
                .eq(bo.getShopId()!=null,OOrderShipListItem::getShopId,bo.getShopId())
                .eq(bo.getStatus()!=null,OOrderShipListItem::getStatus,bo.getStatus())
                .eq(StringUtils.hasText(bo.getOrderNum()),OOrderShipListItem::getOrderNum,bo.getOrderNum())
                ;
        Page<OOrderShipListItem> pages = this.baseMapper.selectPage(pageQuery.build(), queryWrapper);

        return PageResult.build(pages);
    }

    @Override
    public PageResult<OOrderShipListItem> queryWarehousePageList(ShipStockUpBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<OOrderShipListItem> queryWrapper = new LambdaQueryWrapper<OOrderShipListItem>()
                .eq(OOrderShipListItem::getShipper,0)
                .eq(bo.getShopId()!=null,OOrderShipListItem::getShopId,bo.getShopId())
                .eq(bo.getStatus()!=null,OOrderShipListItem::getStatus,bo.getStatus())
                .eq(StringUtils.hasText(bo.getOrderNum()),OOrderShipListItem::getOrderNum,bo.getOrderNum())
                ;
        Page<OOrderShipListItem> pages = this.baseMapper.selectPage(pageQuery.build(), queryWrapper);

        return PageResult.build(pages);
    }
    /**
     * 备货完成
     * @param bo
     * @return
     */
    @Override
    public int stockUpComplete(ShipStockUpCompleteBo bo) {

        if(bo.getIds() == null || bo.getIds().length == 0) return -1;

        int total=0;
        // 循环判断状态
        for (Long id:bo.getIds()) {
            OOrderShipListItem up = this.baseMapper.selectById(id);
            if (up != null) {
                if (up.getStatus() == 0 || up.getStatus() == 1) {
                    OOrderShipListItem update = new OOrderShipListItem();
                    update.setId(id);
                    update.setStatus(2);//备货完成
                    update.setUpdateBy("备货完成");
                    update.setUpdateTime(new Date());
                    this.baseMapper.updateById(update);
                }
            }
        }

        return 1;
    }

    /**
     * 备货完成 by Order
     * @param
     * @return
     */
//    @Override
//    public int stockUpCompleteByOrder(ShipStockUpCompleteBo bo) {
//
//        if(bo.getOrderNums() == null || bo.getOrderNums().length == 0) return -1;
//
//        int total=0;
//        // 循环判断状态
//        for (String orderNum:bo.getOrderNums()) {
//            List<OOrderShipListItem> upList = this.baseMapper.selectList(new LambdaQueryWrapper<OOrderShipListItem>()
//                    .eq(OOrderShipListItem::getOrderNum,orderNum));
//            if (upList != null) {
//                for(OOrderShipListItem up : upList) {
//                    if (up.getStatus() == 0 || up.getStatus() == 1) {
//                        OOrderShipListItem update = new OOrderShipListItem();
//                        update.setId(up.getId());
//                        update.setStatus(2);//备货完成
//                        update.setUpdateBy("备货完成");
//                        update.setUpdateTime(new Date());
//                        this.baseMapper.updateById(update);
//                    }
//                }
//            }
//        }
//
//        return 1;
//    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo<Long> stockUpCompleteByOrder(Long shipOrderId) {
        OOrderShipList oOrderShipList = oOrderShipListMapper.selectById(shipOrderId);
        if(oOrderShipList==null) return ResultVo.error("发货单不存在");
        else if (oOrderShipList.getStatus()==3) {
            return ResultVo.error("发货单已出库");
        }
        List<OOrderShipListItem> oOrderShipListItems = this.baseMapper.selectList(new LambdaQueryWrapper<OOrderShipListItem>().eq(OOrderShipListItem::getListId, oOrderShipList.getId()));
        if(oOrderShipListItems.isEmpty()){
            return ResultVo.error("没有找到发货单明细");
        }
        // 开始出库
        for (OOrderShipListItem item : oOrderShipListItems) {
            if(item.getSkuId()==null||item.getSkuId()==0){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error("======出库错误：发货单明细没有找到SkuId:{}",item.getSkuId());
                return ResultVo.error("发货单明细没有找到SkuId:"+item.getId());
            }
            // 查询库存
            List<OGoodsInventory> oGoodsInventories = goodsInventoryMapper.selectList(new LambdaQueryWrapper<OGoodsInventory>().eq(OGoodsInventory::getSkuId, item.getSkuId()));
            if(oGoodsInventories.isEmpty()){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error("======出库错误：没有找到库存数据:{}",item.getSkuId());
                return ResultVo.error("没有找到库存数据:"+item.getSkuId());
            }
            int stock = oGoodsInventories.stream().mapToInt(oGoodsInventory -> oGoodsInventory.getQuantity()).sum();
            if(stock<item.getQuantity()){
                TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
                log.error("======出库错误：库存不足:{}",item.getSkuId());
                return ResultVo.error("库存不足:"+item.getSkuId());
            }
            //减库存
            OGoodsInventory stockUpdate = new OGoodsInventory();
            stockUpdate.setId(oGoodsInventories.get(0).getId());
            stockUpdate.setQuantity(oGoodsInventories.get(0).getQuantity()-item.getQuantity());
            stockUpdate.setUpdateTime(new Date());
            stockUpdate.setUpdateBy("发货出库");
            goodsInventoryMapper.updateById(stockUpdate);
            // 增加库存明细
            OGoodsInventoryBatch stockBatch = new OGoodsInventoryBatch();
            stockBatch.setInventoryId(Long.parseLong(stockUpdate.getId()));
            stockBatch.setBatchNum(DateUtils.parseDateToStr("yyyyMMddHHmmss", new Date()));
            stockBatch.setQty(item.getQuantity());
            stockBatch.setOriginQty(oGoodsInventories.get(0).getQuantity());
            stockBatch.setCurrentQty(stockUpdate.getQuantity());
            stockBatch.setRemark("发货出库"+item.getOrderNum());
            stockBatch.setSkuId(oGoodsInventories.get(0).getSkuId());
            stockBatch.setGoodsId(oGoodsInventories.get(0).getGoodsId());
            stockBatch.setSkuCode(oGoodsInventories.get(0).getSkuCode());
            stockBatch.setWarehouseId(0L);
            stockBatch.setPositionId(0L);
            stockBatch.setCreateBy("发货出库");
            stockBatch.setCreateTime(new Date());
            stockBatch.setUpdateTime(new Date());
            goodsInventoryBatchMapper.insert(stockBatch);

            // 更新自己
            OOrderShipListItem update = new OOrderShipListItem();
            update.setId(item.getId());
            update.setStatus(3);//备货完成
            update.setUpdateBy("备货完成");
            update.setUpdateTime(new Date());
            this.baseMapper.updateById(update);
        }
        // 更新发货订单
        OOrderShipList shipOrderUpdate = new OOrderShipList();
        shipOrderUpdate.setId(oOrderShipList.getId());
        shipOrderUpdate.setStatus(3);
        shipOrderUpdate.setUpdateTime(new Date());
        shipOrderUpdate.setUpdateBy("发货出库");
        oOrderShipListMapper.updateById(shipOrderUpdate);
        return ResultVo.success(oOrderShipList.getId());
    }

    @Override
    public ResultVo<Integer> updateErpSkuId(Long orderItemId, Long erpSkuId) {
        var oOrderItem = this.baseMapper.selectById(orderItemId);
        if(oOrderItem==null){
            return ResultVo.error("找不到数据");
        }else if(oOrderItem.getStatus()==3) return ResultVo.error("发货之后不允许修改");

        OGoodsSku oGoodsSku = goodsSkuMapper.selectById(erpSkuId);
        if(oGoodsSku==null){
            return ResultVo.error("找不到商品Sku数据");
        }

        OOrderShipListItem update = new OOrderShipListItem();
        update.setId(oOrderItem.getId());
        update.setSkuId(erpSkuId);
        update.setOriginalSkuId(oOrderItem.getSkuId()==null?"":oOrderItem.getSkuId().toString());
        update.setUpdateBy("手动修改ERP SKU ID");
        update.setUpdateTime(new Date());
        this.baseMapper.updateById(update);
        return ResultVo.success();
    }
}




