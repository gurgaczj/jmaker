package com.gurgaczj.jmaker.model;

import lombok.*;
import org.springframework.http.HttpStatus;

import java.time.Instant;

@Setter
@Getter
@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
@NoArgsConstructor(access = AccessLevel.PRIVATE)
@ToString
@EqualsAndHashCode
public class ErrorMessage {

    private long timestamp = Instant.now().toEpochMilli();
    @NonNull
    private int status;
    @NonNull
    private String error;
    @NonNull
    private String message;

    public static ErrorMessage create(HttpStatus status, String message){
        return new ErrorMessage(status.value(), status.getReasonPhrase(), message);
    }
}
