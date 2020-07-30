package com.gurgaczj.jmaker.exception;

public class MailSenderException extends RuntimeException {

    public MailSenderException() {
        super();
    }

    public MailSenderException(String message) {
        super(message);
    }
}
