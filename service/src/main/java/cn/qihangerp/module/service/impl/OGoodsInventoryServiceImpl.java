package cn.qihangerp.module.service.impl;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.common.utils.DateUtils;
import cn.qihangerp.mapper.goods.OGoodsInventoryBatchMapper;
import cn.qihangerp.mapper.goods.OGoodsInventoryMapper;
import cn.qihangerp.model.entity.OGoodsInventoryBatch;
import cn.qihangerp.model.bo.GoodsInventoryModifyBo;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.model.entity.OGoodsInventory;
import cn.qihangerp.module.service.OGoodsInventoryService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.Date;

/**
* @author qilip
* @description 针对表【o_goods_inventory(商品库存表)】的数据库操作Service实现
* @createDate 2024-09-23 22:39:50
*/
@AllArgsConstructor
@Service
public class OGoodsInventoryServiceImpl extends ServiceImpl<OGoodsInventoryMapper, OGoodsInventory>
    implements OGoodsInventoryService{
    private final OGoodsInventoryMapper mapper;
    private final OGoodsInventoryBatchMapper goodsInventoryBatchMapper;

    @Override
    public PageResult<OGoodsInventory> queryPageList(OGoodsInventory bo, PageQuery pageQuery) {
        LambdaQueryWrapper<OGoodsInventory> queryWrapper = new LambdaQueryWrapper<OGoodsInventory>();
        queryWrapper.eq(bo.getGoodsId()!=null,OGoodsInventory::getGoodsId,bo.getGoodsId());
        queryWrapper.eq(bo.getSkuId()!=null,OGoodsInventory::getSkuId,bo.getSkuId());
        queryWrapper.eq(StringUtils.hasText(bo.getGoodsNum()),OGoodsInventory::getGoodsNum,bo.getGoodsNum());
        queryWrapper.eq(StringUtils.hasText(bo.getSkuCode()),OGoodsInventory::getSkuCode,bo.getSkuCode());

        Page<OGoodsInventory> pages = mapper.selectPage(pageQuery.build(), queryWrapper);

        return PageResult.build(pages);
    }

    @Override
    public long getAllInventoryQuantity() {
        return mapper.getAllInventoryQuantity();
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo<Long> modifyInventory(GoodsInventoryModifyBo bo) {
        if(bo.getId()==null|| bo.getId()==0){
            return ResultVo.error("缺少参数：id");
        }
        if(bo.getQty()==null||bo.getQty()==0){
            return ResultVo.error("缺少参数：qty");
        }
        OGoodsInventory oGoodsInventory = mapper.selectById(bo.getId());
        if(oGoodsInventory==null){
            return ResultVo.error("库存数据不存在");
        }
        if(bo.getType()==null){
            return ResultVo.error("缺少参数：type");
        }
        if(bo.getType()!=1&&bo.getType()!=2){
            return ResultVo.error("不支持的库存操作");
        }
        int originQty = oGoodsInventory.getQuantity();
        Integer newQty = null;
        if(bo.getType()==1) {
            // 加库存
            newQty = originQty+bo.getQty();
        }
        else if(bo.getType()==2){
            //减库存
            newQty = originQty-bo.getQty();
        }
        OGoodsInventory stockUpdate = new OGoodsInventory();
        stockUpdate.setId(oGoodsInventory.getId());
        stockUpdate.setQuantity(newQty);
        stockUpdate.setUpdateTime(new Date());
        stockUpdate.setUpdateBy("发货出库");
        mapper.updateById(stockUpdate);

        // 增加库存明细
        OGoodsInventoryBatch stockBatch = new OGoodsInventoryBatch();
        stockBatch.setInventoryId(Long.parseLong(stockUpdate.getId()));
        stockBatch.setBatchNum(DateUtils.parseDateToStr("yyyyMMddHHmmss", new Date()));
        stockBatch.setQty(bo.getQty());
        stockBatch.setOriginQty(originQty);
        stockBatch.setCurrentQty(newQty);
        stockBatch.setSkuId(oGoodsInventory.getSkuId());
        stockBatch.setGoodsId(oGoodsInventory.getGoodsId());
        stockBatch.setSkuCode(oGoodsInventory.getSkuCode());
        stockBatch.setWarehouseId(0L);
        stockBatch.setPositionId(0L);
        if(bo.getType()==1) {
            // 加库存
            stockBatch.setCreateBy("增加库存");
            stockBatch.setRemark("增加库存："+bo.getRemark());
        }
        else if(bo.getType()==2){
            //减库存
            stockBatch.setCreateBy("减少库存");
            stockBatch.setRemark("减少库存:"+bo.getRemark());
        }

        stockBatch.setCreateTime(new Date());
        stockBatch.setUpdateTime(new Date());
        goodsInventoryBatchMapper.insert(stockBatch);
        return ResultVo.success();
    }
}




