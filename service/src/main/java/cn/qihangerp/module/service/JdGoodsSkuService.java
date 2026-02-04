package cn.qihangerp.module.service;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.domain.bo.LinkErpGoodsSkuBo;
import cn.qihangerp.model.entity.JdGoodsSku;
import cn.qihangerp.model.bo.JdGoodsBo;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author qilip
* @description 针对表【jd_goods_sku】的数据库操作Service
* @createDate 2024-03-09 20:44:11
*/
public interface JdGoodsSkuService extends IService<JdGoodsSku> {
    PageResult<JdGoodsSku> queryPageList(JdGoodsBo bo, PageQuery pageQuery);
    ResultVo linkErpGoodsSku(LinkErpGoodsSkuBo bo);
}
