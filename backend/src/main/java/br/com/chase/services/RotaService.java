package br.com.chase.services;

import br.com.chase.exceptions.NotFoundException;
import br.com.chase.exceptions.RotaNotFoundException;
import br.com.chase.models.*;
import br.com.chase.repositories.RotaRepository;
import br.com.chase.exceptions.BadRequestException;
import br.com.chase.repositories.UsuarioRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class RotaService {

    private final RotaRepository rotaRepository;
    private final UsuarioRepository usuarioRepository; // ✅ precisa injetar isso

    public RotaService(RotaRepository rotaRepository, UsuarioRepository usuarioRepository) {
        this.rotaRepository = rotaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    private long parseTimeToMs(String timeString) {
        String[] parts = timeString.split(":");

        if (parts.length == 2) {
            int minutes = Integer.parseInt(parts[0]);
            int seconds = Integer.parseInt(parts[1]);
            return (minutes * 60L + seconds) * 1000;
        }

        if (parts.length == 3) {
            int hours = Integer.parseInt(parts[0]);
            int minutes = Integer.parseInt(parts[1]);
            int seconds = Integer.parseInt(parts[2]);
            return (hours * 3600L + minutes * 60L + seconds) * 1000;
        }

        throw new RuntimeException("Formato de tempo inválido: " + timeString);
    }

    private void validarRota(Rota rota) {
        if (rota.getUid() == null || rota.getUid().isBlank())
            throw new BadRequestException("O campo 'criadorId' é obrigatório.");

        if (rota.getName() == null || rota.getName().isBlank())
            throw new BadRequestException("O campo 'nome' é obrigatório.");

        if (rota.getDistance() <= 0)
            throw new BadRequestException("A distância deve ser maior que zero.");

        if (rota.getRecordTime() == null  || rota.getRecordTime().isBlank())
            throw new BadRequestException("O tempo é obrigatório.");

        List<LatLng> latLngs = rota.getPoints();
        if (latLngs == null || latLngs.isEmpty())
            throw new BadRequestException("A lista de pontos não pode estar vazia.");
    }

    private double calcularVelocidadeMedia(double distancia, String tempoRecorde) {
        try {
            String[] partes = tempoRecorde.split(":");

            int horas = Integer.parseInt(partes[0]);
            int minutos = Integer.parseInt(partes[1]);
            int segundos = Integer.parseInt(partes[2]);

            double tempoEmHoras = horas + (minutos / 60.0) + (segundos / 3600.0);
            return (distancia / 1000.0) / tempoEmHoras;

        } catch (Exception e) {
            throw new BadRequestException("Formato de tempo inválido. Use HH:mm:ss");
        }
    }

    public Rota criarRota(Rota rota) {
        validarRota(rota);

        rota.setCompetitors(1);
        rota.setCreatedAt(new Date());
        rota.setPublic(true);

        // Calcular velocidade média (p/ recorde)
        double velocidadeMedia = calcularVelocidadeMedia(
                rota.getDistance(),
                rota.getRecordTime()
        );
        rota.setBestAverageSpeed(velocidadeMedia);

        // Distância em km
        double distanciaKm = rota.getDistance() / 1000.0;

        // Peso médio (caso queira futuramente buscar do usuário)
        double pesoPadraoKg = 70;

        // Fórmula aproximada para corrida: 1.036 kcal por kg por km
        double caloriasEstimadas = distanciaKm * pesoPadraoKg * 1.036;

        rota.setEstimatedCalories(caloriasEstimadas);

        // Tentar buscar o usuário real pelo UID (se existir)
        String creatorUid = rota.getUid();
        String creatorName = "Criador da rota";
        String creatorPhoto = null;

        // Implementação sem lambda para conseguir setar as variáveis acima:
        if (creatorUid != null && !creatorUid.isBlank()) {
            Optional<Usuario> maybeUser = usuarioRepository.findById(creatorUid);
            if (maybeUser.isPresent()) {
                Usuario usuario = maybeUser.get();
                creatorName = usuario.getDisplayName() != null && !usuario.getDisplayName().isBlank()
                        ? usuario.getDisplayName()
                        : creatorName;
                creatorPhoto = usuario.getPhotoUrl();
            }
        }

        // Criar RotaRecord inicial com dados existentes
        Ranking initial = new Ranking(
                creatorUid,
                creatorName,
                creatorPhoto,
                rota.getRecordTime(),
                velocidadeMedia
        );

        // Inicializa top3 com o criador como 1º colocado
        rota.setTop3(List.of(initial));

        // Salvar no banco
        return rotaRepository.save(rota);
    }

    public List<Rota> buscarRotasPorUsuario(String userId) {
        List<Rota> rotas = rotaRepository.findByUid(userId);
        if (rotas.isEmpty()) {
            throw new NotFoundException("Nenhuma rota encontrada para o usuário informado.");
        }
        return rotas;
    }

    public List<Rota> getPublicRoutes(){
        return rotaRepository.findByIsPublicTrue();
    }

    public Rota getRotaById(String rid) {
        return rotaRepository.findById(rid)
                .orElseThrow(() -> new RotaNotFoundException("Rota com ID " + rid + " não encontrada."));
    }

    public void deletarRota(String rid) {
        Rota rota = rotaRepository.findById(rid)
                .orElseThrow(() -> new RotaNotFoundException("Rota com ID " + rid + " não encontrada."));

        rotaRepository.delete(rota);
    }

    public Rota togglePublic(String rid) {
        Rota rota = rotaRepository.findById(rid)
                .orElseThrow(() -> new RuntimeException("Rota não encontrada"));

        rota.setPublic(!rota.isPublic()); // alterna TRUE ↔ FALSE
        return rotaRepository.save(rota);
    }

    public Map<String, Object> registerRecord(String rid, String uid, String totalTime) {

        Rota route = rotaRepository.findById(rid)
                .orElseThrow(() -> new RuntimeException("Rota não encontrada."));

        Usuario user = usuarioRepository.findByUid(uid);
        if (user == null) throw new RuntimeException("Usuário não encontrado.");

        double avgSpeed = calcularVelocidadeMedia(route.getDistance(), totalTime);
        long newTimeMs = parseTimeToMs(totalTime);

        // ====== SALVAR RANKING ANTIGO PARA COMPARAÇÃO ======
        List<Ranking> oldTop3 = new ArrayList<>(route.getTop3());

        List<Ranking> ranking = new ArrayList<>(route.getTop3());

        Ranking existing = ranking.stream()
                .filter(r -> r.getUid().equals(uid))
                .findFirst()
                .orElse(null);

        if (existing != null) {
            long oldTimeMs = parseTimeToMs(existing.getTotalTime());

            if (newTimeMs >= oldTimeMs) {
                return Map.of(
                        "message", "Tempo registrado, mas você já possui um tempo melhor.",
                        "position", ranking.indexOf(existing) + 1,
                        "top3", ranking,
                        "overtaken", List.of() // ninguém foi ultrapassado
                );
            }

            ranking.remove(existing);
        }

        Ranking record = new Ranking(
                uid,
                user.getDisplayName(),
                user.getPhotoUrl(),
                totalTime,
                avgSpeed
        );

        ranking.add(record);

        ranking.sort(Comparator.comparingLong(r -> parseTimeToMs(r.getTotalTime())));

        List<Ranking> newTop3 = ranking.subList(0, Math.min(3, ranking.size()));

        boolean entrouNoTop3 = newTop3.contains(record);

        // ====== CALCULAR QUEM FOI ULTRAPASSADO ======
        List<String> overtaken = new ArrayList<>();

        for (Ranking oldR : oldTop3) {
            int oldPos = oldTop3.indexOf(oldR);
            int newPos = newTop3.indexOf(oldR);

            // caso tenha saído do top3 => perdeu posição
            if (newPos == -1) {
                overtaken.add(oldR.getUid());
            } else if (newPos > oldPos) {
                // caso tenha descido no ranking
                overtaken.add(oldR.getUid());
            }
        }

        // Atualiza dados da rota
        route.setTop3(newTop3);
        boolean competiuAntes = existing != null;
        boolean criadorDaRota = route.getUid().equals(uid);

        if (!competiuAntes && !criadorDaRota) {
            route.setCompetitors(route.getCompetitors() + 1);
        }
        route.setRecordTime(newTop3.get(0).getTotalTime());
        route.setBestAverageSpeed(
                newTop3.stream().mapToDouble(Ranking::getAverageSpeed).max().orElse(avgSpeed)
        );

        rotaRepository.save(route);

        // ====== RETORNO COMPLETO ======
        return Map.of(
                "message", entrouNoTop3 ? "Entrou no top 3!" : "Tempo registrado.",
                "top3", newTop3,
                "enteredTop3", entrouNoTop3,
                "position", newTop3.indexOf(record) + 1,
                "overtaken", overtaken // << UID DOS QUE FORAM ULTRAPASSADOS
        );
    }
}
