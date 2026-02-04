package cn.qihangerp.module.service.impl;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.domain.bo.LinkErpGoodsSkuBo;
import cn.qihangerp.mapper.DouGoodsMapper;
import cn.qihangerp.mapper.DouGoodsSkuMapper;
import cn.qihangerp.mapper.goods.OGoodsMapper;
import cn.qihangerp.mapper.goods.OGoodsSkuMapper;
import cn.qihangerp.model.entity.OGoods;
import cn.qihangerp.model.entity.OGoodsSku;
import cn.qihangerp.model.entity.DouGoods;
import cn.qihangerp.model.entity.DouGoodsSku;
import cn.qihangerp.model.bo.DouGoodsBo;
import cn.qihangerp.module.service.DouGoodsSkuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.List;

/**
* @author TW
* @description 针对表【dou_goods_sku(抖店商品Sku表)】的数据库操作Service实现
* @createDate 2024-05-31 17:23:21
*/
@AllArgsConstructor
@Service
public class DouGoodsSkuServiceImpl extends ServiceImpl<DouGoodsSkuMapper, DouGoodsSku>
    implements DouGoodsSkuService {
    private final DouGoodsSkuMapper mapper;
    private final DouGoodsMapper douGoodsMapper;
    private final OGoodsSkuMapper oGoodsSkuMapper;
    private final OGoodsMapper oGoodsMapper;
    @Override
    public PageResult<DouGoodsSku> queryPageList(DouGoodsBo bo, PageQuery pageQuery) {
        if(StringUtils.hasText(bo.getCode())){
            bo.setCode(bo.getCode().trim());
        }
        LambdaQueryWrapper<DouGoodsSku> queryWrapper = new LambdaQueryWrapper<DouGoodsSku>()
                .eq(bo.getShopId()!=null,DouGoodsSku::getShopId,bo.getShopId())

                ;

        Page<DouGoodsSku> goodsPage = mapper.selectPage(pageQuery.build(), queryWrapper);
        return PageResult.build(goodsPage);
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

        DouGoodsSku taoGoodsSku = mapper.selectById(bo.getId());
        if(taoGoodsSku == null) {
            return ResultVo.error("DOU商品sku数据不存在");
        }
        List<DouGoods> jdGoods = douGoodsMapper.selectList(new LambdaQueryWrapper<DouGoods>().eq(DouGoods::getProductId, taoGoodsSku.getProductId()));
        if(jdGoods==null||jdGoods.size()==0){
            return ResultVo.error("DOU商品数据不存在");
        }

        DouGoodsSku sku = new DouGoodsSku();
        sku.setId(bo.getId());
        sku.setErpGoodsId(Long.parseLong(oGoodsSku.getGoodsId()));
        sku.setErpGoodsSkuId(Long.parseLong(oGoodsSku.getId()));
        mapper.updateById(sku);

        DouGoods goodsUp=new DouGoods();
        goodsUp.setId(jdGoods.get(0).getId());
        goodsUp.setErpGoodsId(Long.parseLong(oGoodsSku.getGoodsId()));

        douGoodsMapper.updateById(goodsUp);
        return ResultVo.success();
    }
}




