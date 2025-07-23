package com.samha.util;

import lombok.Data;

@Data
public class JWTSecret {
    private String secret;
    public JWTSecret(String secret){
        this.secret = secret;
    }
}
