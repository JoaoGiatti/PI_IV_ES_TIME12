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
            return distancia / tempoEmHoras;
        } catch (Exception e) {
            throw new BadRequestException("Formato de tempo inválido. Use HH:mm:ss");
        }
    }


    public Rota criarRota(Rota rota) {
        validarRota(rota);

        rota.setCompetitors(1);
        rota.setCreatedAt(new Date());
        rota.setPublic(true); // padrão

        // Calcular velocidade média (p/ recorde)
        double velocidadeMedia = calcularVelocidadeMedia(
                rota.getDistance(),
                rota.getRecordTime()
        );
        rota.setBestAverageSpeed(velocidadeMedia);

        // Calcular calorias estimadas (simplificado)
        rota.setEstimatedCalories(rota.getDistance() * 60);

        // Tentar buscar o usuário real pelo UID (se existir)
        String creatorUid = rota.getUid();
        String creatorName = "Criador da rota";
        String creatorPhoto = null;

        if (creatorUid != null && !creatorUid.isBlank()) {
            usuarioRepository.findById(creatorUid).ifPresent(usuario -> {
                // usa lambda para preencher valores se existir
                // nota: não podemos atribuir diretamente aqui ao escopo externo se usamos lambda;
                // então vamos capturar via arrays ou reimplementar sem lambda. Vou reimplementar sem lambda abaixo.
            });
        }

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
        RotaRecord initial = new RotaRecord(
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

    public Map<String, Object> registerRecord(String rid, String uid, String timeString) {
        Rota route = rotaRepository.findById(rid)
                .orElseThrow(() -> new RuntimeException("Rota não encontrada."));

        // Converte tempo string em milissegundos para comparação
        long newTimeMs = parseTimeToMs(timeString);

        List<RotaRecord> ranking = route.getTop3();

        // Caso não tenha top3 inicial, cria
        if (ranking == null) ranking = new ArrayList<>();

        // Cria o novo record
        RotaRecord newRecord = new RotaRecord(uid, timeString);

        // Coloca na lista TEMPORÁRIA para ordenar
        List<RotaRecord> temp = new ArrayList<>(ranking);
        temp.add(newRecord);

        // Ordena do menor tempo pro maior
        temp.sort(Comparator.comparingLong(r -> parseTimeToMs(r.getTimeString())));

        // Pega apenas os top 3
        List<RotaRecord> updatedRanking = temp.stream().limit(3).toList();

        // Verifica se entrou no top 3
        boolean entrou = updatedRanking.contains(newRecord);

        // Atualiza a rota (somente os top3)
        route.setTop3(updatedRanking);
        rotaRepository.save(route);

        // Resposta final
        Map<String, Object> response = new HashMap<>();

        if (!entrou) {
            response.put("message", "Tempo registrado, mas não entrou no top 3.");
            response.put("position", temp.indexOf(newRecord) + 1);
            return response;
        }

        response.put("message", "Novo tempo registrado!");
        response.put("position", updatedRanking.indexOf(newRecord) + 1);
        response.put("top3", updatedRanking);
        return response;
    }

}
