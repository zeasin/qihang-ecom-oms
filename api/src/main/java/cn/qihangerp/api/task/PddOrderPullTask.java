package cn.qihangerp.api.task;

import cn.qihangerp.api.pdd.PddOrderApiService;
import cn.qihangerp.common.enums.EnumShopType;
import cn.qihangerp.common.task.IPollableService;
import cn.qihangerp.model.entity.OShop;
import cn.qihangerp.model.entity.OShopPlatform;
import cn.qihangerp.model.entity.SysTask;
import cn.qihangerp.module.service.OShopPlatformService;
import cn.qihangerp.module.service.OShopPullLogsService;
import cn.qihangerp.module.service.OShopService;
import cn.qihangerp.module.service.SysTaskService;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

@Slf4j
@AllArgsConstructor
@Service
public class PddOrderPullTask implements IPollableService {
    private final SysTaskService taskService;
    private final OShopService shopService;
    private final OShopPlatformService platformService;
    private final OShopPullLogsService pullLogsService;
    private final PddOrderApiService pddOrderApiService;

    @Override
    public void poll() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        ZoneId zoneId = ZoneId.of("Asia/Shanghai");
        log.info("=======自动任务==PDD===拉取订单=========" + LocalDateTime.now(zoneId).format(formatter));
        Date currDateTime = new Date();
        long beginTime = System.currentTimeMillis();
        // 判断设置
        OShopPlatform platform = platformService.selectById(EnumShopType.PDD.getIndex());
        String commonAppKey = platform.getAppKey();
        String commonAppSecret = platform.getAppSecret();
//        if (!StringUtils.hasText(platform.getAppKey()) || !StringUtils.hasText(platform.getAppSecret()) || !StringUtils.hasText(platform.getServerUrl())) {
//            // 平台参数错误
//            OShopPullLogs logs = new OShopPullLogs(0L, EnumShopType.PDD.getIndex(), "ORDER", "定时任务拉取订单", "", "平台参数错误", currDateTime, System.currentTimeMillis() - beginTime);
//            pullLogsService.save(logs);
//            log.info("平台参数设置错误，请检查！");
//            return;
//        }

        // 查询所有PDD店铺
        List<OShop> shopList = shopService.selectShopByShopType(EnumShopType.PDD);
        if (shopList != null && !shopList.isEmpty()) {
//            OShopPlatform sysPlatform = platformService.selectById(EnumShopType.PDD.getIndex());
//            log.info("=======自动任务==PDD===拉取订单=========循环PDD店铺");
            for (OShop shop : shopList) {
                try {
                    log.info("=======自动任务==PDD===拉取订单=========PDD店铺：{}",shop.getName());
                    if (shop.getApiStatus().intValue() == 1) {
                        String appkey = StringUtils.hasText(shop.getAppKey())?shop.getAppKey():commonAppKey;
                        String appsecret = StringUtils.hasText(shop.getAppSecret())?shop.getAppSecret():commonAppSecret;
                        //拉取新订单
                        pddOrderApiService.pullOrder( shop.getId(), appkey, appsecret, shop.getAccessToken());

                    } else {
                        log.error("========未开启自动任务，手动获取token之后自动开启==========");
                    }
                }catch (Exception e){
                    e.printStackTrace();
                    log.info("=======自动任务==PDD===拉取订单异常========="+e.getMessage());
                }
            }

        } else {
            log.info("=======自动任务==PDD===拉取订单=========没有找到PDD店铺");
        }


    }

    @Override
    public String getCronExpression() {
        SysTask task = taskService.getById(3);
        if(task!=null&&task.getStatus().intValue() == 1) {
            return task.getCron();
        }else return "-";
    }
}