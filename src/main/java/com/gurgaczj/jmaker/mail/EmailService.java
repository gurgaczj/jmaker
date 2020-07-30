package com.gurgaczj.jmaker.mail;

import com.gurgaczj.jmaker.model.MailSendingParams;
import org.springframework.mail.MailException;

public interface EmailService {

    void sendSimpleMessage(MailSendingParams mailSendingParams);

    void sendSimpleMessage(MailSendingParams... mailSendingParams);
}
