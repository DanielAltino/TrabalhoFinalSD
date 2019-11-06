package com.example.chamadoincidentesservidor;

public class Setores {

    private String nome, email, descricao, setor;
    private int setorId;

    public Setores() {
    }

    public Setores(String nome, String email, String descricao, String setor, int setorId) {
        this.nome = nome;
        this.email = email;
        this.descricao = descricao;
        this.setor = setor;
        this.setorId = setorId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getSetor() {
        return setor;
    }

    public void setSetor(String setor) {
        this.setor = setor;
    }

    public int getSetorId() {
        return setorId;
    }

    public void setSetorId(int setorId) {
        this.setorId = setorId;
    }
}
