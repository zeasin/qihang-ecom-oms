package cn.qihangerp.mapper;

import cn.qihangerp.model.entity.ShopOrder;
import cn.qihangerp.model.query.ShopOrderQueryBo;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import org.apache.ibatis.annotations.Param;

/**
* @author qilip
* @description 针对表【oms_shop_order】的数据库操作Mapper
* @createDate 2025-07-15 11:36:42
* @Entity cn.qihangerp.model.entity.ShopOrder
*/
public interface ShopOrderMapper extends BaseMapper<ShopOrder> {
    Page<ShopOrder> searchOrderByGoodsParam(@Param("page") Page<ShopOrder> page, @Param("qw") ShopOrderQueryBo qw);
}




