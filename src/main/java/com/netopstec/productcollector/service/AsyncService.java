package com.netopstec.productcollector.service;

import com.netopstec.productcollector.domain.ProxyIp;
import com.netopstec.productcollector.util.HttpUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.concurrent.Future;

/**
 * AsyncService
 *
 * @author linyi
 * @date 2019/1/9 19:15
 */
@Service
@Slf4j
public class AsyncService {
    /**
     * 测试ip是否可用的异步方法
     *
     * @param proxyIp
     * @return
     */
    @Async
    public Future<ProxyIp> test(ProxyIp proxyIp) {
        log.debug("开始测试" + proxyIp);
        HttpClient client = HttpUtil.CLIENT;
        HttpGet httpGet = new HttpGet("https://www.tmall.com/");
        // 设置代理ip,端口，协议
        HttpHost httpHost = new HttpHost(proxyIp.getIp(), proxyIp.getPort(), proxyIp.getType().toLowerCase());
        RequestConfig requestConfig = RequestConfig.custom()
                .setConnectTimeout(10000)
                .setSocketTimeout(10000)
                .setProxy(httpHost).build();
        httpGet.setConfig(requestConfig);
        try {
            long start = System.currentTimeMillis();
            HttpResponse response = client.execute(httpGet);
            if (response != null
                    && response.getStatusLine().getStatusCode() == 200) {
                String content = EntityUtils.toString(response.getEntity());
                long end = System.currentTimeMillis();
                proxyIp.setReqMillisecond(end - start);
                return new AsyncResult<>(proxyIp);
            }
        } catch (IOException e) {
            log.debug("代理Ip不可用,抛弃" + proxyIp, e);
        }
        log.debug("结束测试" + proxyIp);
        return null;
    }
}
