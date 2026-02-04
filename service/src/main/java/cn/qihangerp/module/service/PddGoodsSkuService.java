package cn.qihangerp.module.service;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.domain.bo.LinkErpGoodsSkuBo;
import cn.qihangerp.model.bo.PddGoodsSkuBo;
import cn.qihangerp.model.entity.PddGoodsSku;
import com.baomidou.mybatisplus.extension.service.IService;


/**
* @author TW
* @description 针对表【pdd_goods_sku(pdd商品SKU表)】的数据库操作Service
* @createDate 2024-06-04 17:11:49
*/
public interface PddGoodsSkuService extends IService<PddGoodsSku> {
    PageResult<PddGoodsSku> queryPageList(PddGoodsSkuBo bo, PageQuery pageQuery);
    ResultVo linkErpGoodsSku(LinkErpGoodsSkuBo bo);
}
