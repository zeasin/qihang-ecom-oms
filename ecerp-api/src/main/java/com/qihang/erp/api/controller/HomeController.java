package com.qihang.erp.api.controller;

import com.alibaba.nacos.api.config.annotation.NacosValue;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {
    @Autowired
    private MySpiderController spiderController;

    @GetMapping("/test/22")
    public String home(){
        spiderController.startSpider();
        return "hello world";
    }

    @Value(value = "${zhijian.name:1}")
    private String serverName;

    @GetMapping(value = "/test/na")
    public String get() {
        return serverName;
    }

}
