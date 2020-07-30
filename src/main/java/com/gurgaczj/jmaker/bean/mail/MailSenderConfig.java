package com.gurgaczj.jmaker.bean.mail;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailSender;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

import java.util.Properties;

@Configuration
public class MailSenderConfig {

    /**
     * Do not forget to cinfigure this also in properties file!
     * @return {@link JavaMailSender}
     */
    @Bean
    public JavaMailSender mailSender(){
        JavaMailSenderImpl mailSender = new JavaMailSenderImpl();

        mailSender.setHost("some.host"); // mail host
        mailSender.setPort(587); // mail port

        mailSender.setUsername("user"); // mail username
        mailSender.setPassword("pass"); // mail password

        Properties props = mailSender.getJavaMailProperties(); // mail properties
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        mailSender.setJavaMailProperties(props);

        return mailSender;
    }
}
