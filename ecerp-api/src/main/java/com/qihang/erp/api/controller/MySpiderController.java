package com.qihang.erp.api.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import us.codecraft.webmagic.Spider;

@Component
public class MySpiderController {
    @Autowired
    private Environment environment;

    public void startSpider() {
        String startUrl = "https://pifa.pinduoduo.com/";
        int threadCount = Integer.parseInt(environment.getProperty("spider.thread.count"));

        Spider.create(new MyPageProcessor())
                .addUrl(startUrl)
                .thread(threadCount)
                .run();

    }
}
