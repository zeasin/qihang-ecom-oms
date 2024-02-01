package com.qihang.erp.api.mapper;

import java.util.List;
import com.qihang.erp.api.domain.TaoOrderAddress;
import org.apache.ibatis.annotations.Mapper;

/**
 * 淘宝订单地址Mapper接口
 * 
 * @author qihang
 * @date 2024-01-03
 */
@Mapper
public interface TaoOrderAddressMapper 
{
    /**
     * 查询淘宝订单地址
     * 
     * @param id 淘宝订单地址主键
     * @return 淘宝订单地址
     */
    public TaoOrderAddress selectTaoOrderAddressById(Long id);
    public TaoOrderAddress selectTaoOrderAddressByOrderId(String orderId);

    /**
     * 查询淘宝订单地址列表
     * 
     * @param taoOrderAddress 淘宝订单地址
     * @return 淘宝订单地址集合
     */
    public List<TaoOrderAddress> selectTaoOrderAddressList(TaoOrderAddress taoOrderAddress);

    /**
     * 新增淘宝订单地址
     * 
     * @param taoOrderAddress 淘宝订单地址
     * @return 结果
     */
    public int insertTaoOrderAddress(TaoOrderAddress taoOrderAddress);

    /**
     * 修改淘宝订单地址
     * 
     * @param taoOrderAddress 淘宝订单地址
     * @return 结果
     */
    public int updateTaoOrderAddress(TaoOrderAddress taoOrderAddress);

    /**
     * 删除淘宝订单地址
     * 
     * @param id 淘宝订单地址主键
     * @return 结果
     */
    public int deleteTaoOrderAddressById(Long id);

    /**
     * 批量删除淘宝订单地址
     * 
     * @param ids 需要删除的数据主键集合
     * @return 结果
     */
    public int deleteTaoOrderAddressByIds(Long[] ids);
}
