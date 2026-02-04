package cn.qihangerp.api.jd.controller;

import cn.qihangerp.api.jd.JdApiCommon;
import cn.qihangerp.api.jd.JdPullRequest;
import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.common.enums.EnumShopType;
import cn.qihangerp.common.enums.HttpStatus;
import cn.qihangerp.model.entity.OShopPullLasttime;
import cn.qihangerp.model.entity.OShopPullLogs;
import cn.qihangerp.model.entity.JdGoodsSku;
import cn.qihangerp.module.service.JdGoodsService;
import cn.qihangerp.module.service.OShopPullLasttimeService;
import cn.qihangerp.module.service.OShopPullLogsService;
import cn.qihangerp.open.common.ApiResultVo;
import cn.qihangerp.open.jd.JdGoodsApiHelper;
import cn.qihangerp.open.jd.response.JdGoodsSkuListResponse;
import lombok.AllArgsConstructor;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.Date;

@RequestMapping("/jd/goods")
@RestController
@AllArgsConstructor
public class JdGoodsApiController {
    private final JdApiCommon jdApiCommon;
    private final JdGoodsService goodsService;
    private final OShopPullLogsService pullLogsService;
    private final OShopPullLasttimeService pullLasttimeService;



    /**
     * 拉取商品列表（包含sku）
     * @param params
     * @return
     * @throws Exception
     */
    @RequestMapping(value = "/pull_goods", method = RequestMethod.POST)
    public AjaxResult pullSkuList(@RequestBody JdPullRequest params) throws Exception {
        if (params.getShopId() == null || params.getShopId() <= 0) {
            return AjaxResult.error(HttpStatus.PARAMS_ERROR, "参数错误，没有店铺Id");
        }
        Long currTimeMillis = System.currentTimeMillis();
        Date currDateTime = new Date();
        var checkResult = jdApiCommon.checkBefore(params.getShopId());
        if (checkResult.getCode() != HttpStatus.SUCCESS) {
            return AjaxResult.error(checkResult.getCode(), checkResult.getMsg(), checkResult.getData());
        }
        String accessToken = checkResult.getData().getAccessToken();
        String serverUrl = checkResult.getData().getServerUrl();
        String appKey = checkResult.getData().getAppKey();
        String appSecret = checkResult.getData().getAppSecret();
        // 获取最后更新时间
        LocalDateTime startTime = null;
        LocalDateTime endTime = null;
        OShopPullLasttime lasttime = pullLasttimeService.getLasttimeByShop(params.getShopId(), "GOODS");
        if(params.getPullType()!=null && params.getPullType()==1) {
            if (lasttime != null) {
                // 按更新时间来
                startTime = lasttime.getLasttime().minusHours(7*24);//取上次结束一个小时前
                endTime = LocalDateTime.now();
            }
        }
//        String pullParams = "{PageNo:1,PageSize:50,startTime:"+startTime+",endTime:"+endTime+"}";
        String pullParams = "{startTime:"+startTime+",endTime:"+endTime+"}";


        ApiResultVo<JdGoodsSkuListResponse> resultVo = JdGoodsApiHelper.pullGoodsSkuList(appKey, appSecret, accessToken);
        if(resultVo.getCode() !=0 ){
            OShopPullLogs logs = new OShopPullLogs();
            logs.setShopId(params.getShopId());
            logs.setShopType(EnumShopType.JD.getIndex());
            logs.setPullType("GOODS");
            logs.setPullWay("主动拉取商品sku");
            logs.setPullParams(pullParams);
            logs.setPullResult(resultVo.getMsg());
            logs.setPullTime(currDateTime);
            logs.setDuration(System.currentTimeMillis() - currTimeMillis);
            pullLogsService.save(logs);
            return AjaxResult.error("接口拉取错误："+resultVo.getMsg());
        }

        int successTotal = 0;
        for (var sku: resultVo.getList()){
            JdGoodsSku jdGoodsSku = new JdGoodsSku();
            BeanUtils.copyProperties(sku,jdGoodsSku);
            goodsService.saveGoodsSku(params.getShopId(),jdGoodsSku);
            successTotal++;
        }
        // 添加拉取日志
        OShopPullLogs logs = new OShopPullLogs();
        logs.setShopId(params.getShopId());
        logs.setShopType(EnumShopType.JD.getIndex());
        logs.setPullType("GOODS");
        logs.setPullWay("主动拉取商品sku");
        logs.setPullParams(pullParams);
        logs.setPullResult("{successTotal:"+successTotal+"}");
        logs.setPullTime(currDateTime);
        logs.setDuration(System.currentTimeMillis() - currTimeMillis);
        pullLogsService.save(logs);


        if(lasttime == null){
            // 新增
            OShopPullLasttime insertLasttime = new OShopPullLasttime();
            insertLasttime.setShopId(params.getShopId());
            insertLasttime.setCreateTime(new Date());
            insertLasttime.setLasttime(endTime==null?LocalDateTime.now():endTime);
            insertLasttime.setPullType("GOODS");
            pullLasttimeService.save(insertLasttime);

        }else {
            // 修改
            OShopPullLasttime updateLasttime = new OShopPullLasttime();
            updateLasttime.setId(lasttime.getId());
            updateLasttime.setUpdateTime(new Date());
            updateLasttime.setLasttime(endTime==null?LocalDateTime.now():endTime);
            pullLasttimeService.updateById(updateLasttime);
        }
        return AjaxResult.success("接口拉取成功，总数据："+successTotal);
    }
}
