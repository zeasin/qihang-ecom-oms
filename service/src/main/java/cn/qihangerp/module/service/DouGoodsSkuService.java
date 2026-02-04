package cn.qihangerp.module.service;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.domain.bo.LinkErpGoodsSkuBo;
import cn.qihangerp.model.entity.DouGoodsSku;
import cn.qihangerp.model.bo.DouGoodsBo;
import com.baomidou.mybatisplus.extension.service.IService;


/**
* @author TW
* @description 针对表【dou_goods_sku(抖店商品Sku表)】的数据库操作Service
* @createDate 2024-05-31 17:23:21
*/
public interface DouGoodsSkuService extends IService<DouGoodsSku> {
    PageResult<DouGoodsSku> queryPageList(DouGoodsBo bo, PageQuery pageQuery);
    ResultVo linkErpGoodsSku(LinkErpGoodsSkuBo bo);
}
