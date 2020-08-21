package com.gurgaczj.jmaker.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class NewPassword {

    @NonNull private String oldPassword;
    @NonNull private String newPassword;
    @NonNull private String verifyNewPassword;
}
