package com.example.bernardojr.whereverigo.dominio;

import java.util.Date;

public class Pessoa{

    private int id;
    private Usuario usuario;
    private String nome;
    private Date dataNascimento;
    private String sexo;
    private String strDataNascimento;

    public Pessoa() {
    }

    public Pessoa(int id, Usuario usuario, String nome, String sexo, String strDataNascimento) {
        this.id = id;
        this.usuario = usuario;
        this.nome = nome;
        this.sexo = sexo;
        this.strDataNascimento = strDataNascimento;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public Date getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(Date dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getStrDataNascimento() {
        return strDataNascimento;
    }

    public void setStrDataNascimento(String strDataNascimento) {
        this.strDataNascimento = strDataNascimento;
    }
}
