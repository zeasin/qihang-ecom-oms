package com.zhijian.system.service;

import com.zhijian.system.domain.SysOss;

import java.util.List;


/**
 * 文件Service接口
 * 
 * @author qihang
 * @date 2024-01-15
 */
public interface ISysOssService 
{
    /**
     * 查询文件
     * 
     * @param ossId 文件主键
     * @return 文件
     */
    public SysOss selectSysOssByOssId(Long ossId);

    /**
     * 查询文件列表
     * 
     * @param sysOss 文件
     * @return 文件集合
     */
    public List<SysOss> selectSysOssList(SysOss sysOss);

    /**
     * 新增文件
     * 
     * @param sysOss 文件
     * @return 结果
     */
    public int insertSysOss(SysOss sysOss);

    /**
     * 修改文件
     * 
     * @param sysOss 文件
     * @return 结果
     */
    public int updateSysOss(SysOss sysOss);

    /**
     * 批量删除文件
     * 
     * @param ossIds 需要删除的文件主键集合
     * @return 结果
     */
    public int deleteSysOssByOssIds(Long[] ossIds);

    /**
     * 删除文件信息
     * 
     * @param ossId 文件主键
     * @return 结果
     */
    public int deleteSysOssByOssId(Long ossId);
}
