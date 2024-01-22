package com.qihang.oms.api.controller;

import com.alibaba.nacos.api.common.Constants;
import com.qihang.oms.api.common.AjaxResult;
import com.qihang.oms.api.domain.LoginBody;
import com.qihang.oms.api.domain.SysUser;
import com.qihang.oms.api.service.ISysUserService;
import com.qihang.oms.api.service.SysLoginService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {
    @Autowired
    private SysLoginService loginService;
    @Autowired
    private ISysUserService userService;
    @PostMapping(value = "/login")
    public AjaxResult login(@RequestBody LoginBody loginBody){
        AjaxResult ajax = AjaxResult.success();
//        SysUser user = userService.selectUserByUserName(loginBody.getUsername());
        // 生成令牌
        String token = loginService.login(loginBody.getUsername(), loginBody.getPassword(), loginBody.getCode(),
                loginBody.getUuid());
        ajax.put(Constants.TOKEN, token);
        return ajax;
    }
}