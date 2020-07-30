package com.gurgaczj.jmaker.model;

import lombok.*;

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
