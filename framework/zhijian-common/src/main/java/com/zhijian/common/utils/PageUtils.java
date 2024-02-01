package com.zhijian.common.utils;

import com.github.pagehelper.PageHelper;
import com.zhijian.common.core.page.PageDomain;
import com.zhijian.common.core.page.TableSupport;
import com.zhijian.common.utils.sql.SqlUtil;

/**
 * 分页工具类
 * 
 * @author qihang
 */
public class PageUtils extends PageHelper
{
    /**
     * 设置请求分页数据
     */
    public static void startPage()
    {
//        PageDomain pageDomain = TableSupport.buildPageRequest();
//        Integer pageNum = pageDomain.getPageNum();
//        Integer pageSize = pageDomain.getPageSize();
//        String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
//        if(StringUtils.isEmpty(orderBy)){
//            orderBy = " ID DESC ";
//        }
//        Boolean reasonable = pageDomain.getReasonable();
//        PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
        startPage(true);
    }

    public static void startPage(boolean isIdDesc)
    {
        PageDomain pageDomain = TableSupport.buildPageRequest();
        Integer pageNum = pageDomain.getPageNum();
        Integer pageSize = pageDomain.getPageSize();
        String orderBy = SqlUtil.escapeOrderBySql(pageDomain.getOrderBy());
        if(isIdDesc) {
            if (StringUtils.isEmpty(orderBy)) {
                orderBy = " ID DESC ";
            }
        }
        Boolean reasonable = pageDomain.getReasonable();
        PageHelper.startPage(pageNum, pageSize, orderBy).setReasonable(reasonable);
    }

    /**
     * 清理分页的线程变量
     */
    public static void clearPage()
    {
        PageHelper.clearPage();
    }
}
