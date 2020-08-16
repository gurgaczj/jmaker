package com.gurgaczj.jmaker.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class AccountLessInfoDto {

    private String username;
    private Integer type;
    private Long lastDay;
    private Long creationDate;

}
