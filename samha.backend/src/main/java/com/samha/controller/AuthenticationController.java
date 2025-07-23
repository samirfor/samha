package com.samha.controller;

import com.samha.application.token.ChangePassword;
import com.samha.application.token.IsTokenValid;
import com.samha.application.token.RefreshSession;
import com.samha.application.token.RefreshToken;
import com.samha.commons.UseCaseFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/auth")
public class AuthenticationController {

    private final UseCaseFacade facade;

    //TODO: Acrescentar no frontend a rota para utilizar o refresh_token

    @GetMapping("refreshToken")
    public void refreshToken(HttpServletResponse response, HttpServletRequest request) throws IOException {
        this.facade.execute(new RefreshToken(response, request));
    }

    @PostMapping("isTokenValid")
    public void isTokenValid(HttpServletResponse response, HttpServletRequest request) throws  IOException {
        this.facade.execute(new IsTokenValid(response, request));
    }

    @PostMapping("change-password")
    public void changePassword(@RequestBody Map<String, String> params) {
        facade.execute(new ChangePassword(params));
    }

    @PostMapping("refreshSession")
    public void refreshSession(HttpServletResponse response, HttpServletRequest request) {
        this.facade.execute(new RefreshSession(response, request));
    }
}
