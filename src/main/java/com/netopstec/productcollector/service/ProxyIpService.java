package com.netopstec.productcollector.service;


import com.netopstec.productcollector.domain.ProxyIp;
import com.netopstec.productcollector.repository.ProxyIpRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;


/**
 * ProxyIpService
 *
 * @author linyi
 * @date 2018/12/28 10:56
 */
@Slf4j
@Service
public class ProxyIpService {

    private List<ProxyIp> proxyIpList = new ArrayList<>();

    @Autowired
    private ProxyIpRepository proxyIpRepository;
    @Autowired
    private AsyncService asyncService;


    /**
     * 测试ip是否可用
     *
     * @param proxyIpList
     */
    public void testAndSave(List<ProxyIp> proxyIpList) {
        log.info("爬取代理中...");
        List<Future<ProxyIp>> futures = new ArrayList<>();
        for (ProxyIp proxyIp : proxyIpList) {
            Future<ProxyIp> future = asyncService.test(proxyIp);
            futures.add(future);
        }
        List<ProxyIp> newProxyIpList = new ArrayList<>();
        futures.forEach(f -> {
            while (true) {
                if (f != null && (f.isDone() || f.isCancelled())) {
                    try {
                        ProxyIp ip = f.get();
                        if (ip != null) {
                            newProxyIpList.add(ip);
                        }
                    } catch (InterruptedException | ExecutionException e) {
                        log.error("异步验证IP有效性失败: ", e);
                    }
                    break;
                }
            }
        });
        log.info("爬到代理IP :{}, 可用的IP :{}", proxyIpList.size(), newProxyIpList.size());
        if (newProxyIpList.size() > 0) {
            proxyIpRepository.deleteAll();
            proxyIpRepository.saveAll(newProxyIpList);
            this.proxyIpList = newProxyIpList;
        }
    }

    /**
     *
     * @return
     */
    public ProxyIp getOne() {
        if (proxyIpList == null || proxyIpList.size() == 0) {
            List<ProxyIp> all = proxyIpRepository.findAll();
            return all.get(new Random().nextInt(all.size()));
        }
        return this.proxyIpList.get(new Random().nextInt(proxyIpList.size()));
    }
}
