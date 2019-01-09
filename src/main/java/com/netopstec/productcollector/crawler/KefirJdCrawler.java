package com.netopstec.productcollector.crawler;

import cn.wanghaomiao.seimi.annotation.Crawler;
import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.struct.Response;
import com.alibaba.fastjson.JSONObject;
import io.netty.handler.codec.json.JsonObjectDecoder;
import lombok.extern.slf4j.Slf4j;

import java.math.BigDecimal;

/**
 * KefirJdCrawler
 *
 * @author linyi
 * @date 2019/1/7 16:12
 */
@Slf4j
@Crawler(name = "Kefir-Jd-Crawler", useUnrepeated = false, useCookie = true, delay = 2)
public class KefirJdCrawler extends BaseSeimiCrawler {

    @Override
    public String[] startUrls() {
        return new String[]{"https://c0.3.cn/stock?skuId=2915483&cat=1320,1585,9434&venderId=1000090824&area=15_1213_3411_52667"};
    }

    @Override
    public void start(Response response) {
        String content = response.getContent();
        String url = response.getUrl();
        log.info("京东线程[{}]开始解析url[{}]的response.", Thread.currentThread().getName(), url);
        try {
            /*
            if(content.endsWith("\\)")) {
                int indexOf = content.indexOf("(");
                if (indexOf == -1) return;
                content = content.substring(indexOf + 1, content.length());
            }
            */
            JSONObject contentJson = JSONObject.parseObject(content);
            JSONObject stockJson = contentJson.getJSONObject("stock");
            JSONObject self_dJson = stockJson.getJSONObject("self_D");
            String vender = self_dJson.getString("vender");
            String deliver = self_dJson.getString("deliver");
            String weightValue = stockJson.getString("weightValue");
            String stockDesc = stockJson.getString("stockDesc");
            JSONObject jdPrice = stockJson.getJSONObject("jdPrice");
            BigDecimal price = jdPrice.getBigDecimal("p");
            System.out.println(price);
        } catch (Exception e) {
            log.info("京东线程[{}]解析url[{}]的response.出错：{}", Thread.currentThread().getName(), url, e);
        }
        log.info("京东线程[{}]结束解析url[{}]的response.", Thread.currentThread().getName(), url);
    }


}
