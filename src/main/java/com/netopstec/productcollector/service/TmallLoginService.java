package com.netopstec.productcollector.service;

import com.netopstec.productcollector.util.RSAUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.HttpClientContext;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


@Slf4j
@Service
public class TmallLoginService {

    public String tmallLogin(String username, String pwd) {
        log.info(username + "登陆天猫中。。。");
        if (StringUtils.isEmpty(username) || StringUtils.isEmpty(pwd)) {
            return "";
        }
        CloseableHttpClient client = HttpClients.createDefault();
        HttpClientContext context = HttpClientContext.create();
        String loginFormUrl = "https://login.taobao.com/member/login.jhtml?style=miniall&newMini2=true&enup=true&from=tmall&allp=assets_css%3D3.0.10/login_pc.css&full_redirect=false&tpl_redirect_url=https%3A%2F%2Fmdetail.tmall.com%2Fcross%2Fx_cross_iframe.htm%3Ftype%3Dminilogin%26t%3D2013072520131122%26callback" + System.currentTimeMillis();
        HttpGet get = new HttpGet(loginFormUrl);
        String loginFormHtml = null;
        try {
            HttpResponse response = client.execute(get, context);
            loginFormHtml = EntityUtils.toString(response.getEntity());
            EntityUtils.consume(response.getEntity());
        } catch (Exception e) {
            log.error("获取天猫登录窗口失败，返回的html为{}: ", loginFormHtml == null ? "" : loginFormHtml, e);
        }
        if (StringUtils.isEmpty(loginFormHtml)) {
            return "";
        }

        Map<String, String> params = new HashMap<>();
        String realPwd = null;
        Document doc = Jsoup.parse(loginFormHtml);
        Element form = doc.getElementById("J_Form");
        if (form == null) {
            log.error("获取天猫登录窗口失败，返回的html中没有J_Form元素。");
            return "";
        }
        try {
            Element submit = form.getElementsByClass("submit").get(0);
            String pbk = submit.getElementById("J_PBK").val();
            try {
                // 利用java的脚本引擎执行js脚本获取加密后的密码
                // 这里可以利用nodejs做一个加密的rest服务，应该能提升效率
                realPwd = RSAUtils.getRealPwd(pwd, pbk);
            } catch (Exception e) {
                log.error("获取加密后的密码失败：", e);
            }
            if (StringUtils.isEmpty(realPwd)) {
                return "";
            }

            Elements inputs = submit.getElementsByTag("input");
            for (Element input : inputs) {
                String name = input.attr("name");
                if (StringUtils.isEmpty(name)) {
                    continue;
                }
                params.put(name, input.val());
            }
        } catch (Exception e) {
            log.error("获取窗口提交参数失败：", e);
            return "";
        }

        params.put("TPL_username",username);
        params.put("TPL_password_2", realPwd);
        params.put("loginASRSuc", "1");


        String loginUrl = "https://login.taobao.com" + form.attr("action");
        HttpPost post = new HttpPost(loginUrl);
        post.setHeader("Content-Type", "application/x-www-form-urlencoded");
        post.setHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.110 Safari/537.36");
        post.setHeader("Referer", loginFormUrl);
        post.setHeader("Origin", "https://login.taobao.com");
        post.setHeader("Upgrade-Insecure-Requests", "1");
        post.setHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8");

        List<NameValuePair> pairs = new ArrayList<>();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            pairs.add(new BasicNameValuePair(entry.getKey(), entry.getValue()));
        }

        String cookie = context.getCookieStore().getCookies().stream().map(c -> c.getName() + "=" + c.getValue()).collect(Collectors.joining(";"));
        post.setHeader("Cookie", cookie);

        try {
            post.setEntity(new UrlEncodedFormEntity(pairs, "GBK"));
            HttpResponse loginRes = client.execute(post, context);
            String loginHtml = EntityUtils.toString(loginRes.getEntity());
            Document loginDoc = Jsoup.parse(loginHtml);
            Element msg = loginDoc.getElementById("J_Message");
            if (msg != null) {
                // todo 有详细的错误信息需要返回给调用者
                log.error("用户[{}]登录天猫出错: {}", username, msg.text());
                return "";
            }
            context.getCookieStore().getCookies().forEach(System.out::println);
            EntityUtils.consume(loginRes.getEntity());
        } catch (Exception e) {
            log.error("用户[{}]登录天猫失败: ", username, e);
            return "";
        }

        String collect = context.getCookieStore().getCookies().stream().map(c -> c.getName() + "=" + c.getValue()).collect(Collectors.joining(";"));
        log.info("登录成功， cookie：" + collect);
        return collect;
    }
}
