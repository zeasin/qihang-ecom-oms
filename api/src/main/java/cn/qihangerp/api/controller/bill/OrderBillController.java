package cn.qihangerp.api.controller.bill;


import cn.qihangerp.common.AjaxResult;
import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.TableDataInfo;
import cn.qihangerp.model.entity.ErpBillShopOrder;
import cn.qihangerp.model.entity.OShop;
import cn.qihangerp.service.ErpBillShopOrderService;
import cn.qihangerp.service.OShopService;
import cn.qihangerp.security.common.BaseController;
import com.alibaba.fastjson2.JSONObject;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

@Slf4j
@AllArgsConstructor
@RestController
@RequestMapping("/financial")
public class OrderBillController extends BaseController {
    private final OShopService shopService;
    private final ErpBillShopOrderService erpBillShopOrderService;
    /**
     * 查询订单账单
     */
    @GetMapping("/order_bill/list")
    public TableDataInfo list(ErpBillShopOrder goods, PageQuery pageQuery)
    {
        if(getUserId()!=1) {
            goods.setTenantId(getUserId());
        }
        PageResult<ErpBillShopOrder> pageResult = erpBillShopOrderService.queryPageList(goods, pageQuery);
        return getDataTable(pageResult);
    }

    @DeleteMapping("/order_bill/del/{ids}")
    public AjaxResult remove(@PathVariable Long[] ids)
    {
        if(getUserId()==1) {
            return AjaxResult.error("超级管理员账号不允许操作");
        }
        return toAjax(erpBillShopOrderService.removeBatchByIds(Arrays.stream(ids).toList()));
    }

    /**
     *
     * @param file
     * @return
     * @throws IOException
     * @throws InvalidFormatException
     */
    @RequestMapping(value = "/order_bill/pdd_bill_import", method = RequestMethod.POST)
    public AjaxResult orderSendExcel(@RequestPart("file") MultipartFile file, @RequestParam Long shopId) throws IOException, InvalidFormatException {
        OShop shop = shopService.getById(shopId);
        if(shop == null){
            return AjaxResult.error("店铺不存在");
        }
        String fileName = file.getOriginalFilename();
        String dir = System.getProperty("user.dir");
        String csvFile = dir + File.separator + "import\\pdd_bill_" + fileName;
        System.out.println(csvFile);
        List<String[]> newData = new ArrayList<>();
        try {
            // 获取文件的输入流
            InputStreamReader inputStreamReader = new InputStreamReader(file.getInputStream(),"GBK");

            // 使用 OpenCSV 解析 CSV 文件
            CSVReader csvReader = new CSVReader(inputStreamReader);
            List<String[]> csvData = csvReader.readAll(); // 读取所有的 CSV 数据

            // 遍历 CSV 数据并打印
            int i=0;
            for (String[] row : csvData) {

                if (i > 4 && i<csvData.size()-5) {
                    newData.add(row);
                    System.out.println(i);
//                    for (String cell : row) {
//                        System.out.print(cell + "\t");
//                    }
                }
                i++;
//                System.out.println(i);
            }
            // 关闭 CSVReader
            csvReader.close();

        } catch (IOException e) {
            e.printStackTrace();

        } catch (CsvException e) {
            e.printStackTrace();
        }
        log.info("======={}", JSONObject.toJSONString(newData));
        for (String[] row : newData) {

            String date = row[1];
            String income = row[2];
            String outcome = row[3];
            String bizType = row[4];
            String remark = row[5];
            String detail = row[6];
            ErpBillShopOrder bill = new ErpBillShopOrder();
            bill.setShopId(shopId);
            bill.setShopType(shop.getType());
            bill.setTenantId(0L);
            bill.setOrderId(row[0]);
            Integer type=null;
            Float amount=null;
            try{
               if(Float.parseFloat(income)>0){
                   type=1;
                   amount=Float.parseFloat(income);
               } else if (Float.parseFloat(outcome)<0) {
                   type=2;
                   amount=0-Float.parseFloat(outcome);
               }
            }catch (Exception e){}
            bill.setType(type);
            bill.setAmount(amount.doubleValue());
            bill.setBizTime(date);
            bill.setBizType(bizType);
            bill.setRemark(remark);
            bill.setDetail(detail);
            bill.setCreateTime(new Date());
            erpBillShopOrderService.save(bill);
        }
        Map<String, Integer> result = new HashMap<>();
        result.put("success",0);
        result.put("fail",0);
        return AjaxResult.success(result);
    }
}
