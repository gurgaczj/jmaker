package com.gurgaczj.jmaker.model;

import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.EqualsAndHashCode;

@Setter
@Getter
@RequiredArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode
public class MailSendingParams {

    @NonNull
    private String to;
    @NonNull
    private String subject;
    @NonNull
    private String text;
}
