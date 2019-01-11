package com.netopstec.productcollector.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.config.SocketConfig;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpRequestRetryHandler;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.util.Map;

@Slf4j
public final class HttpUtil {

    public static final HttpClient CLIENT = HttpClients.custom().setDefaultSocketConfig(SocketConfig.custom().setSoTimeout(3000).build()).setRetryHandler(new DefaultHttpRequestRetryHandler()).build();


    /**
     * Do get string.
     *
     * @param url the url
     * @return the string
     */
    public static String doGet(String url) {
        return doGet(url, null);
    }

    /**
     * Do get string.
     *
     * @param url         the url
     * @param queryString the query string
     * @return string string
     */
    public static String doGet(String url, String queryString) {
        HttpGet get = new HttpGet(url + (queryString == null ? "" : queryString));
        get.setHeader("Content-Type", "text/html; charset=utf-8");
        try {
            HttpResponse response = CLIENT.execute(get);
            if (response != null
                    && response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity(), "GBK");
            }
        } catch (Exception e) {
            log.error("error in HttpUtil --> doGet: ", e);
        }
        return null;
    }

    /**
     * Do post string.
     *
     * @param url    the url
     * @param params the params
     * @return the string
     */
    public static String doPost(String url, Map<String, Object> params) {
        String paramsStr = JsonUtil.obj2String(params);
        return doPost(url, paramsStr);
    }

    public static String doPost(String url, String paramsStr) {
        HttpPost post = new HttpPost(url);
        try {
            StringEntity entity = new StringEntity(paramsStr, "utf-8");
            post.setEntity(entity);
            HttpResponse response = CLIENT.execute(post);
            if (response != null
                    && response.getStatusLine().getStatusCode() == 200) {
                return EntityUtils.toString(response.getEntity());
            }
        } catch (Exception e) {
            log.error("error ini HttpUtil --> doPost: ", e);
        }
        return null;
    }
}