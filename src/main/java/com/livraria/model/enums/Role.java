package com.livraria.model.enums;

/**
 * Define os perfis de acesso disponíveis no sistema da livraria.
 */
public enum Role {
    ADMIN("Administrador"),
    CONSUMIDOR("Consumidor");

    private final String descricao;

    Role(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }

    @Override
    public String toString() {
        return descricao;
    }
}
