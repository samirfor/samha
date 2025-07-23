package com.samha.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.AUTHORIZATION;
import static org.springframework.http.HttpStatus.FORBIDDEN;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

public abstract class JWTUtil {

    public static Integer ACCESS_TOKEN_EXPIRATION = 300;
    public static Integer REFRESH_TOKEN_EXPIRATION = 600;

    public static String generateAccessToken(String username, List<String> claims, String issuer, int expiresAt){
        String secret = JWTUtil.getSecret();
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + expiresAt * 60 * 1000))
                .withIssuer(issuer)
                .withClaim("papeis", claims)
                .sign(algorithm);
    }

    public static String generateRefreshToken(String username, String issuer, int expiresAt){
        String secret = JWTUtil.getSecret();
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        return JWT.create()
                .withSubject(username)
                .withExpiresAt(new Date(System.currentTimeMillis() + expiresAt * 60 * 1000))
                .withIssuer(issuer)
                .sign(algorithm);
    }

    public static void getNewToken(User user, HttpServletRequest request, HttpServletResponse response){
        List<String> claims = user.getAuthorities().stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        String access_token = JWTUtil.generateAccessToken(user.getUsername(), claims, request.getRequestURL().toString(), ACCESS_TOKEN_EXPIRATION);
        String refresh_token = JWTUtil.generateRefreshToken(user.getUsername(), request.getRequestURL().toString(), REFRESH_TOKEN_EXPIRATION);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);
        response.setContentType(APPLICATION_JSON_VALUE);
        try {
            new ObjectMapper().writeValue(response.getOutputStream(), tokens);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void refreshSession(String username, Collection<? extends GrantedAuthority> authorities, HttpServletRequest request, HttpServletResponse response) {
        List<String> claims = authorities.stream().map(GrantedAuthority::getAuthority).collect(Collectors.toList());
        String access_token = JWTUtil.generateAccessToken(username, claims, request.getRequestURL().toString(), ACCESS_TOKEN_EXPIRATION);
        String refresh_token = JWTUtil.generateRefreshToken(username, request.getRequestURL().toString(), REFRESH_TOKEN_EXPIRATION);
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);
        response.setContentType(APPLICATION_JSON_VALUE);
        try {
            new ObjectMapper().writeValue(response.getOutputStream(), tokens);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static DecodedJWT verifyToken(String token) throws JWTVerificationException {
        String secret = JWTUtil.getSecret();
        Algorithm algorithm = Algorithm.HMAC256(secret.getBytes());
        JWTVerifier verifier = JWT.require(algorithm).build();
        return verifier.verify(token);
    }

    public static void verifyToken(String token, HttpServletResponse response) throws IOException {
        try {
            DecodedJWT decodedJWT = JWTUtil.verifyToken(token);
            String login = decodedJWT.getSubject();
            String[] papeis = decodedJWT.getClaim("papeis").asArray(String.class);
            Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
            Arrays.stream(papeis).forEach( papel -> authorities.add(new SimpleGrantedAuthority(papel)));
            UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(login, null, authorities);
            SecurityContextHolder.getContext().setAuthentication(authenticationToken);
        }catch (Exception ex){
            JWTUtil.writeErrorResponse(response, ex);
        }
    }

    public static void writeTokenResponse(String access_token, String refresh_token, HttpServletResponse response) throws IOException{
        Map<String, String> tokens = new HashMap<>();
        tokens.put("access_token", access_token);
        tokens.put("refresh_token", refresh_token);
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), tokens);
    }

    public static void writeErrorResponse(HttpServletResponse response, Exception ex) throws IOException{
        response.setHeader("error", ex.getMessage());
        response.setStatus(FORBIDDEN.value());
        Map<String, String> error = new HashMap<>();
        error.put("errorMessage", "Usuário não encontrado");
        error.put("cause", ex.getClass().toString());
        response.setContentType(APPLICATION_JSON_VALUE);
        new ObjectMapper().writeValue(response.getOutputStream(), error);
    }

    public static String getSecret(){
        String userHome = System.getProperty("user.home");
        String filePath = userHome + File.separator + "secret.json";

        try {
            Path path = Paths.get(filePath);
            Reader reader = new FileReader(path.toFile());
            var gson = new Gson();
            var secret = gson.fromJson(reader, JWTSecret.class);
            reader.close();
            return secret.getSecret();
        } catch (IOException e) {
            throw new RuntimeException("Não foi possível encontrar a chave mestra");
        }
    }

    public static List<String> getPapeisFromToken(String access_token){
        DecodedJWT decodedJWT = JWTUtil.verifyToken(access_token);
        return decodedJWT.getClaim("papeis").asList(String.class);
    }

    public static void isTokenValid(HttpServletRequest request, HttpServletResponse response) throws IOException {
        try {
            String authorizationHeader = request.getHeader(AUTHORIZATION);
            String access_token = authorizationHeader.substring("Bearer ".length());
            JWTUtil.verifyToken(access_token);
            response.setStatus(OK.value());
            new ObjectMapper().writeValue(response.getOutputStream(), true);
        } catch (JWTVerificationException e) {
            JWTUtil.writeErrorResponse(response, e);
        }
    }
}
