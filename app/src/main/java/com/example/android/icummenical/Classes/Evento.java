package com.example.android.icummenical.Classes;

public class Evento {

    private String keyEvento;
    private String background;
    private String titulo;
    private String data;
    private String horario;
    private String local;
    private String descricao;
    private String atividades;

    public Evento() {
    }

    public Evento(String keyEvento, String background, String titulo, String data, String horario, String local, String descricao, String atividades) {
        this.keyEvento = keyEvento;
        this.background = background;
        this.titulo = titulo;
        this.data = data;
        this.horario = horario;
        this.local = local;
        this.descricao = descricao;
        this.atividades = atividades;
    }

    public String getKeyEvento() {
        return keyEvento;
    }

    public void setKeyEvento(String keyEvento) {
        this.keyEvento = keyEvento;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getHorario() {
        return horario;
    }

    public void setHorario(String horario) {
        this.horario = horario;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getAtividades() {
        return atividades;
    }

    public void setAtividades(String atividades) {
        this.atividades = atividades;
    }

}
