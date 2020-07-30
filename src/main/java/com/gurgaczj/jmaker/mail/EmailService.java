package com.gurgaczj.jmaker.mail;

import com.gurgaczj.jmaker.model.MailSendingParams;

public interface EmailService {

    void sendSimpleMessage(MailSendingParams mailSendingParams);

    void sendSimpleMessage(MailSendingParams... mailSendingParams);
}
