package cn.qihangerp.service.impl;

import cn.qihangerp.common.PageQuery;
import cn.qihangerp.common.PageResult;
import cn.qihangerp.common.ResultVo;
import cn.qihangerp.mapper.ErpMerchantMapper;
import cn.qihangerp.mapper.ErpWarehouseMapper;
import cn.qihangerp.model.entity.ErpMerchant;
import cn.qihangerp.model.entity.ErpWarehouse;
import cn.qihangerp.model.request.WarehouseCloudQuery;
import cn.qihangerp.model.request.WarehousePageQuery;
import cn.qihangerp.service.ErpWarehouseService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
* @author qilip
* @description 针对表【erp_cloud_warehouse】的数据库操作Service实现
* @createDate 2025-07-07 15:10:27
*/
@Slf4j
@AllArgsConstructor
@Service
public class ErpWarehouseServiceImpl extends ServiceImpl<ErpWarehouseMapper, ErpWarehouse>
    implements ErpWarehouseService {
    private final ErpMerchantMapper merchantMapper;


    @Override
    public PageResult<ErpWarehouse> queryPageList(WarehousePageQuery query ) {
        LambdaQueryWrapper<ErpWarehouse> qw = new LambdaQueryWrapper<ErpWarehouse>()
//                .eq(query.getMerchantId()!=null && userIdentity == 0,ErpWarehouse::getMerchantId, query.getMerchantId())
//                .like(query.getMerchantId()!=null && userIdentity == 20,ErpWarehouse::getMerchantIds, "," + query.getMerchantId() + ",")
//                .eq(userIdentity==null&&(userIdentity!=0&&userIdentity!=20),ErpWarehouse::getMerchantId,-1)
                .eq(query.getMerchantId() != null, ErpWarehouse::getMerchantId, query.getMerchantId())
                .eq(query.getShopId() != null, ErpWarehouse::getShopId, query.getShopId())
                .eq(query.getStatus() != null, ErpWarehouse::getStatus, query.getStatus())
                .eq(StringUtils.hasText(query.getWarehouseType()), ErpWarehouse::getWarehouseType, query.getWarehouseType())
                .like(StringUtils.hasText(query.getWarehouseNo()), ErpWarehouse::getWarehouseNo, query.getWarehouseNo())
                .like(StringUtils.hasText(query.getWarehouseName()), ErpWarehouse::getWarehouseName, query.getWarehouseName());

        PageQuery pageQuery = new PageQuery();
        BeanUtils.copyProperties(query, pageQuery);
        Page<ErpWarehouse> pages = this.baseMapper.selectPage(pageQuery.build(), qw);
//        Page<WarehouseListVO> pagesResult = new Page<>();
//        BeanUtils.copyProperties(pages, pagesResult);
//
//        if(pages.getRecords()!=null && pages.getRecords().size()>0){
//            List<WarehouseListVO> listVO = warehouseDataConverter.toListVO(pages.getRecords());
//            pagesResult.setRecords(listVO);
//        }
        return PageResult.build(pages);
    }



    @Override
    public List<ErpWarehouse> getCloudWarehouseList(WarehouseCloudQuery query) {
        LambdaQueryWrapper<ErpWarehouse> qw = new LambdaQueryWrapper<ErpWarehouse>()
                .eq(ErpWarehouse::getMerchantId, query.getMerchantId())
                .eq(ErpWarehouse::getType,2)
                .eq(query.getStatus() != null, ErpWarehouse::getStatus, query.getStatus());
        var list = this.baseMapper.selectList(qw);
//        List<WarehouseListVO> listVO = warehouseDataConverter.toListVO(list);
//        return listVO;
        return list;
    }

    @Override
    public List<ErpWarehouse> getJdWarehouseList(ErpWarehouse bo) {
        List<ErpWarehouse> erpWarehouses = this.baseMapper.selectList(new LambdaQueryWrapper<ErpWarehouse>()
                        .eq( bo.getMerchantId()!=null, ErpWarehouse::getMerchantId, bo.getMerchantId())
                        .eq(bo.getId()!=null,ErpWarehouse::getId, bo.getId())
                        .eq(ErpWarehouse::getType,2)
                        .eq(ErpWarehouse::getWarehouseType, "JDYC")
        );
        return erpWarehouses;
    }

    @Override
    public List<ErpWarehouse> getJkyWarehouseList(ErpWarehouse bo) {
        List<ErpWarehouse> erpWarehouses = this.baseMapper.selectList(new LambdaQueryWrapper<ErpWarehouse>()
                .eq( bo.getMerchantId()!=null, ErpWarehouse::getMerchantId, bo.getMerchantId())
                .eq(bo.getId()!=null,ErpWarehouse::getId, bo.getId())
                .eq(ErpWarehouse::getType,2)
                .eq(ErpWarehouse::getWarehouseType, "JKYYC")
        );
        return erpWarehouses;
    }

    @Override
    public List<ErpWarehouse> getWarehouseList(ErpWarehouse bo) {
        List<ErpWarehouse> erpWarehouses = this.baseMapper.selectList(new LambdaQueryWrapper<ErpWarehouse>()
                .eq(bo.getMerchantId()!=null, ErpWarehouse::getMerchantId, bo.getMerchantId())
                .eq(bo.getShopId()!=null, ErpWarehouse::getShopId, bo.getShopId())
                .eq(bo.getType()!=null, ErpWarehouse::getType, bo.getType())
                .eq(StringUtils.hasText(bo.getWarehouseType()), ErpWarehouse::getWarehouseType, bo.getWarehouseType())
        );
        return erpWarehouses;
    }

    @Transactional(rollbackFor = Exception.class)
    @Override
    public ResultVo<Long> saveErpCloudWarehouse(List<ErpWarehouse> list) {
        Long warehouseId = 0L;
        for (ErpWarehouse erpWarehouse : list) {
            List<ErpWarehouse> erpWarehouses = this.baseMapper.selectList(new LambdaQueryWrapper<ErpWarehouse>()
                    .eq(ErpWarehouse::getWarehouseNo, erpWarehouse.getWarehouseNo())
                    .eq(ErpWarehouse::getMerchantId, erpWarehouse.getMerchantId())
                    .eq(ErpWarehouse::getWarehouseType, erpWarehouse.getWarehouseType())
            );
            if (erpWarehouses !=null&& erpWarehouses.size() > 0) {
                erpWarehouse.setId(erpWarehouses.get(0).getId());
                warehouseId = erpWarehouses.get(0).getId();
                this.baseMapper.updateById(erpWarehouse);
            }else{
                this.baseMapper.insert(erpWarehouse);
                warehouseId = erpWarehouse.getId();
            }
        }
//        List<Long> ids = list.stream().map(ErpWarehouse::getId).toList();
//        this.baseMapper.delete(new LambdaQueryWrapper<ErpWarehouse>()
//                .eq(ErpWarehouse::getMerchantId, list.get(0).getMerchantId())
//                .notIn(ErpWarehouse::getId,ids)
//        );
        return ResultVo.success(warehouseId);
    }



}




