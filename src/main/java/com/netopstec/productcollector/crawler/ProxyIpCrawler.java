package com.netopstec.productcollector.crawler;

import cn.wanghaomiao.seimi.annotation.Crawler;
import cn.wanghaomiao.seimi.def.BaseSeimiCrawler;
import cn.wanghaomiao.seimi.struct.Request;
import cn.wanghaomiao.seimi.struct.Response;
import com.netopstec.productcollector.domain.ProxyIp;
import com.netopstec.productcollector.service.ProxyIpService;
import lombok.extern.slf4j.Slf4j;
import org.jsoup.nodes.Element;
import org.seimicrawler.xpath.JXDocument;
import org.seimicrawler.xpath.JXNode;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.List;

/**
 * ProxyIpCrawler
 *
 * @author linyi
 * @date 2018/12/28 10:56
 */

@Slf4j
@Crawler(delay = 0, useCookie = true,useUnrepeated = false)
public class ProxyIpCrawler extends BaseSeimiCrawler {

    @Autowired
    private ProxyIpService proxyIpService;

    @Override
    public String[] startUrls() {
        return new String[]{"https://www.xicidaili.com/wt/"};
    }

    @Override
    public void start(Response response) {
        JXDocument doc = response.document();
        String url = response.getUrl();
        log.info("线程[{}]开始解析url[{}]的response.", Thread.currentThread().getName(), url);
        List<JXNode> trList = doc.selN("//table/tbody/tr[@class='odd']");
        List<ProxyIp> proxyIpList = new ArrayList<>();
        for (JXNode trJxNode : trList) {
            Element element = trJxNode.asElement();
            ProxyIp proxyIp = new ProxyIp();
            proxyIp.setIp(element.child(1).text().trim());
            proxyIp.setPort(Integer.valueOf(element.child(2).text().trim()));
            proxyIp.setAddress(element.child(3).text().trim());
            proxyIp.setType(element.child(5).text().trim());
            proxyIp.setLastValidation(element.child(9).text().trim());
            proxyIpList.add(proxyIp);
        }
        if (proxyIpList.size() > 0) {
            proxyIpService.testAndSave(proxyIpList);
        }
        log.info("线程[{}]结束解析url[{}]的response.", Thread.currentThread().getName(), url);
    }

    // @Scheduled(cron = "0 0/8 * * * ?")
    public void callByCron() {
        logger.info("我是一个根据cron表达式执行的调度器, 任务是：刷新IP");
        push(Request.build("https://www.xicidaili.com/wt/", ProxyIpCrawler::start));
    }

}
