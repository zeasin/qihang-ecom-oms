package cn.qihangerp.module.service;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.domain.bo.LinkErpGoodsSkuBo;
import cn.qihangerp.model.entity.TaoGoodsSku;
import cn.qihangerp.model.bo.TaoGoodsBo;
import com.baomidou.mybatisplus.extension.service.IService;


/**
* @author TW
* @description 针对表【tao_goods_sku】的数据库操作Service
* @createDate 2024-02-29 19:01:35
*/
public interface TaoGoodsSkuService extends IService<TaoGoodsSku> {
    PageResult<TaoGoodsSku> queryPageList(TaoGoodsBo bo, PageQuery pageQuery);
    ResultVo linkErpGoodsSku(LinkErpGoodsSkuBo bo);
}
