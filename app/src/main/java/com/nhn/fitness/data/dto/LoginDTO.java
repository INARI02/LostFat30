package com.nhn.fitness.data.dto;

import com.google.gson.annotations.Expose;

public class LoginDTO {
    @Expose
    private String username;
    @Expose
    private String password;

    public LoginDTO(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
