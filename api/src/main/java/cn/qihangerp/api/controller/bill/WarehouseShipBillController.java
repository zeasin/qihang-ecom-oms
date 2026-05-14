package cn.qihangerp.api.controller.bill;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.TableDataInfo;
import cn.qihangerp.model.entity.ErpBillShipment;
import cn.qihangerp.service.ErpBillShipmentService;
import cn.qihangerp.security.common.BaseController;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 仓库发货账单
 */
@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/financial")
public class WarehouseShipBillController extends BaseController {
    private final ErpBillShipmentService erpBillShipmentService;
    /**
     * 发货费用账单
     */
    @GetMapping("/warehouse_ship_bill/list")
    public TableDataInfo list(ErpBillShipment goods, PageQuery pageQuery)
    {
        if(getUserId()!=1) {
            goods.setTenantId(getUserId());
        }
        PageResult<ErpBillShipment> pageResult = erpBillShipmentService.queryWarehousePageList(goods, pageQuery);
        return getDataTable(pageResult);
    }

//    @PostMapping("/supplier_ship_bill/confirmBillSettlement/{ids}")
//    public AjaxResult remove(@PathVariable Long[] ids)
//    {
//        for(Long id:ids) {
//            ErpBillShipment bill = erpBillShipmentService.getById(id);
//            if(bill != null) {
//                if(bill.getTenantId()==getUserId()){
//                    if(bill.getStatus()==0){
//                        ErpBillShipment update =new ErpBillShipment();
//                        update.setId(bill.getId());
//                        update.setStatus(1);
//                        update.setUpdateTime(new Date());
//                        update.setUpdateBy("批量确认结算"+getUsername());
//                        erpBillShipmentService.updateById(update);
//                    }
//                }
//            }
//        }
//            return AjaxResult.success();
//
//    }
}
