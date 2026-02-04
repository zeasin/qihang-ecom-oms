package cn.qihangerp.module.service.impl;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.domain.bo.LinkErpGoodsSkuBo;
import cn.qihangerp.mapper.goods.OGoodsMapper;
import cn.qihangerp.mapper.goods.OGoodsSkuMapper;
import cn.qihangerp.model.bo.PddGoodsSkuBo;
import cn.qihangerp.model.entity.OGoods;
import cn.qihangerp.model.entity.OGoodsSku;
import cn.qihangerp.model.entity.PddGoods;
import cn.qihangerp.model.entity.PddGoodsSku;
import cn.qihangerp.mapper.PddGoodsMapper;
import cn.qihangerp.mapper.PddGoodsSkuMapper;
import cn.qihangerp.module.service.PddGoodsSkuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
* @author TW
* @description 针对表【pdd_goods_sku(pdd商品SKU表)】的数据库操作Service实现
* @createDate 2024-06-04 17:11:49
*/
@AllArgsConstructor
@Service
public class PddGoodsSkuServiceImpl extends ServiceImpl<PddGoodsSkuMapper, PddGoodsSku>
    implements PddGoodsSkuService {
    private final PddGoodsSkuMapper mapper;
    private final PddGoodsMapper pddGoodsMapper;
    private final OGoodsSkuMapper oGoodsSkuMapper;
    private final OGoodsMapper oGoodsMapper;
    @Override
    public PageResult<PddGoodsSku> queryPageList(PddGoodsSkuBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<PddGoodsSku> ew = new LambdaQueryWrapper<PddGoodsSku>()
                .eq(bo.getShopId()!=null,PddGoodsSku::getShopId,bo.getShopId())
                .eq(bo.getGoodsId()!=null,PddGoodsSku::getGoodsId,bo.getGoodsId())
                .eq(StringUtils.hasText(bo.getSkuId()),PddGoodsSku::getSkuId,bo.getSkuId())
                .eq(bo.getErpSkuId()!=null,PddGoodsSku::getErpGoodsSkuId,bo.getErpSkuId())
                .eq(StringUtils.hasText(bo.getOuterId()),PddGoodsSku::getOuterId,bo.getOuterId())
                .eq(bo.getIsSkuOnsale()!=null,PddGoodsSku::getIsSkuOnsale,bo.getIsSkuOnsale())
                ;
        IPage<PddGoodsSku> result = mapper.selectPage(pageQuery.build(), ew);
        return PageResult.build(result);
    }
    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo linkErpGoodsSku(LinkErpGoodsSkuBo bo) {
        OGoodsSku oGoodsSku = oGoodsSkuMapper.selectById(bo.getErpGoodsSkuId());
        if(oGoodsSku == null) return ResultVo.error("未找到系统商品sku");

        OGoods oGoods=oGoodsMapper.selectById(oGoodsSku.getGoodsId());
        if(oGoods == null){
            return ResultVo.error("未找到系统商品");
        }

        PddGoodsSku taoGoodsSku = mapper.selectById(bo.getId());
        if(taoGoodsSku == null) {
            return ResultVo.error("PDD商品sku数据不存在");
        }
        List<PddGoods> jdGoods = pddGoodsMapper.selectList(new LambdaQueryWrapper<PddGoods>().eq(PddGoods::getGoodsId, taoGoodsSku.getGoodsId()));
        if(jdGoods==null||jdGoods.size()==0){
            return ResultVo.error("PDD商品数据不存在");
        }

        PddGoodsSku sku = new PddGoodsSku();
        sku.setId(Long.parseLong(bo.getId()));
        sku.setErpGoodsId(Long.parseLong(oGoodsSku.getGoodsId()));
        sku.setErpGoodsSkuId(Long.parseLong(oGoodsSku.getId()));
        mapper.updateById(sku);

        PddGoods goodsUp=new PddGoods();
        goodsUp.setId(jdGoods.get(0).getId());
        goodsUp.setErpGoodsId(Long.parseLong(oGoodsSku.getGoodsId()));
        pddGoodsMapper.updateById(goodsUp);
        return ResultVo.success();
    }
}




