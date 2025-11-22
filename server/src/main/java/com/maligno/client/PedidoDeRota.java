package com.maligno.client;

import java.io.Serializable;

public class PedidoDeRota implements Serializable {
    private String uid;
    private String name;
    private String description;
    private String startLocation;
    private String endLocation;
    private Double distance;
    private String recordTime;

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
        if (isEmpty(this.uid)) return false;
        if (isEmpty(this.name)) return false;
        if (isEmpty(this.description)) return false;
        if (isEmpty(this.startLocation)) return false;
        if (isEmpty(this.endLocation)) return false;

        if (this.distance == null || this.distance <= 0) return false;

        if (!validarVelocidadeFisica()) return false;

        return !isEmpty(this.recordTime);
    }

    private boolean validarVelocidadeFisica() {
        try {
            String[] partes = recordTime.split(":");
            int horas = Integer.parseInt(partes[0]);
            int minutos = Integer.parseInt(partes[1]);
            int segundos = Integer.parseInt(partes[2]);

            int totalSegundos = horas * 3600 + minutos * 60 + segundos;
            if (totalSegundos <= 0) return false;

            // distância em METROS
            double metros = this.distance;

            // velocidade média em m/s
            double velocidadeMS = metros / totalSegundos;

            // Bolt = 12.4 m/s → limite de 12.5 m/s
            if (velocidadeMS > 12.5) {
                System.out.printf("❌ Velocidade impossível: %.2f m/s (%.2f km/h)%n", velocidadeMS, velocidadeMS * 3.6);
                return false;
            }

            return true;

        } catch (Exception e) {
            return false;
        }
    }

    private static boolean isEmpty(String s) {
        return s == null || s.trim().isEmpty();
    }
}
