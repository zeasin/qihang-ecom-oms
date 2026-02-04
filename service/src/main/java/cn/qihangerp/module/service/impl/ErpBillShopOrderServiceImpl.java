package cn.qihangerp.module.service.impl;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.mapper.ErpBillShopOrderBillMapper;
import cn.qihangerp.model.entity.ErpBillShopOrder;
import cn.qihangerp.module.service.ErpBillShopOrderService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author qilip
* @description 针对表【erp_shop_bill(店铺账单)】的数据库操作Service实现
* @createDate 2025-05-26 11:05:47
*/
@Service
public class ErpBillShopOrderServiceImpl extends ServiceImpl<ErpBillShopOrderBillMapper, ErpBillShopOrder>
    implements ErpBillShopOrderService {

    private final String DATE_PATTERN =
            "^(?:(?:(?:\\d{4}-(?:0?[1-9]|1[0-2])-(?:0?[1-9]|1\\d|2[0-8]))|(?:(?:(?:\\d{2}(?:0[48]|[2468][048]|[13579][26])|(?:(?:0[48]|[2468][048]|[13579][26])00))-0?2-29))$)|(?:(?:(?:\\d{4}-(?:0?[13578]|1[02]))-(?:0?[1-9]|[12]\\d|30))$)|(?:(?:(?:\\d{4}-0?[13-9]|1[0-2])-(?:0?[1-9]|[1-2]\\d|30))$)|(?:(?:(?:\\d{2}(?:0[48]|[13579][26]|[2468][048])|(?:(?:0[48]|[13579][26]|[2468][048])00))-0?2-29))$)$";
    private final Pattern DATE_FORMAT = Pattern.compile(DATE_PATTERN);


    @Override
    public PageResult<ErpBillShopOrder> queryPageList(ErpBillShopOrder bo, PageQuery pageQuery) {
        String beginTime ="";
        String endTime = "";
        if(StringUtils.hasText(bo.getBizTime())){
            Matcher matcher = DATE_FORMAT.matcher(bo.getBizTime());
            boolean b = matcher.find();
            if(b){
                beginTime = bo.getBizTime() + " 00:00:00";
                endTime = bo.getBizTime() + " 23:59:59";
            }
        }

        LambdaQueryWrapper<ErpBillShopOrder> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(bo.getTenantId()!=null, ErpBillShopOrder::getTenantId,bo.getTenantId());
        queryWrapper.eq(bo.getShopId()!=null, ErpBillShopOrder::getShopId,bo.getShopId());
        queryWrapper.eq(bo.getType()!=null, ErpBillShopOrder::getType, bo.getType());
        queryWrapper.ge(StringUtils.hasText(beginTime),ErpBillShopOrder::getBizTime,beginTime);
        queryWrapper.le(StringUtils.hasText(endTime),ErpBillShopOrder::getBizTime,endTime);
        queryWrapper.eq(StringUtils.hasText(bo.getOrderId()), ErpBillShopOrder::getOrderId,bo.getOrderId());
        Page<ErpBillShopOrder> pages = this.baseMapper.selectPage(pageQuery.build(), queryWrapper);
        return PageResult.build(pages);
    }
}




