package cn.qihangerp.module.service.impl;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVoEnum;
import cn.qihangerp.mapper.TaoGoodsSkuMapper;
import cn.qihangerp.mapper.TaoRefundMapper;
import cn.qihangerp.model.entity.TaoGoodsSku;
import cn.qihangerp.model.entity.TaoOrder;
import cn.qihangerp.model.entity.TaoRefund;
import cn.qihangerp.model.bo.TaoRefundBo;
import cn.qihangerp.module.service.TaoRefundService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
* @author TW
* @description 针对表【tao_refund(淘宝退款订单表)】的数据库操作Service实现
* @createDate 2024-02-29 19:01:45
*/
@AllArgsConstructor
@Service
public class TaoRefundServiceImpl extends ServiceImpl<TaoRefundMapper, TaoRefund>
    implements TaoRefundService {
    private final TaoGoodsSkuMapper goodsSkuMapper;
    private final TaoRefundMapper mapper;
    private final String DATE_PATTERN =
            "^(?:(?:(?:\\d{4}-(?:0?[1-9]|1[0-2])-(?:0?[1-9]|1\\d|2[0-8]))|(?:(?:(?:\\d{2}(?:0[48]|[2468][048]|[13579][26])|(?:(?:0[48]|[2468][048]|[13579][26])00))-0?2-29))$)|(?:(?:(?:\\d{4}-(?:0?[13578]|1[02]))-(?:0?[1-9]|[12]\\d|30))$)|(?:(?:(?:\\d{4}-0?[13-9]|1[0-2])-(?:0?[1-9]|[1-2]\\d|30))$)|(?:(?:(?:\\d{2}(?:0[48]|[13579][26]|[2468][048])|(?:(?:0[48]|[13579][26]|[2468][048])00))-0?2-29))$)$";
    private final Pattern DATE_FORMAT = Pattern.compile(DATE_PATTERN);
    @Override
    public PageResult<TaoRefund> queryPageList(TaoRefundBo bo, PageQuery pageQuery) {
        if(StringUtils.hasText(bo.getUpdateTime())){
            Matcher matcher = DATE_FORMAT.matcher(bo.getUpdateTime());
            boolean b = matcher.find();
            if(!b){
                bo.setUpdateTime("");
            }
        }

        LambdaQueryWrapper<TaoRefund> queryWrapper = new LambdaQueryWrapper<TaoRefund>()
                .eq(bo.getShopId()!=null,TaoRefund::getShopId,bo.getShopId())
                .eq(StringUtils.hasText(bo.getRefundId()),TaoRefund::getRefundId,bo.getRefundId())
                .eq(StringUtils.hasText(bo.getTid()),TaoRefund::getTid,bo.getTid())
                .eq(StringUtils.hasText(bo.getDisputeType()),TaoRefund::getDisputeType,bo.getDisputeType())
                .ge(StringUtils.hasText(bo.getUpdateTime()), TaoRefund::getCreated,bo.getUpdateTime()+" 00:00:00")
                .le(StringUtils.hasText(bo.getUpdateTime()),TaoRefund::getCreated,bo.getUpdateTime()+" 23:59:59")
                ;

        Page<TaoRefund> page = mapper.selectPage(pageQuery.build(), queryWrapper);

        return PageResult.build(page);
    }

    @Override
    public int saveAndUpdateRefund(Long shopId, TaoRefund refund) {
        List<TaoRefund> taoRefunds = mapper.selectList(new LambdaQueryWrapper<TaoRefund>().eq(TaoRefund::getRefundId, refund.getRefundId()));
        if(taoRefunds!=null && taoRefunds.size()>0){
            // 存在，修改
            TaoRefund update = new TaoRefund();
            update.setId(taoRefunds.get(0).getId());
            if(refund.getSkuId()!=null){
                update.setSkuId(refund.getSkuId());
                List<TaoGoodsSku> pddGoodsSku = goodsSkuMapper.selectList(new LambdaQueryWrapper<TaoGoodsSku>().eq(TaoGoodsSku::getSkuId, refund.getSkuId()));
                if (pddGoodsSku != null && !pddGoodsSku.isEmpty()) {
                    update.setOGoodsId(pddGoodsSku.get(0).getErpGoodsId().toString());
                    update.setOGoodsSkuId(pddGoodsSku.get(0).getErpGoodsSkuId().toString());
                }
            }
            update.setOuterId(refund.getOuterId());
            update.setSku(refund.getSku());
            update.setTid(refund.getTid());
            update.setOid(refund.getOid());
            update.setModified(refund.getModified());
            update.setStatus(refund.getStatus());
            update.setEndTime(refund.getEndTime());
            update.setUpdateTime(new Date());
            mapper.updateById(update);
            return ResultVoEnum.DataExist.getIndex();
        }else{
            refund.setShopId(shopId);
            if(refund.getSkuId()!=null) {
                List<TaoGoodsSku> pddGoodsSku = goodsSkuMapper.selectList(new LambdaQueryWrapper<TaoGoodsSku>().eq(TaoGoodsSku::getSkuId, refund.getSkuId()));
                if (pddGoodsSku != null && !pddGoodsSku.isEmpty()) {
                    refund.setOGoodsId(pddGoodsSku.get(0).getErpGoodsId().toString());
                    refund.setOGoodsSkuId(pddGoodsSku.get(0).getErpGoodsSkuId().toString());
                }
            }
            refund.setCreateTime(new Date());
            mapper.insert(refund);
            return ResultVoEnum.SUCCESS.getIndex();
        }

    }
}




