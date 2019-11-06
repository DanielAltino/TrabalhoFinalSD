package com.example.chamadoincidentesservidor;

public class Setores {

    private String nome;
    private int setorId;

    public Setores(String nome, int setorId) {
        this.nome = nome;
        this.setorId = setorId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getSetorId() {
        return setorId;
    }

    public void setSetorId(int setorId) {
        this.setorId = setorId;
    }
}
