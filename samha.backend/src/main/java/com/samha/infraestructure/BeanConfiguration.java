package com.samha.infraestructure;

import com.google.gson.Gson;
import com.samha.commons.IUseCaseManager;
import com.samha.commons.UseCaseFacade;
import com.samha.util.GenerateAlphaNumericString;
import com.samha.util.JWTSecret;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

@Configuration
public class BeanConfiguration implements WebMvcConfigurer {

    @Bean
    public UseCaseFacade useCaseFacade(IUseCaseManager manager) {
        return new UseCaseFacade(manager);
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    CommandLineRunner generateJWTSecret() {
        return args -> {
            String userHome = System.getProperty("user.home");
            String filePath = userHome + "/secret.json";
            File secretFile = new File(filePath);

            if (secretFile.exists()) {
                return;
            }

            String secret = GenerateAlphaNumericString.getRandomString(256);
            JWTSecret jwtSecret = new JWTSecret(secret);
            Gson gson = new Gson();

            try {
                BufferedWriter writer = new BufferedWriter(new FileWriter(secretFile));
                gson.toJson(jwtSecret, writer);
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        };
    }


}
