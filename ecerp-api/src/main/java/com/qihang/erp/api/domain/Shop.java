package com.qihang.erp.api.domain;

import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 店铺对象 s_shop
 * 
 * @author qihang
 * @date 2023-12-31
 */
public class Shop extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 店铺名 */
    @Excel(name = "店铺名")
    private String name;

    /** 店铺别名 */
    @Excel(name = "店铺别名")
    private String nickName;

    /** 标识 */
    @Excel(name = "标识")
    private String ename;

    /** 店铺主题 */
    @Excel(name = "店铺主题")
    private String company;

    /** 对应第三方平台Id */
    @Excel(name = "对应第三方平台Id")
    private Long type;

    /** 店铺url */
    @Excel(name = "店铺url")
    private String url;

    /** 排序 */
    @Excel(name = "排序")
    private Long orderNum;

    /** 是否删除0否1是 */
    @Excel(name = "是否删除0否1是")
    private Long isDelete;

    /** 是否显示(0：是1否） */
    @Excel(name = "是否显示(0：是1否）")
    private Long isShow;

    /** 更新时间 */
    @Excel(name = "更新时间")
    private Long modifyOn;

    /** 第三方平台店铺id，淘宝天猫开放平台使用 */
    @Excel(name = "第三方平台店铺id，淘宝天猫开放平台使用")
    private Long sellerUserId;

    /** 卖家userId */
    @Excel(name = "卖家userId")
    private String sellerUserIdStr;

    /** 第三方平台sessionKey */
    @Excel(name = "第三方平台sessionKey")
    private String sessionKey;

    /** Appkey暂时抖音用 */
    @Excel(name = "Appkey暂时抖音用")
    private String appkey;

    /** Appsercet暂时抖音用 */
    @Excel(name = "Appsercet暂时抖音用")
    private String appSercet;

    // 到期
    private Long expiresIn;

    // access_token开始时间
    private Long accessTokenBegin;

    // 刷新token
    private String refreshToken;

    //刷新token过期时间
    private Long refreshTokenTimeout;
    // API request url 请求url
    private String apiRequestUrl;

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public Long getAccessTokenBegin() {
        return accessTokenBegin;
    }

    public void setAccessTokenBegin(Long accessTokenBegin) {
        this.accessTokenBegin = accessTokenBegin;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Long getRefreshTokenTimeout() {
        return refreshTokenTimeout;
    }

    public void setRefreshTokenTimeout(Long refreshTokenTimeout) {
        this.refreshTokenTimeout = refreshTokenTimeout;
    }

    public String getApiRequestUrl() {
        return apiRequestUrl;
    }

    public void setApiRequestUrl(String apiRequestUrl) {
        this.apiRequestUrl = apiRequestUrl;
    }

    public void setId(Long id)
    {
        this.id = id;
    }

    public Long getId() 
    {
        return id;
    }
    public void setName(String name) 
    {
        this.name = name;
    }

    public String getName() 
    {
        return name;
    }
    public void setNickName(String nickName) 
    {
        this.nickName = nickName;
    }

    public String getNickName() 
    {
        return nickName;
    }
    public void setEname(String ename) 
    {
        this.ename = ename;
    }

    public String getEname() 
    {
        return ename;
    }
    public void setCompany(String company) 
    {
        this.company = company;
    }

    public String getCompany() 
    {
        return company;
    }
    public void setType(Long type) 
    {
        this.type = type;
    }

    public Long getType() 
    {
        return type;
    }
    public void setUrl(String url) 
    {
        this.url = url;
    }

    public String getUrl() 
    {
        return url;
    }
    public void setOrderNum(Long orderNum) 
    {
        this.orderNum = orderNum;
    }

    public Long getOrderNum() 
    {
        return orderNum;
    }
    public void setIsDelete(Long isDelete) 
    {
        this.isDelete = isDelete;
    }

    public Long getIsDelete() 
    {
        return isDelete;
    }
    public void setIsShow(Long isShow) 
    {
        this.isShow = isShow;
    }

    public Long getIsShow() 
    {
        return isShow;
    }
    public void setModifyOn(Long modifyOn) 
    {
        this.modifyOn = modifyOn;
    }

    public Long getModifyOn() 
    {
        return modifyOn;
    }
    public void setSellerUserId(Long sellerUserId) 
    {
        this.sellerUserId = sellerUserId;
    }

    public Long getSellerUserId() 
    {
        return sellerUserId;
    }
    public void setSellerUserIdStr(String sellerUserIdStr) 
    {
        this.sellerUserIdStr = sellerUserIdStr;
    }

    public String getSellerUserIdStr() 
    {
        return sellerUserIdStr;
    }
    public void setSessionKey(String sessionKey) 
    {
        this.sessionKey = sessionKey;
    }

    public String getSessionKey() 
    {
        return sessionKey;
    }
    public void setAppkey(String appkey) 
    {
        this.appkey = appkey;
    }

    public String getAppkey() 
    {
        return appkey;
    }
    public void setAppSercet(String appSercet) 
    {
        this.appSercet = appSercet;
    }

    public String getAppSercet() 
    {
        return appSercet;
    }

}
