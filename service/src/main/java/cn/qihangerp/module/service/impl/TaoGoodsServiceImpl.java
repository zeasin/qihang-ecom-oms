package cn.qihangerp.module.service.impl;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.common.utils.StringUtils;
import cn.qihangerp.mapper.TaoGoodsMapper;
import cn.qihangerp.mapper.TaoGoodsSkuMapper;
import cn.qihangerp.mapper.goods.OGoodsSkuMapper;
import cn.qihangerp.model.entity.OGoodsSku;
import cn.qihangerp.model.entity.TaoGoods;
import cn.qihangerp.model.entity.TaoGoodsSku;
import cn.qihangerp.model.bo.TaoGoodsBo;
import cn.qihangerp.module.service.TaoGoodsService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
* @author TW
* @description 针对表【tao_goods】的数据库操作Service实现
* @createDate 2024-02-29 09:28:38
*/
@AllArgsConstructor
@Service
public class TaoGoodsServiceImpl extends ServiceImpl<TaoGoodsMapper, TaoGoods>
    implements TaoGoodsService {

    private final TaoGoodsMapper mapper;
    private final TaoGoodsSkuMapper skuMapper;
    private final OGoodsSkuMapper goodsSkuMapper;

    @Override
    public PageResult<TaoGoods> queryPageList(TaoGoodsBo bo, PageQuery pageQuery) {
        LambdaQueryWrapper<TaoGoods> queryWrapper = new LambdaQueryWrapper<TaoGoods>()
                .eq(bo.getShopId()!=null,TaoGoods::getShopId,bo.getShopId());

        Page<TaoGoods> taoGoodsPage = mapper.selectPage(pageQuery.build(), queryWrapper);
        return PageResult.build(taoGoodsPage);
    }

    @Transactional
    @Override
    public int saveAndUpdateGoods(Long shopId, TaoGoods goods) {
        List<TaoGoods> goodsList = mapper.selectList(new LambdaQueryWrapper<TaoGoods>().eq(TaoGoods::getNumIid, goods.getNumIid()));
        if(goodsList!=null && goodsList.size()>0){
            // 存在，更新
            goods.setShopId(shopId);
            goods.setId(goodsList.get(0).getId());
            mapper.updateById(goods);

            // 删除sku
            skuMapper.delete(new LambdaQueryWrapper<TaoGoodsSku>().eq(TaoGoodsSku::getTaoGoodsId,goods.getId()));

            // 重新插入sku
            if(goods.getSkus()!=null) {
                for (TaoGoodsSku sku : goods.getSkus()) {
                    sku.setTaoGoodsId(goods.getId());
                    sku.setShopId(goods.getShopId());
                    Long erpGoodsId = 0L;
                    Long erpGoodsSkuId = 0L;
                    // 根据OuterId查找ERP系统中的skuid
                    if(StringUtils.isNotEmpty(sku.getOuterId())) {
                        List<OGoodsSku> oGoodsSkus = goodsSkuMapper.selectList(new LambdaQueryWrapper<OGoodsSku>().eq(OGoodsSku::getSkuCode, sku.getOuterId()));
                        if(oGoodsSkus!=null && !oGoodsSkus.isEmpty()){
                            erpGoodsId=Long.parseLong(oGoodsSkus.get(0).getGoodsId());
                            erpGoodsSkuId=Long.parseLong(oGoodsSkus.get(0).getId());
                        }
                    }
                    sku.setErpGoodsId(erpGoodsId);
                    sku.setErpGoodsSkuId(erpGoodsSkuId);
                    skuMapper.insert(sku);
                }
            }
            return ResultVoEnum.DataExist.getIndex();
        }else {
            // 不存在，新增
            goods.setShopId(shopId);
            goods.setCreateTime(new Date());
            mapper.insert(goods);
            // 插入sku
            if(goods.getSkus()!=null) {
                for (TaoGoodsSku sku : goods.getSkus()) {
                    sku.setTaoGoodsId(goods.getId());
                    sku.setShopId(goods.getShopId());
                    // 根据OuterId查找ERP系统中的skuid
                    if(StringUtils.isNotEmpty(sku.getOuterId())) {
                        List<OGoodsSku> oGoodsSkus = goodsSkuMapper.selectList(new LambdaQueryWrapper<OGoodsSku>().eq(OGoodsSku::getSkuCode, sku.getOuterId()));
                        if(oGoodsSkus!=null && !oGoodsSkus.isEmpty()){
                            sku.setErpGoodsId(Long.parseLong(oGoodsSkus.get(0).getGoodsId()));
                            sku.setErpGoodsSkuId(Long.parseLong(oGoodsSkus.get(0).getId()));
                        }
                    }

                    skuMapper.insert(sku);
                }
            }
            return 0;
        }
    }
}




