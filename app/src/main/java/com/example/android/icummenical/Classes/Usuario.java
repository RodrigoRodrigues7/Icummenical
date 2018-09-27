package com.example.android.icummenical.Classes;

import com.google.firebase.database.Exclude;

public class Usuario {

    private String nome;
    private String email;
    private int fotoPerfil;
    private String senha;
    private String tipoUsuario;

    public Usuario() {
    }

    public Usuario(String nome, String email, int fotoPerfil, String senha) {
        this.nome = nome;
        this.email = email;
        this.fotoPerfil = fotoPerfil;
        this.senha = senha;
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

    public int getFotoPerfil() {
        return fotoPerfil;
    }

    public void setFotoPerfil(int fotoPerfil) {
        this.fotoPerfil = fotoPerfil;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    @Exclude
    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getTipoUsuario() {
        return tipoUsuario;
    }

    public void setTipoUsuario(String tipoUsuario) {
        this.tipoUsuario = tipoUsuario;
    }

}
