package com.qihang.erp.api.mapper;

import java.util.List;
import com.qihang.erp.api.domain.GoodsImg;
import org.apache.ibatis.annotations.Mapper;

/**
 * 商品图片Mapper接口
 * 
 * @author qihang
 * @date 2023-12-29
 */
@Mapper
public interface GoodsImgMapper 
{
    /**
     * 查询商品图片
     * 
     * @param id 商品图片主键
     * @return 商品图片
     */
    public GoodsImg selectGoodsImgById(Long id);

    /**
     * 查询商品图片列表
     * 
     * @param goodsImg 商品图片
     * @return 商品图片集合
     */
    public List<GoodsImg> selectGoodsImgList(GoodsImg goodsImg);

    /**
     * 新增商品图片
     * 
     * @param goodsImg 商品图片
     * @return 结果
     */
    public int insertGoodsImg(GoodsImg goodsImg);

    /**
     * 修改商品图片
     * 
     * @param goodsImg 商品图片
     * @return 结果
     */
    public int updateGoodsImg(GoodsImg goodsImg);

    /**
     * 删除商品图片
     * 
     * @param id 商品图片主键
     * @return 结果
     */
    public int deleteGoodsImgById(Long id);

    /**
     * 批量删除商品图片
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteGoodsImgByIds(Long[] ids);
}
