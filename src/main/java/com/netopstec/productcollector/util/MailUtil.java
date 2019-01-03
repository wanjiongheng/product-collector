package com.netopstec.productcollector.util;

import lombok.extern.slf4j.Slf4j;

import javax.annotation.Resource;
import javax.mail.Message;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * MailUtil
 *
 * @author linyi
 * @date 2019/1/3 16:18
 */
@Slf4j
public class MailUtil {

    private static Properties props = new Properties();

    static {
        InputStream inputStream = MailUtil.class.getClassLoader().getResourceAsStream("mail.properties");
        try {
            props.load(inputStream);
            inputStream.close();
        } catch (IOException e) {
            log.error("mailUtil读取properties文件错误！");
            e.printStackTrace();
        }
    }

    public static void sendMail(String content, String address) {
        try {
            // 使用环境属性和授权信息，创建邮件会话
            Session session = Session.getInstance(props);

            // 通过session得到transport对象
            Transport transport = session.getTransport();

            // 连接邮件服务器：邮箱类型，帐号，授权码
            transport.connect("smtp.qq.com",props.getProperty("mail.user"), props.getProperty("mail.password"));

            // 创建邮件信息
            MimeMessage message = new MimeMessage(session);

            // 设置发件人
            InternetAddress from = new InternetAddress(props.getProperty("mail.user"));
            message.setFrom(from);

            // 设置收件人的邮箱
            InternetAddress to = new InternetAddress(address);
            message.setRecipient(Message.RecipientType.TO, to);

            // 设置邮件标题
            message.setSubject("测试邮件");

            // 设置邮件的内容体
            message.setContent(content, "text/html;charset=UTF-8");

            // 发送邮件
            transport.sendMessage(message, message.getAllRecipients());
            transport.close();

            log.info("邮件发送成功");
        } catch (Exception e) {
            log.error("发送mail邮件失败！");
            e.printStackTrace();
        }
    }



}
