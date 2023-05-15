package com.vodafone.sportyshoes.dtos;

import lombok.Data;

@Data
public class SignupDto {
    private String name;
    private String email;
    private String password;
    private String password2;
}
