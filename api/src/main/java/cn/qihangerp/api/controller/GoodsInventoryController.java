package cn.qihangerp.api.controller;


import cn.qihangerp.common.*;
import cn.qihangerp.model.entity.OGoodsInventory;
import cn.qihangerp.model.entity.OGoodsInventoryBatch;
import cn.qihangerp.model.bo.GoodsInventoryModifyBo;
import cn.qihangerp.module.service.OGoodsInventoryBatchService;
import cn.qihangerp.module.service.OGoodsInventoryService;
import cn.qihangerp.security.common.BaseController;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/goodsInventory")
public class GoodsInventoryController extends BaseController {
    private final OGoodsInventoryService goodsInventoryService;
    private final OGoodsInventoryBatchService inventoryBatchService;

    @GetMapping("/list")
    public TableDataInfo list(OGoodsInventory bo, PageQuery pageQuery)
    {
        PageResult<OGoodsInventory> pageResult = goodsInventoryService.queryPageList(bo, pageQuery);
        return getDataTable(pageResult);
    }

    @GetMapping(value = "/{id}")
    public AjaxResult getInfo(@PathVariable("id") Long id)
    {
        OGoodsInventory goodsInventory = goodsInventoryService.getById(id);
        if(goodsInventory!=null) {
            List<OGoodsInventoryBatch> list = inventoryBatchService.list(new LambdaQueryWrapper<OGoodsInventoryBatch>().eq(OGoodsInventoryBatch::getSkuId, goodsInventory.getSkuId()));
            return AjaxResult.success(list);
        }
        return success();
    }

    @PostMapping("/modify")
    public AjaxResult modify(@RequestBody GoodsInventoryModifyBo bo)
    {
        ResultVo<Long> resultVo = goodsInventoryService.modifyInventory(bo);
        if(resultVo.getCode()!=0) return AjaxResult.error(resultVo.getMsg());
        else return AjaxResult.success(resultVo.getData());
    }
}
