package com.qihang.erp.api.service.impl;

import java.util.List;

import com.qihang.erp.api.domain.GoodsSpec;
import com.qihang.erp.api.domain.GoodsSpecAttr;
import com.qihang.erp.api.domain.bo.GoodsSpecAddBo;
import com.qihang.erp.api.domain.vo.GoodsSpecListVo;
import com.qihang.erp.api.mapper.GoodsSpecAttrMapper;
import com.qihang.erp.api.mapper.GoodsSpecMapper;
import com.zhijian.common.utils.DateUtils;
import com.zhijian.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qihang.erp.api.mapper.GoodsMapper;
import com.qihang.erp.api.domain.Goods;
import com.qihang.erp.api.service.IGoodsService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.interceptor.TransactionAspectSupport;

/**
 * 商品管理Service业务层处理
 * 
 * @author qihang
 * @date 2023-12-29
 */
@Service
public class GoodsServiceImpl implements IGoodsService 
{
    @Autowired
    private GoodsMapper goodsMapper;
    @Autowired
    private GoodsSpecMapper goodsSpecMapper;
    @Autowired
    private GoodsSpecAttrMapper specAttrMapper;


    /**
     * 查询商品管理
     * 
     * @param id 商品管理主键
     * @return 商品管理
     */
    @Override
    public Goods selectGoodsById(Long id)
    {
        return goodsMapper.selectGoodsById(id);
    }

    /**
     * 查询商品管理列表
     * 
     * @param goods 商品管理
     * @return 商品管理
     */
    @Override
    public List<Goods> selectGoodsList(Goods goods)
    {
        return goodsMapper.selectGoodsList(goods);
    }

    /**
     * 新增商品管理
     * 
     * @param goods 商品管理
     * @return 结果
     */
    @Transactional
    @Override
    public int insertGoods(Goods goods)
    {
        // 查询编码是否存在
        Goods goods1 = goodsMapper.selectGoodsByNumber(goods.getNumber());
        if(goods1!=null) return -1;

        // 1、添加主表erp_goods
        goods.setCreateTime(DateUtils.getNowDate());
        goodsMapper.insertGoods(goods);

        // 2、添加规格表erp_goods_spec
        for (GoodsSpecAddBo bo:goods.getSpecList()) {
            GoodsSpec spec = new GoodsSpec();
            spec.setGoodsId(goods.getId());
            spec.setSpecNum(bo.getSpecNum());
            spec.setColorId(bo.getColorId());
            spec.setColorValue(bo.getColorValue());
            if(goods.getColorImages()!=null && StringUtils.isNotEmpty(goods.getColorImages().get(bo.getColorId()))){
                spec.setColorImage(goods.getColorImages().get(bo.getColorId()));
            }else {
                spec.setColorImage(goods.getImage());
            }
            spec.setSizeId(bo.getSizeId());
            spec.setSizeValue(bo.getSizeValue());
            spec.setStyleId(bo.getStyleId());
            spec.setStyleValue(bo.getStyleValue());
            if(bo.getPurPrice() == null){
                spec.setPurPrice(goods.getPurPrice());
            }else spec.setPurPrice(bo.getPurPrice());
            spec.setStatus(1);
            spec.setDisable(0);
            goodsSpecMapper.insertGoodsSpec(spec);
        }

        // 3、添加规格属性表erp_goods_spec_attr
        if(goods.getColorValues()!=null) {
            for (Long val:goods.getColorValues()) {
                GoodsSpecAttr specAttr = new GoodsSpecAttr();
                specAttr.setGoodsId(goods.getId());
                specAttr.setType("color");
                specAttr.setK("颜色");
                specAttr.setKid(114L);
                specAttr.setVid(val);
                specAttrMapper.insertGoodsSpecAttr(specAttr);
            }

        }
        if(goods.getSizeValues()!=null) {
            for (Long val:goods.getSizeValues()) {
                GoodsSpecAttr specAttr = new GoodsSpecAttr();
                specAttr.setGoodsId(goods.getId());
                specAttr.setType("size");
                specAttr.setK("尺码");
                specAttr.setKid(115L);
                specAttr.setVid(val);
                specAttrMapper.insertGoodsSpecAttr(specAttr);
            }

        }
        if(goods.getColorValues()!=null) {
            for (Long val:goods.getColorValues()) {
                GoodsSpecAttr specAttr = new GoodsSpecAttr();
                specAttr.setGoodsId(goods.getId());
                specAttr.setType("style");
                specAttr.setK("款式");
                specAttr.setKid(116L);
                specAttr.setVid(val);
                specAttrMapper.insertGoodsSpecAttr(specAttr);
            }

        }
//        TransactionAspectSupport.currentTransactionStatus().setRollbackOnly();
        return 1;
    }

    /**
     * 修改商品管理
     * 
     * @param goods 商品管理
     * @return 结果
     */
    @Override
    public int updateGoods(Goods goods)
    {
        goods.setUpdateTime(DateUtils.getNowDate());
        return goodsMapper.updateGoods(goods);
    }

    /**
     * 批量删除商品管理
     * 
     * @param ids 需要删除的商品管理主键
     * @return 结果
     */
    @Override
    public int deleteGoodsByIds(Long[] ids)
    {
        return goodsMapper.deleteGoodsByIds(ids);
    }

    /**
     * 删除商品管理信息
     * 
     * @param id 商品管理主键
     * @return 结果
     */
    @Override
    public int deleteGoodsById(Long id)
    {
        return goodsMapper.deleteGoodsById(id);
    }

    @Override
    public List<GoodsSpecListVo> searchGoodsSpec(String keyword) {
        return goodsMapper.searchGoodsSpec(keyword);
    }
}
