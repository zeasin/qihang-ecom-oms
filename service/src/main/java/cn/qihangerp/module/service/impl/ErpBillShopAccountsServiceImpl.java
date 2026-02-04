package cn.qihangerp.module.service.impl;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.mapper.ErpBillShopAccountsMapper;
import cn.qihangerp.model.entity.ErpBillShopAccounts;
import cn.qihangerp.module.service.ErpBillShopAccountsService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

/**
* @author qilip
* @description 针对表【erp_bill_shop_accounts(店铺账目表)】的数据库操作Service实现
* @createDate 2025-05-28 15:02:57
*/
@Service
public class ErpBillShopAccountsServiceImpl extends ServiceImpl<ErpBillShopAccountsMapper, ErpBillShopAccounts>
    implements ErpBillShopAccountsService {

    @Override
    public PageResult<ErpBillShopAccounts> queryPageList(ErpBillShopAccounts bo, PageQuery pageQuery) {
        LambdaQueryWrapper<ErpBillShopAccounts> queryWrapper = new LambdaQueryWrapper<ErpBillShopAccounts>()
                .eq(ErpBillShopAccounts::getTenantId,bo.getTenantId())
                .eq(StringUtils.hasText(bo.getDate()),ErpBillShopAccounts::getDate,bo.getDate())
                .eq(bo.getShopId() != null, ErpBillShopAccounts::getShopId, bo.getShopId())
                .eq(StringUtils.hasText(bo.getUsageScenario()), ErpBillShopAccounts::getUsageScenario, bo.getUsageScenario())
                .eq(bo.getType() != null, ErpBillShopAccounts::getType, bo.getType());

        Page<ErpBillShopAccounts> pages = this.baseMapper.selectPage(pageQuery.build(), queryWrapper);
        return PageResult.build(pages);
    }
}




