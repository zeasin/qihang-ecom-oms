package cn.qihangerp.module.service;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.model.entity.OGoodsInventory;
import cn.qihangerp.model.bo.GoodsInventoryModifyBo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author qilip
* @description 针对表【o_goods_inventory(商品库存表)】的数据库操作Service
* @createDate 2024-09-23 22:39:50
*/
public interface OGoodsInventoryService extends IService<OGoodsInventory> {
    PageResult<OGoodsInventory> queryPageList(OGoodsInventory bo, PageQuery pageQuery);
    long getAllInventoryQuantity();

    /**
     * 库存变更
     * @param bo
     * @return
     */
    ResultVo<Long> modifyInventory(GoodsInventoryModifyBo bo);
}
