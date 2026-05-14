package cn.qihangerp.service;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.model.entity.ErpBillShopAccounts;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author qilip
* @description 针对表【erp_bill_shop_accounts(店铺账目表)】的数据库操作Service
* @createDate 2025-05-28 15:02:57
*/
public interface ErpBillShopAccountsService extends IService<ErpBillShopAccounts> {
    PageResult<ErpBillShopAccounts> queryPageList(ErpBillShopAccounts bo, PageQuery pageQuery);
}
