package com.gurgaczj.jmaker.service;

import com.gurgaczj.jmaker.dto.AccountDto;
import com.gurgaczj.jmaker.mapper.DtoMapper;
import com.gurgaczj.jmaker.model.Account;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class DtoMapperTests {

    @Test
    public void toDtoTest(){
        Account account = new Account();
        account.setId(null);
        account.setUsername("TEST_1");
        account.setCreationDate(Instant.now().getEpochSecond());
        account.setSecret("efewfesfs56u56e");
        account.setEmail("sa56ikjfd@wpp.pl");
        account.setLastDay(3215235L);
        account.setPassword("pass67ih");
        account.setPremiumDays(325235L);
        account.setEnabled(true);
        account.setType(1);

        AccountDto accountDto = DtoMapper.toDto(account, AccountDto.class);

        assertEquals(account.getUsername(), accountDto.getUsername());
        assertEquals(account.getEmail(), accountDto.getEmail());
        assertEquals(account.getCreationDate(), accountDto.getCreationDate());
        assertEquals(account.getLastDay(), accountDto.getLastDay());
        assertEquals(account.getPremiumDays(), accountDto.getPremiumDays());
        assertEquals(account.getType(), accountDto.getType());
    }
}
