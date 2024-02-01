package com.qihang.erp.api.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.qihang.erp.api.mapper.GoodsImgMapper;
import com.qihang.erp.api.domain.GoodsImg;
import com.qihang.erp.api.service.IGoodsImgService;

/**
 * 商品图片Service业务层处理
 * 
 * @author qihang
 * @date 2023-12-29
 */
@Service
public class GoodsImgServiceImpl implements IGoodsImgService 
{
    @Autowired
    private GoodsImgMapper goodsImgMapper;

    /**
     * 查询商品图片
     * 
     * @param id 商品图片主键
     * @return 商品图片
     */
    @Override
    public GoodsImg selectGoodsImgById(Long id)
    {
        return goodsImgMapper.selectGoodsImgById(id);
    }

    /**
     * 查询商品图片列表
     * 
     * @param goodsImg 商品图片
     * @return 商品图片
     */
    @Override
    public List<GoodsImg> selectGoodsImgList(GoodsImg goodsImg)
    {
        return goodsImgMapper.selectGoodsImgList(goodsImg);
    }

    /**
     * 新增商品图片
     * 
     * @param goodsImg 商品图片
     * @return 结果
     */
    @Override
    public int insertGoodsImg(GoodsImg goodsImg)
    {
        return goodsImgMapper.insertGoodsImg(goodsImg);
    }

    /**
     * 修改商品图片
     * 
     * @param goodsImg 商品图片
     * @return 结果
     */
    @Override
    public int updateGoodsImg(GoodsImg goodsImg)
    {
        return goodsImgMapper.updateGoodsImg(goodsImg);
    }

    /**
     * 批量删除商品图片
     * 
     * @param ids 需要删除的商品图片主键
     * @return 结果
     */
    @Override
    public int deleteGoodsImgByIds(Long[] ids)
    {
        return goodsImgMapper.deleteGoodsImgByIds(ids);
    }

    /**
     * 删除商品图片信息
     * 
     * @param id 商品图片主键
     * @return 结果
     */
    @Override
    public int deleteGoodsImgById(Long id)
    {
        return goodsImgMapper.deleteGoodsImgById(id);
    }
}
