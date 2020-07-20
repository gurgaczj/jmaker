package com.gurgaczj.jmaker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.lang.Nullable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("accounts")
public class Account {

    @Id
    @Column(value = "id")
    private Long id;

    @NonNull
    @Column(value = "name")
    private String username;

    @NonNull
    @Column(value = "password")
    private String password;

    @Nullable
    @Column(value = "secret")
    private String secret;

    @NonNull
    @Column(value = "type")
    private Integer type;

    @NonNull
    @Column(value = "premdays")
    private Long premiumDays;

    @NonNull
    @Column(value = "lastday")
    private Long lastDay;

    @NonNull
    @Column(value = "email")
    private String email;

    @NonNull
    @Column(value = "creation")
    private Long creationDate;
}
