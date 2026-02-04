package cn.qihangerp.module.service.impl;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.model.entity.OShipment;
import cn.qihangerp.model.entity.OShipmentItem;
import cn.qihangerp.mapper.OShipmentItemMapper;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.module.service.ErpShipmentService;
import cn.qihangerp.mapper.OShipmentMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
* @author qilip
* @description 针对表【erp_shipment(发货记录表)】的数据库操作Service实现
* @createDate 2025-06-01 23:22:40
*/
@AllArgsConstructor
@Service
public class ErpShipmentServiceImpl extends ServiceImpl<OShipmentMapper, OShipment>
    implements ErpShipmentService{
    private final OShipmentItemMapper shipmentItemMapper;
    @Override
    public PageResult<OShipment> queryPageList(OShipment shipping, PageQuery pageQuery) {
        LambdaQueryWrapper<OShipment> queryWrapper = new LambdaQueryWrapper<OShipment>()
                .eq(shipping.getShipper()!=null, OShipment::getShipper,shipping.getShipper())
                .eq(StringUtils.hasText(shipping.getOrderNum()), OShipment::getOrderNum, shipping.getOrderNum())
                .eq(StringUtils.hasText(shipping.getShipCode()), OShipment::getShipCode, shipping.getShipCode())
                .eq(shipping.getShopId() != null, OShipment::getShopId, shipping.getShopId());

        Page<OShipment> pages = this.baseMapper.selectPage(pageQuery.build(), queryWrapper);
        if(pages.getRecords().size()>0){
            for(OShipment item : pages.getRecords()){
                item.setItemList(shipmentItemMapper.selectList(new LambdaQueryWrapper<OShipmentItem>()
                        .eq(OShipmentItem::getShipmentId,item.getId())));
            }
        }
        return PageResult.build(pages);
    }

    @Override
    public OShipment queryDetailById(Long id) {
        OShipment oShipment = this.baseMapper.selectById(id);
        if(oShipment !=null){
            oShipment.setItemList(shipmentItemMapper.selectList(new LambdaQueryWrapper<OShipmentItem>().eq(OShipmentItem::getShipmentId, oShipment.getId())));
        }
        return oShipment;
    }
}




