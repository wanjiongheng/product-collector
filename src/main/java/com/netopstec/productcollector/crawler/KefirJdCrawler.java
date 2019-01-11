package com.netopstec.productcollector.crawler;

import cn.wanghaomiao.seimi.annotation.Crawler;
import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.struct.Request;
import cn.wanghaomiao.seimi.struct.Response;
import com.alibaba.fastjson.JSONObject;
import com.mysql.cj.x.protobuf.MysqlxDatatypes;
import com.netopstec.productcollector.domain.JdKefir;
import com.netopstec.productcollector.util.HttpUtil;
import com.sun.javafx.binding.StringFormatter;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.utils.URLEncodedUtils;
import org.apache.http.util.TextUtils;
import org.springframework.scheduling.annotation.Scheduled;

import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

/**
 * KefirJdCrawler
 *
 * @author linyi
 * @date 2019/1/7 16:12
 */
@Slf4j
@Crawler(useUnrepeated = false)
public class KefirJdCrawler extends BaseSeimiCrawler {

    @Override
    public String[] startUrls() {
        return new String[0];
    }

    @Override
    public void start(Response response) {
        String url = response.getUrl();
        log.info("京东线程[{}]开始解析url[{}]的response.", Thread.currentThread().getName(), url);
        log.info("京东线程[{}]结束解析url[{}]的response.", Thread.currentThread().getName(), url);
    }

    @Scheduled(fixedRate = 1000 * 60 * 60 * 24)
    public void task () {
        log.info("------------------------------京东任务start------------------------------------------");
        JdKefir jdKefir = null;
        try{
            String url = "https://c0.3.cn/stock?skuId=2915483&cat=1320,1585,9434&venderId=1000090824&area=15_1213_3411_52667";
            String content = HttpUtil.doGet(url);
            if (content == null || "".equals(content)) {
                log.error("---------------------------------------获取京东商品价格为空，执行return---------------------------------------------");
                return;
            }
            JSONObject contentJson = JSONObject.parseObject(content);
            JSONObject stockJson = contentJson.getJSONObject("stock");
            // 获取店铺自身信息
            JSONObject self_dJson = stockJson.getJSONObject("self_D");
            String vender = self_dJson.getString("vender");
            String deliver = self_dJson.getString("deliver");
            String weightValue = stockJson.getString("weightValue");
            String stockDesc = stockJson.getString("stockDesc");
            JSONObject jdPrice = stockJson.getJSONObject("jdPrice");
            BigDecimal price = jdPrice.getBigDecimal("p");
            // 封装对象
            jdKefir = new JdKefir();
            jdKefir.setVender(vender);
            jdKefir.setDeliver(deliver);
            jdKefir.setPrice(price);
            jdKefir.setStockDesc(stockDesc);
            jdKefir.setWeight(weightValue);
        }catch (Exception e) {
            log.error("---------------------------------------------使用httpUtils获取京东商品价格出错------------------------------", e);
        }

        if (jdKefir != null) {
            log.info("--------------------------------------------push到KefirJdCrawler去获取其他信息----------------------------------");
            Request request = Request.build("", "start");
            Map<String, Object> map = new HashMap<>();
            map.put("jdKefir", jdKefir);
            request.setMeta(map);
            request.setCrawlerName(this.getClass().getSimpleName());
            push(request);
        }
    }


}
