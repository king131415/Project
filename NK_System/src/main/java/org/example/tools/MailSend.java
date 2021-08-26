package org.example.tools;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;


import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

/**
 * 发送邮件的工具类
 */

@Component
public class MailSend {
    //以当前类命名来创建日志记录对象，准备后面记录日志
    private  static  final Logger loger= LoggerFactory.getLogger(MailSend.class);

    //注入java发邮件对象
    @Autowired
    private JavaMailSender mailSender;
//    private JavaMailSender mailSender;

    //注入发件人信息
    @Value("${spring.mail.username}")
    private String from;

    public void sendMail(String to,String subject,String content){
        try {
            //创建邮件主体
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message);
            //发送邮件
            helper.setFrom(from);
            helper.setTo(to);
            //设置邮件的主题
            helper.setSubject(subject);
            helper.setText(content,true);
            //发送邮件
            mailSender.send(helper.getMimeMessage());
        }catch (MessagingException e){
            loger.error("发送邮件失败:"+e.getMessage());
        }

    }
}
