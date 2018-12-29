package com.netopstec.productcollector.crawler;

import cn.wanghaomiao.seimi.annotation.Crawler;
import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.struct.Request;
import cn.wanghaomiao.seimi.struct.Response;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;

import java.util.*;

/**
 * KefirTmallChaoShiCrawler
 *
 * @author linyi
 * @date 2018/12/29 16:26
 */
@Slf4j
@Crawler(name = "Kefir-Tmall-ChaoShi-Crawler", delay = 2, useUnrepeated = true, useCookie = true)
public class KefirTmallChaoShiCrawler extends BaseSeimiCrawler {

    @Override
    public String[] startUrls() {
        return new String[0];
    }

    @Override
    public List<Request> startRequests() {
        List<Request> requests = new LinkedList<>();
        Request request = Request.build("https://mdskip.taobao.com/core/initItemDetail.htm?itemId=543854752443", KefirTmallChaoShiCrawler::start);
        Map<String, String> map = new HashMap<>();
        map.put("Referer", "https://chaoshi.detail.tmall.com/item.htm");
        request.setHeader(map);
        requests.add(request);
        return requests;
    }

    @Override
    public void start(Response response) {
        String url = response.getUrl();
        String content = response.getContent();
        log.info("线程[{}]开始解析url[{}]的response.", Thread.currentThread().getName(), url);
        if (content == null || "".equals(content)) {
            log.info("线程[{}]停止解析url[{}]的response. 原因content内容为空", Thread.currentThread().getName(), url);
            return;
        }
        JSONObject jsonObject = JSONObject.parseObject(content);
        String isSuccess = jsonObject.getString("isSuccess");
        if (!"true".equals(isSuccess)) {
            log.info("线程[{}]开始解析url[{}]的response. 返回的json内容isSuccess != true", Thread.currentThread().getName(), url);
            return;
        }
        JSONObject defaultModel = jsonObject.getJSONObject("defaultModel");
        JSONObject itemPriceResultDO = defaultModel.getJSONObject("itemPriceResultDO");
        if (itemPriceResultDO == null) {
            log.info("线程[{}]停止解析url[{}]的response. 原因天猫返回的itemPriceResultDO为空", Thread.currentThread().getName(), url);
            return;
        }
        JSONObject priceInfo = itemPriceResultDO.getJSONObject("priceInfo");
        if (priceInfo != null) {
            Set<String> skuSet = priceInfo.keySet();
            for (String sku : skuSet) {
                JSONObject skuPriceInfo = priceInfo.getJSONObject(sku);
                JSONArray promotionList = skuPriceInfo.getJSONArray("promotionList");
                if (promotionList == null) {
                    promotionList = skuPriceInfo.getJSONArray("suggestivePromotionList");
                }
                if (promotionList != null) {
                    JSONObject promotion = promotionList.getJSONObject(0);
                    String price = promotion.getString("price");
                    String type = promotion.getString("type");
                    Long startTime = promotion.getLong("startTime");
                    Long endTime = promotion.getLong("endTime");
                    System.out.println(price + type);
                }
            }
        }
        log.info("线程[{}]结束解析url[{}]的response.", Thread.currentThread().getName(), url);
    }

    /**
     * 定时爬取tmall超市的
     */
    @Scheduled(cron = "0 1 0 * * ? ")
    public void task() {
        log.info("任务开始执行，爬取天猫超市开啡尔的商品信息");
        Request request = Request.build("https://mdskip.taobao.com/core/initItemDetail.htm?itemId=543854752443", "start");
        Map<String, String> header = new HashMap<>();
        header.put("Referer", "https://chaoshi.detail.tmall.com/item.htm");
        request.setHeader(header);
        request.setCrawlerName("Kefir-Tmall-ChaoShi-Crawler");
        push(request);
    }

}