package cn.qihangerp.service;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.model.bo.BindErpGoodsSkuBo;
import cn.qihangerp.model.bo.LinkErpGoodsSkuBo;

import cn.qihangerp.model.entity.ShopGoodsSku;
import cn.qihangerp.model.query.ShopGoodsSkuQuery;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author qilip
* @description 针对表【oms_shop_goods_sku(其他渠道店铺商品SKU)】的数据库操作Service
* @createDate 2025-07-15 08:29:21
*/
public interface ShopGoodsSkuService extends IService<ShopGoodsSku> {
    PageResult<ShopGoodsSku> queryPageList(ShopGoodsSkuQuery bo, PageQuery pageQuery);

    List<ShopGoodsSku> querySkuList(Long shopGoodsId);
    ResultVo linkErpGoodsSku(LinkErpGoodsSkuBo bo);

    /**
     * 查询平台skuid是否存在商品
     * @param platformSkuId
     * @param shopId
     * @return
     */
    ShopGoodsSku selectByPlatformSkuId(String platformSkuId,Long shopId);

}
