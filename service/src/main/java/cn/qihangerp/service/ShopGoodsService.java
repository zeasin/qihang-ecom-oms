package cn.qihangerp.service;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;

import cn.qihangerp.model.entity.OShop;
import cn.qihangerp.model.entity.ShopGoods;
import cn.qihangerp.model.request.*;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author qilip
* @description 针对表【oms_shop_goods(其他渠道店铺商品)】的数据库操作Service
* @createDate 2025-07-15 08:29:21
*/
public interface ShopGoodsService extends IService<ShopGoods> {
    PageResult<ShopGoods> queryPageList(ShopGoods bo, PageQuery pageQuery);
    PageResult<ShopGoods> queryVirtualAndNoSkusPageList(ShopGoods bo, PageQuery pageQuery);
    ResultVo<Integer> saveOrUpdateGoods(Long shopId,Long merchantId,Integer shopType, ShopGoods goods);

    /**
     * 添加商品
     * @param goodsAddRequest
     *
     * @return
     */
    ResultVo<Long> addGoods(ShopGoodsAddRequest goodsAddRequest, OShop shop);
    ResultVo<Long> addGoodsSku(ShopGoodsSkuAddRequest request );
    ResultVo<Long> insertGoodsSku(ShopGoodsSkuInsertRequest request );
    ResultVo<Long> updateGoodsSku(ShopGoodsSkuUpdateRequest request );
    ResultVo<Long> updateGoodsStock(ShopGoodsSkuStockUpdateRequest goodsAddRequest);
    ResultVo<Long> savePddGoods(ShopGoods shopGoods,Long shopId);
}
