package com.maligno.client;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class PedidoDeRota implements Serializable {

    private String uid;
    private String name;
    private String description;
    private String startLocation;
    private String endLocation;
    private Double distance;  // em METROS
    private String recordTime; // formato esperado: HH:mm:ss

    public PedidoDeRota(
            String uid,
            String name,
            String description,
            String startLocation,
            String endLocation,
            Double distance,
            String recordTime
    ) {
        this.uid = uid;
        this.name = name;
        this.description = description;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.distance = distance;
        this.recordTime = recordTime;
    }

    public boolean validar() {
        return validarComResultado().isValido();
    }

    /**
     * Retorna um objeto com flag de válido e lista de erros.
     */
    public ResultadoValidacao validarComResultado() {
        List<String> erros = new ArrayList<>();

        // Campos obrigatórios
        if (isEmpty(this.uid)) {
            erros.add("UID é obrigatório.");
        }
        if (isEmpty(this.name)) {
            erros.add("Nome é obrigatório.");
        }
        if (isEmpty(this.description)) {
            erros.add("Descrição é obrigatória.");
        }
        if (isEmpty(this.startLocation)) {
            erros.add("Local de início (startLocation) é obrigatório.");
        }
        if (isEmpty(this.endLocation)) {
            erros.add("Local de término (endLocation) é obrigatório.");
        }

        // Distância
        if (this.distance == null) {
            erros.add("Distância é obrigatória.");
        } else if (this.distance <= 0) {
            erros.add("Distância deve ser maior que zero.");
        }

        // Tempo
        if (isEmpty(this.recordTime)) {
            erros.add("Tempo de recorde (recordTime) é obrigatório.");
        } else {
            // Só tenta validar tempo/velocidade se tiver algo preenchido
            Long totalSegundos = parseRecordTimeEmSegundos(this.recordTime, erros);

            // Se o formato for inválido, parseRecordTimeEmSegundos já adiciona erro e retorna null
            if (totalSegundos != null) {
                if (totalSegundos <= 0) {
                    erros.add("Tempo total (recordTime) deve ser maior que zero.");
                }

                // Velocidade física (metros / segundo)
                if (this.distance != null && this.distance > 0 && totalSegundos > 0) {
                    double velocidadeMS = this.distance / totalSegundos;
                    double velocidadeKMH = velocidadeMS * 3.6;

                    // Bolt ≈ 12.4 m/s → limite 12.5 m/s
                    if (velocidadeMS > 12.5) {
                        erros.add(String.format(
                                "Velocidade média inválida: %.2f m/s (%.2f km/h). Limite máximo permitido é 12.5 m/s.",
                                velocidadeMS, velocidadeKMH
                        ));
                    }
                }
            }
        }

        boolean valido = erros.isEmpty();
        return new ResultadoValidacao(valido, erros);
    }

    /**
     * Converte recordTime (HH:mm:ss) em segundos.
     * Se der problema, adiciona mensagens de erro na lista e retorna null.
     */
    private Long parseRecordTimeEmSegundos(String recordTime, List<String> erros) {
        String[] partes = recordTime.split(":");
        if (partes.length != 3) {
            erros.add("recordTime deve estar no formato HH:mm:ss.");
            return null;
        }

        int horas;
        int minutos;
        int segundos;

        try {
            horas = Integer.parseInt(partes[0]);
            minutos = Integer.parseInt(partes[1]);
            segundos = Integer.parseInt(partes[2]);
        } catch (NumberFormatException e) {
            erros.add("recordTime deve conter apenas números no formato HH:mm:ss.");
            return null;
        }

        if (horas < 0 || minutos < 0 || segundos < 0) {
            erros.add("Horas, minutos e segundos não podem ser negativos.");
            return null;
        }

        if (minutos >= 60 || segundos >= 60) {
            erros.add("Minutos e segundos devem estar entre 0 e 59.");
            return null;
        }

        return (long) horas * 3600 + (long) minutos * 60 + segundos;
    }

    private static boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }

    // Getters (se precisar usar nos testes ou em outros lugares)
    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public Double getDistance() {
        return distance;
    }

    public String getRecordTime() {
        return recordTime;
    }

    /**
     * DTO simples para retornar o resultado da validação.
     */
    public static final class ResultadoValidacao {
        private final boolean valido;
        private final List<String> erros;

        public ResultadoValidacao(boolean valido, List<String> erros) {
            this.valido = valido;
            // evita modification externa da lista
            this.erros = Collections.unmodifiableList(new ArrayList<>(erros));
        }

        public boolean isValido() {
            return valido;
        }

        public List<String> getErros() {
            return erros;
        }

        @Override
        public String toString() {
            return "ResultadoValidacao{" +
                    "valido=" + valido +
                    ", erros=" + erros +
                    '}';
        }
    }
}
