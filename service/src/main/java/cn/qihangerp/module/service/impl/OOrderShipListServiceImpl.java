package cn.qihangerp.module.service.impl;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.model.bo.ShipStockUpBo;
import cn.qihangerp.model.entity.OOrderShipListItem;
import cn.qihangerp.mapper.OOrderShipListItemMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.model.entity.OOrderShipList;
import cn.qihangerp.module.service.OOrderShipListService;
import cn.qihangerp.mapper.OOrderShipListMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
* @author qilip
* @description 针对表【o_order_ship_list(发货-备货表（取号发货加入备货清单、分配供应商发货加入备货清单）)】的数据库操作Service实现
* @createDate 2025-06-01 23:07:24
*/
@AllArgsConstructor
@Service
public class OOrderShipListServiceImpl extends ServiceImpl<OOrderShipListMapper, OOrderShipList>
    implements OOrderShipListService{
    private final OOrderShipListItemMapper shipListItemMapper;

    @Override
    public PageResult<OOrderShipList> querySupplierPageList(ShipStockUpBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<OOrderShipList> queryWrapper = new LambdaQueryWrapper<OOrderShipList>()
                .eq(OOrderShipList::getShipper,1)
                .eq(bo.getShipSupplierId()!=null,OOrderShipList::getShipSupplierId,bo.getShipSupplierId())
                .eq(bo.getShopId()!=null,OOrderShipList::getShopId,bo.getShopId())
                .eq(bo.getStatus()!=null,OOrderShipList::getStatus,bo.getStatus())
                .eq(StringUtils.hasText(bo.getOrderNum()),OOrderShipList::getOrderNum,bo.getOrderNum())
                ;
        Page<OOrderShipList> pages = this.baseMapper.selectPage(pageQuery.build(), queryWrapper);
        if(pages.getRecords()!=null && pages.getRecords().size()>0){
            for(OOrderShipList o : pages.getRecords()){
                o.setItems(shipListItemMapper.selectList(new LambdaQueryWrapper<OOrderShipListItem>().eq(OOrderShipListItem::getListId,o.getId())));
            }
        }

        return PageResult.build(pages);
    }

    @Override
    public PageResult<OOrderShipList> queryWarehousePageList(ShipStockUpBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<OOrderShipList> queryWrapper = new LambdaQueryWrapper<OOrderShipList>()
                .eq(OOrderShipList::getShipper,0)
                .eq(bo.getShipSupplierId()!=null,OOrderShipList::getShipSupplierId,bo.getShipSupplierId())
                .eq(bo.getShopId()!=null,OOrderShipList::getShopId,bo.getShopId())
                .eq(bo.getStatus()!=null,OOrderShipList::getStatus,bo.getStatus())
                .eq(StringUtils.hasText(bo.getOrderNum()),OOrderShipList::getOrderNum,bo.getOrderNum())
                ;
        Page<OOrderShipList> pages = this.baseMapper.selectPage(pageQuery.build(), queryWrapper);
        if(pages.getRecords()!=null && pages.getRecords().size()>0){
            for(OOrderShipList o : pages.getRecords()){
                o.setItems(shipListItemMapper.selectList(new LambdaQueryWrapper<OOrderShipListItem>().eq(OOrderShipListItem::getListId,o.getId())));
            }
        }

        return PageResult.build(pages);
    }
}




