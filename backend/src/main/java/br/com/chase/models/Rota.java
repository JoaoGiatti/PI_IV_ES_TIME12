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

    // getters e Setters omitidos pra deixar mais limpo
}