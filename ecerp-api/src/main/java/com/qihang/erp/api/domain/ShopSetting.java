package com.qihang.erp.api.domain;

import com.zhijian.common.annotation.Excel;
import com.zhijian.common.core.domain.BaseEntity;

/**
 * 第三方平台设置对象 s_shop_setting
 * 
 * @author qihang
 * @date 2024-01-18
 */
public class ShopSetting extends BaseEntity
{
    private static final long serialVersionUID = 1L;

    /** 主键 */
    private Long id;

    /** 配置名 */
    @Excel(name = "配置名")
    private String name;

    /** appKey */
    @Excel(name = "appKey")
    private String appKey;

    /** appSecret */
    @Excel(name = "appSecret")
    private String appSecret;

    /** 阿里access token */
    @Excel(name = "阿里access token")
    private String accessToken;

    /** 到期 */
    @Excel(name = "到期")
    private Long expiresIn;

    /** access_token开始时间 */
    @Excel(name = "access_token开始时间")
    private Long accessTokenBegin;

    /** 刷新token */
    @Excel(name = "刷新token")
    private String refreshToken;

    /** 刷新token过期时间 */
    @Excel(name = "刷新token过期时间")
    private Long refreshTokenTimeout;

    /** 更新时间 */
    @Excel(name = "更新时间")
    private Long modifyOn;

    /** 请求url */
    @Excel(name = "请求url")
    private String requestUrl;

    /** 第三方店铺id */
    @Excel(name = "第三方店铺id")
    private String thirdId;

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
    public void setAppKey(String appKey) 
    {
        this.appKey = appKey;
    }

    public String getAppKey() 
    {
        return appKey;
    }
    public void setAppSecret(String appSecret) 
    {
        this.appSecret = appSecret;
    }

    public String getAppSecret() 
    {
        return appSecret;
    }
    public void setAccessToken(String accessToken) 
    {
        this.accessToken = accessToken;
    }

    public String getAccessToken() 
    {
        return accessToken;
    }
    public void setExpiresIn(Long expiresIn) 
    {
        this.expiresIn = expiresIn;
    }

    public Long getExpiresIn() 
    {
        return expiresIn;
    }
    public void setAccessTokenBegin(Long accessTokenBegin) 
    {
        this.accessTokenBegin = accessTokenBegin;
    }

    public Long getAccessTokenBegin() 
    {
        return accessTokenBegin;
    }
    public void setRefreshToken(String refreshToken) 
    {
        this.refreshToken = refreshToken;
    }

    public String getRefreshToken() 
    {
        return refreshToken;
    }
    public void setRefreshTokenTimeout(Long refreshTokenTimeout) 
    {
        this.refreshTokenTimeout = refreshTokenTimeout;
    }

    public Long getRefreshTokenTimeout() 
    {
        return refreshTokenTimeout;
    }
    public void setModifyOn(Long modifyOn) 
    {
        this.modifyOn = modifyOn;
    }

    public Long getModifyOn() 
    {
        return modifyOn;
    }
    public void setRequestUrl(String requestUrl) 
    {
        this.requestUrl = requestUrl;
    }

    public String getRequestUrl() 
    {
        return requestUrl;
    }
    public void setThirdId(String thirdId) 
    {
        this.thirdId = thirdId;
    }

    public String getThirdId() 
    {
        return thirdId;
    }

}
