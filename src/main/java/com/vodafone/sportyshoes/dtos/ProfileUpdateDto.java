package com.vodafone.sportyshoes.dtos;

import lombok.Data;

@Data
public class ProfileUpdateDto {
    private String name;
    private String email;
    private String password;
    private String newPassword;
    private String newPassword2;
}
