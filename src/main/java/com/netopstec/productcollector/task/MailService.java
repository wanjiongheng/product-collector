package com.netopstec.productcollector.task;

import com.netopstec.productcollector.domain.TmallKefir;
import com.netopstec.productcollector.repository.TmallKefirRepository;
import com.netopstec.productcollector.util.MailUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;

/**
 * MailService
 *
 * @author linyi
 * @date 2019/1/3 16:44
 */
@Slf4j
@Service
public class MailService {

    @Autowired
    private TmallKefirRepository tmallKefirRepository;


    @Scheduled(cron = "0 0 9,21 * * ?")
    public void sendMailTask () {

        List<TmallKefir> all = tmallKefirRepository.findAll();
        Optional<TmallKefir> max = all.stream().max(new Comparator<TmallKefir>() {
            @Override
            public int compare(TmallKefir o1, TmallKefir o2) {
                Long id1 = o1.getId();
                Long id2 = o2.getId();
                if (id1 > id2) return 1;
                if (id1 < id2) return -1;
                return 0;
            }
        });
        TmallKefir tmallKefir = max.get();
        String content = "<h1>天猫超市<h1><div>价格：" + tmallKefir.getPrice() + "</div><div>" + "活动：" + tmallKefir.getType() + "</div>";
        MailUtil.sendMail(content, "842723495@qq.com");
        MailUtil.sendMail(content, "1458005950@qq.com");
    }
}
