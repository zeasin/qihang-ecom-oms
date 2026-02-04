package cn.qihangerp.module.service.impl;

import cn.qihangerp.mapper.goods.OGoodsSkuMapper;
import cn.qihangerp.model.entity.OGoodsSku;
import cn.qihangerp.module.service.OGoodsSkuService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

/**
* @author TW
* @description 针对表【o_goods_sku(商品规格库存管理)】的数据库操作Service实现
* @createDate 2024-03-11 14:24:49
*/
@AllArgsConstructor
@Service
public class OGoodsSkuServiceImpl extends ServiceImpl<OGoodsSkuMapper, OGoodsSku>
    implements OGoodsSkuService {
    private final OGoodsSkuMapper skuMapper;
    @Override
    public List<OGoodsSku> searchGoodsSpec(String keyword) {
        LambdaQueryWrapper<OGoodsSku> queryWrapper =
                new LambdaQueryWrapper<OGoodsSku>()
                        .likeRight(OGoodsSku::getGoodsId,keyword).or()
                        .likeRight(OGoodsSku::getId,keyword).or()
                        .likeRight(OGoodsSku::getSkuCode,keyword).or()
                        .like(OGoodsSku::getGoodsName,keyword).or()
                        .like(OGoodsSku::getSkuName,keyword)
                ;
        queryWrapper.last("LIMIT 10");
        return skuMapper.selectList(queryWrapper);
    }
}




