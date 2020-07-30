package com.gurgaczj.jmaker.mail.impl;

import com.gurgaczj.jmaker.mail.EmailService;
import com.gurgaczj.jmaker.model.MailSendingParams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Date;

@Service
public class EmailServiceImpl implements EmailService {

    private final static Logger logger = LoggerFactory.getLogger(EmailServiceImpl.class);

    private final JavaMailSender mailSender;

    public EmailServiceImpl(JavaMailSender mailSender) {
        this.mailSender = mailSender;
    }

    @Override
    public void sendSimpleMessage(MailSendingParams mailSendingParams) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();

        simpleMailMessage.setTo(mailSendingParams.getTo());
        simpleMailMessage.setSubject(mailSendingParams.getSubject());
        simpleMailMessage.setSentDate(new Date());
        simpleMailMessage.setText(mailSendingParams.getText());

        mailSender.send(simpleMailMessage);
    }

    @Override
    public void sendSimpleMessage(MailSendingParams... mailSendingParams) {
        Flux.fromArray(mailSendingParams)
                .subscribe(param -> sendSimpleMessage(param));
    }
}
