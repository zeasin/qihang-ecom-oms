package com.qihang.erp.api.controller;


import lombok.extern.java.Log;
import us.codecraft.webmagic.Page;
import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.processor.PageProcessor;
import us.codecraft.webmagic.selector.Selectable;

@Log
public class MyPageProcessor implements PageProcessor {
    private Site site = Site.me().setRetryTimes(3).setSleepTime(1000);
    @Override
    public void process(Page page) {
        String url = page.getUrl().toString();
        log.info("开始抓取网页内容"+url);
        // 处理抓取到的网页内容的逻辑

//        Selectable xpath = page.getHtml().xpath("//td/text()");
        Selectable xpath = page.getHtml().xpath("//div[@class='hotword-container']/a/text()");
        System.out.println(xpath);
        log.info("完成");
    }

    @Override
    public Site getSite() {
//        return PageProcessor.super.getSite();
        return site;
    }
}
