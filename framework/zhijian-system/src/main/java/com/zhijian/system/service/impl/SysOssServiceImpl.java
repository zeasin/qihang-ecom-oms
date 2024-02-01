package com.zhijian.system.service.impl;

import java.util.List;
import com.zhijian.common.utils.DateUtils;
import com.zhijian.system.domain.SysOss;
import com.zhijian.system.mapper.SysOssMapper;
import com.zhijian.system.service.ISysOssService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


/**
 * 文件Service业务层处理
 * 
 * @author qihang
 * @date 2024-01-15
 */
@Service
public class SysOssServiceImpl implements ISysOssService
{
    @Autowired
    private SysOssMapper sysOssMapper;

    /**
     * 查询文件
     * 
     * @param ossId 文件主键
     * @return 文件
     */
    @Override
    public SysOss selectSysOssByOssId(Long ossId)
    {
        return sysOssMapper.selectSysOssByOssId(ossId);
    }

    /**
     * 查询文件列表
     * 
     * @param sysOss 文件
     * @return 文件
     */
    @Override
    public List<SysOss> selectSysOssList(SysOss sysOss)
    {
        return sysOssMapper.selectSysOssList(sysOss);
    }

    /**
     * 新增文件
     * 
     * @param sysOss 文件
     * @return 结果
     */
    @Override
    public int insertSysOss(SysOss sysOss)
    {
        sysOss.setCreateTime(DateUtils.getNowDate());
        return sysOssMapper.insertSysOss(sysOss);
    }

    /**
     * 修改文件
     * 
     * @param sysOss 文件
     * @return 结果
     */
    @Override
    public int updateSysOss(SysOss sysOss)
    {
        sysOss.setUpdateTime(DateUtils.getNowDate());
        return sysOssMapper.updateSysOss(sysOss);
    }

    /**
     * 批量删除文件
     * 
     * @param ossIds 需要删除的文件主键
     * @return 结果
     */
    @Override
    public int deleteSysOssByOssIds(Long[] ossIds)
    {
        return sysOssMapper.deleteSysOssByOssIds(ossIds);
    }

    /**
     * 删除文件信息
     * 
     * @param ossId 文件主键
     * @return 结果
     */
    @Override
    public int deleteSysOssByOssId(Long ossId)
    {
        return sysOssMapper.deleteSysOssByOssId(ossId);
    }
}
