package com.samha.application.token;

import com.samha.commons.UseCase;
import com.samha.util.JWTUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class IsTokenValid extends UseCase<Void> {

    private HttpServletResponse response;
    private HttpServletRequest request;

    public IsTokenValid(HttpServletResponse response, HttpServletRequest request) {
        this.request = request;
        this.response = response;
    }

    @Override
    protected Void execute() throws Exception {
        JWTUtil.isTokenValid(request, response);
        return null;
    }
}
