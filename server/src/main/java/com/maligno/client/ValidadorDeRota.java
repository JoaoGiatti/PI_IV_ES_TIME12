package com.maligno.client;

import java.util.List;

public class ValidadorDeRota {

    private static final double MAX_HUMAN_RUNNING_SPEED_MPS = 8.0;

    /**
     * Valida um RouteRequest inteiro.
     * Retorna true se TODOS os campos estiverem válidos.
     */
    public static boolean isValid(Rota route) {
        if (route == null) return false;

        // Valida campos obrigatórios de texto
        if (isInvalid(route.getUid())) return false;
        if (isInvalid(route.getName())) return false;
        if (isInvalid(route.getDescription())) return false;
        if (isInvalid(route.getStartLocation())) return false;
        if (isInvalid(route.getEndLocation())) return false;

        // Valida números
        if (!isValidDistance(route.getDistance())) return false;
        if (!isValidRecordTime(route.getRecordTime())) return false;

        // Valida performance com velocidades e tempos impossiveis
        if (!isRealisticPerformance(route.getDistance(), route.getRecordTime())) return false;

        // Valida lista de pontos
        return isValidPoints(route.getPoints());
    }

    // --------- Métodos auxiliares ------------
    private static boolean isInvalid(String value) {
        return value == null || value.trim().isEmpty();
    }

    private static boolean isValidDistance(double distance) {
        return distance > 0;
    }

    /**
     * Valida tempo no formato "HH:mm:ss".
     * Exemplo válido: "00:05:30", "01:12:09", "99:59:59"
     */
    private static boolean isValidRecordTime(String recordTime) {
        if (recordTime == null) {
            return false;
        }

        if (!recordTime.matches("^\\d{2}:\\d{2}:\\d{2}$")) {
            return false;
        }

        String[] parts = recordTime.split(":");
        int hours = Integer.parseInt(parts[0]);
        int minutes = Integer.parseInt(parts[1]);
        int seconds = Integer.parseInt(parts[2]);

        if (hours < 0 || hours > 99) return false;
        if (minutes < 0 || minutes > 59) return false;
        return seconds >= 0 && seconds <= 59;
    }

    /**
     * Verifica se a combinação distância + tempo é "humana".
     */
    private static boolean isRealisticPerformance(double distanceMeters, String recordTime) {
        int totalSeconds = parseRecordTimeToSeconds(recordTime);

        // tempo zero ou negativo com distância > 0 é impossível
        if (totalSeconds <= 0 && distanceMeters > 0) {
            return false;
        }

        // se distância for zero, ok (por exemplo, rota só criada, sem corrida ainda)
        if (distanceMeters <= 0) {
            return true;
        }

        double speedMps = distanceMeters / (double) totalSeconds;

        // se a velocidade média for maior que o limite, é impossível
        return speedMps <= MAX_HUMAN_RUNNING_SPEED_MPS;
    }

    /**
     * Converte "HH:mm:ss" para segundos.
     * Pressupõe que o formato já foi validado.
     */
    private static int parseRecordTimeToSeconds(String recordTime) {
        if (recordTime == null || !recordTime.matches("^\\d{2}:\\d{2}:\\d{2}$")) {
            return -1;
        }

        try {
            String[] parts = recordTime.split(":");
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            int seconds = Integer.parseInt(parts[2]);

            return hours * 3600 + minutes * 60 + seconds;
        } catch (NumberFormatException e) {
            return -1;
        }
    }

    /**
     * Valida a lista de pontos da rota.
     * - Lista não pode ser nula ou vazia
     * - Cada ponto precisa estar dentro dos limites de lat/lng
     */
    private static boolean isValidPoints(List<LatLng> points) {
        if (points == null || points.isEmpty()) {
            return false;
        }

        for (LatLng point : points) {
            if (point == null) {
                return false;
            }

            double lat = point.getLatitude();
            double lng = point.getLongitude();

            if (lat < -90.0 || lat > 90.0) {
                return false;
            }
            if (lng < -180.0 || lng > 180.0) {
                return false;
            }
        }

        return true;
    }
}