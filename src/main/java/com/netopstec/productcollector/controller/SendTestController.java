package com.netopstec.productcollector.controller;

import com.netopstec.productcollector.task.MailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * SendTestController
 *
 * @author linyi
 * @date 2019/1/3 18:47
 */
@RestController
public class SendTestController {

    @Autowired
    private MailService mailService;


    @RequestMapping("/send")
    public String send() {
        mailService.sendMailTask();
        return "success";
    }
}
