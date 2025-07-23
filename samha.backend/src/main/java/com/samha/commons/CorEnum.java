package com.samha.commons;

public enum CorEnum {
    VERMELHO("#C7312C"),
    LARANJA("#FF9966"),
    AZUL("#00A4CA"),
    ;

    private String id;

    CorEnum(String id) {
        this.id = id;
    }

    public String getId() {
        return id;
    }

    @Override
    public String toString() {
        return this.id;
    }
}
