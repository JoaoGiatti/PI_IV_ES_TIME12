package br.com.chase.services;

import br.com.chase.exceptions.NotFoundException;
import br.com.chase.models.Rota;
import br.com.chase.models.LatLng;
import br.com.chase.models.Ranking;
import br.com.chase.repositories.RotaRepository;
import br.com.chase.exceptions.BadRequestException;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class RotaService {

    private final RotaRepository rotaRepository;

    public RotaService(RotaRepository rotaRepository) {
        this.rotaRepository = rotaRepository;
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

        rota.setCreatedAt(new Date());
        rota.setPublic(true); // padrão

        // Calcular velocidade média
        double velocidadeMedia = calcularVelocidadeMedia(
                rota.getDistance(),
                rota.getRecordTime()
        );
        rota.setBestAverageSpeed(velocidadeMedia);

        // Calcular calorias estimadas (simplificado)
        rota.setEstimatedCalories(rota.getDistance() * 60);

        // Criar ranking inicial (criador é o primeiro)
        Ranking rankingInicial = new Ranking(
                rota.getUid(),
                "Criador da rota", // Pode ser substituído depois pelo nome real vindo do app
                null, // foto de perfil, caso tenha
                rota.getRecordTime(),
                velocidadeMedia
        );
        rota.setTop3(List.of(rankingInicial));

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
}
