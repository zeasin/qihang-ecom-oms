package cn.qihangerp.module.service.impl;

import cn.qihangerp.model.entity.OShipmentItem;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import cn.qihangerp.module.service.ErpShipmentItemService;
import cn.qihangerp.mapper.OShipmentItemMapper;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

/**
* @author qilip
* @description 针对表【erp_shipment_item(发货明细表)】的数据库操作Service实现
* @createDate 2025-06-01 23:25:11
*/
@AllArgsConstructor
@Service
public class ErpShipmentItemServiceImpl extends ServiceImpl<OShipmentItemMapper, OShipmentItem>
    implements ErpShipmentItemService{

}




