package cn.qihangerp.service;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.model.bo.MerchantAddBo;
import cn.qihangerp.model.entity.ErpMerchant;
import cn.qihangerp.model.query.MerchantQuery;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;

/**
* @author qilip
* @description 针对表【oms_tenant(租户用户表)】的数据库操作Service
* @createDate 2024-06-23 11:10:08
*/
public interface ErpMerchantService extends IService<ErpMerchant> {
    PageResult<ErpMerchant> queryPageList(MerchantQuery bo, PageQuery pageQuery);
    ErpMerchant selectUserByUserName(String userName);
    void updateByUserId(ErpMerchant tenant, Long userId);
    ResultVo<ErpMerchant> add(MerchantAddBo bo,String createBy);

}
