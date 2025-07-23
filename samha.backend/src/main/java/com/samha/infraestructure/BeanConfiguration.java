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
            String secret = GenerateAlphaNumericString.getRandomString(256);
            JWTSecret jwtSecret = new JWTSecret(secret);
            Gson gson = new Gson();

            // Obter o diretório do usuário
            String userHome = System.getProperty("user.home");

            // Definir o caminho absoluto para o arquivo
            String filePath = userHome + "/secret.json";

            try {
                // Criar um escritor para o arquivo
                BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));

                // Converter o objeto jwtSecret em JSON e gravá-lo no arquivo
                gson.toJson(jwtSecret, writer);

                // Fechar o escritor
                writer.close();
            } catch (IOException e) {
                // Tratar erros de IO, se necessário
                e.printStackTrace();
            }
        };
    }


}
