package br.com.chase.models;

public class Ranking {

    private String usuarioId;
    private String nomeUsuario;
    private String fotoUsuario;
    private String tempo;
    private double velocidadeMedia;

    public Ranking() {}

    public Ranking(String usuarioId, String nomeUsuario, String fotoUsuario, String tempo, double velocidadeMedia) {
        this.usuarioId = usuarioId;
        this.nomeUsuario = nomeUsuario;
        this.fotoUsuario = fotoUsuario;
        this.tempo = tempo;
        this.velocidadeMedia = velocidadeMedia;
    }

    public String getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(String usuarioId) {
        this.usuarioId = usuarioId;
    }

    public String getNomeUsuario() {
        return nomeUsuario;
    }

    public void setNomeUsuario(String nomeUsuario) {
        this.nomeUsuario = nomeUsuario;
    }

    public String getFotoUsuario() {
        return fotoUsuario;
    }

    public void setFotoUsuario(String fotoUsuario) {
        this.fotoUsuario = fotoUsuario;
    }

    public String getTempo() {
        return tempo;
    }

    public void setTempo(String tempo) {
        this.tempo = tempo;
    }

    public double getVelocidadeMedia() {
        return velocidadeMedia;
    }

    public void setVelocidadeMedia(double velocidadeMedia) {
        this.velocidadeMedia = velocidadeMedia;
    }
}
