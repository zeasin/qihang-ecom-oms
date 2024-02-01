package com.qihang.erp.api.task;

import org.springframework.stereotype.Component;

/**
 * 库存报表生成任务
 */
@Component("inventoryReportTask")
public class InventoryReportTask {
    public void run()
    {
        System.out.println("执行仓库库存报表生成任务：日报");
    }

}
