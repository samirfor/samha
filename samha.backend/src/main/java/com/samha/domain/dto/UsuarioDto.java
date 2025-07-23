package com.samha.domain.dto;

import lombok.Data;

import java.io.Serializable;

@Data
public class UsuarioDto implements Serializable {
    private Long id;
    private String senha;
    private String login;
    private Long papel_id;
    private Long servidor_id;
}
