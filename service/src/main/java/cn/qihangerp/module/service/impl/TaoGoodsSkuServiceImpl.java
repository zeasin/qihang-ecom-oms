package cn.qihangerp.module.service.impl;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.domain.bo.LinkErpGoodsSkuBo;
import cn.qihangerp.mapper.TaoGoodsMapper;
import cn.qihangerp.mapper.TaoGoodsSkuMapper;
import cn.qihangerp.model.entity.OGoodsSku;
import cn.qihangerp.module.service.OGoodsSkuService;
import cn.qihangerp.model.entity.TaoGoods;
import cn.qihangerp.model.entity.TaoGoodsSku;
import cn.qihangerp.model.bo.TaoGoodsBo;
import cn.qihangerp.module.service.TaoGoodsSkuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

/**
* @author TW
* @description 针对表【tao_goods_sku】的数据库操作Service实现
* @createDate 2024-02-29 19:01:35
*/
@AllArgsConstructor
@Service
public class TaoGoodsSkuServiceImpl extends ServiceImpl<TaoGoodsSkuMapper, TaoGoodsSku>
    implements TaoGoodsSkuService {
    private final TaoGoodsSkuMapper mapper;
    private final TaoGoodsMapper goodsMapper;
    private final OGoodsSkuService oGoodsSkuService;

    @Override
    public PageResult<TaoGoodsSku> queryPageList(TaoGoodsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<TaoGoodsSku> ew = new LambdaQueryWrapper<TaoGoodsSku>()
                .eq(bo.getShopId()!=null,TaoGoodsSku::getShopId,bo.getShopId())
                .eq(bo.getNumIid()!=null,TaoGoodsSku::getNumIid,bo.getNumIid())
                .eq(bo.getSkuId()!=null,TaoGoodsSku::getSkuId,bo.getSkuId())
                .eq(StringUtils.hasText(bo.getOuterId()),TaoGoodsSku::getOuterId,bo.getOuterId())
                ;
        IPage<TaoGoodsSku> result = mapper.selectPage(pageQuery.build(), ew);
        return PageResult.build(result);
    }

    /**
     * 手动绑定erpGoodsSku
     * @param
     * @return
     */
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo linkErpGoodsSku(LinkErpGoodsSkuBo bo) {
        OGoodsSku oGoodsSku = oGoodsSkuService.getById(bo.getErpGoodsSkuId());
        if(oGoodsSku == null) return ResultVo.error("未找到系统商品sku");
        TaoGoodsSku taoGoodsSku = mapper.selectById(bo.getId());
        if(taoGoodsSku == null) {
            return ResultVo.error("Tao商品sku数据不存在");
        }
        TaoGoodsSku sku = new TaoGoodsSku();
        sku.setId(Long.parseLong(bo.getId()));
        sku.setErpGoodsId(Long.parseLong(oGoodsSku.getGoodsId()));
        sku.setErpGoodsSkuId(Long.parseLong(oGoodsSku.getId()));
        mapper.updateById(sku);

        TaoGoods goodsUp=new TaoGoods();
        goodsUp.setId(taoGoodsSku.getTaoGoodsId());
        goodsUp.setErpGoodsId(Long.parseLong(oGoodsSku.getGoodsId()));
        goodsMapper.updateById(goodsUp);
        return ResultVo.success();
    }
}




