package com.samha.bootstrap;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "samha.bootstrap.admin")
public class AdminBootstrapProperties {

    private boolean enabled = false;
    private String login = "admin";
    private String password = "123";
    private String papelNome = "COORDENADOR_ACADEMICO";

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPapelNome() {
        return papelNome;
    }

    public void setPapelNome(String papelNome) {
        this.papelNome = papelNome;
    }
}
