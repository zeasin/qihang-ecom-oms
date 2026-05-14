package cn.qihangerp.service;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.model.entity.ErpSupplier;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author qilip
* @description 针对表【o_goods_supplier】的数据库操作Service
* @createDate 2024-09-07 16:35:43
*/
public interface ErpSupplierService extends IService<ErpSupplier> {
    PageResult<ErpSupplier> queryPageList(ErpSupplier bo, PageQuery pageQuery);

}
