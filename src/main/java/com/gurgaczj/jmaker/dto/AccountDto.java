package com.gurgaczj.jmaker.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AccountDto {

    private String username;
    private Integer type;
    private Long premiumDays;
    private Long lastDay;
    private String email;
    private Long creationDate;

}
