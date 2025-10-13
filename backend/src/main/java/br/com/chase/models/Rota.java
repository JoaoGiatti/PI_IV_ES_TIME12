package br.com.chase.models;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.List;

@Document(collection = "rotas")
public class Rota {
    @Id
    private String id;
    private String criadorId;
    private String nome;
    private List<Ponto> pontos;
    private double distancia;
    private String tempoRecorde;
    private double velocidadeMediaRecorde;
    private double caloriasEstimadas;
    private String visibilidade;
    private Date dataCriacao;

    // 🔹 Getters e Setters

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCriadorId() {
        return criadorId;
    }

    public void setCriadorId(String criadorId) {
        this.criadorId = criadorId;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public List<Ponto> getPontos() {
        return pontos;
    }

    public void setPontos(List<Ponto> pontos) {
        this.pontos = pontos;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public String getTempoRecorde() {
        return tempoRecorde;
    }

    public void setTempoRecorde(String tempoRecorde) {
        this.tempoRecorde = tempoRecorde;
    }

    public double getVelocidadeMediaRecorde() {
        return velocidadeMediaRecorde;
    }

    public void setVelocidadeMediaRecorde(double velocidadeMediaRecorde) {
        this.velocidadeMediaRecorde = velocidadeMediaRecorde;
    }

    public double getCaloriasEstimadas() {
        return caloriasEstimadas;
    }

    public void setCaloriasEstimadas(double caloriasEstimadas) {
        this.caloriasEstimadas = caloriasEstimadas;
    }

    public String getVisibilidade() {
        return visibilidade;
    }

    public void setVisibilidade(String visibilidade) {
        this.visibilidade = visibilidade;
    }

    public Date getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(Date dataCriacao) {
        this.dataCriacao = dataCriacao;
    }
}