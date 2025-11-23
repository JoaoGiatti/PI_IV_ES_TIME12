package Data;

import com.maligno.client.PedidoDeRota;

public final class PedidoDeRotaMassaDados {

    private PedidoDeRotaMassaDados() {}

    public static PedidoDeRota pedidoValido() {
        return new PedidoDeRota(
                "uid-123",
                "Corrida Matinal",
                "Treino leve de corrida",
                "Praça Central",
                "Parque da Cidade",
                10000.0,        // 10 km em metros
                "00:50:00"      // 50 minutos -> ~3,33 m/s
        );
    }

    public static PedidoDeRota pedidoComVelocidade(double metros, String tempo) {
        return new PedidoDeRota(
                "uid-vel",
                "Teste Velocidade",
                "Teste de velocidade média",
                "Ponto A",
                "Ponto B",
                metros,
                tempo
        );
    }

    public static PedidoDeRota pedidoComCampos(
            String uid,
            String name,
            String description,
            String startLocation,
            String endLocation,
            Double distance,
            String recordTime
    ) {
        return new PedidoDeRota(
                uid,
                name,
                description,
                startLocation,
                endLocation,
                distance,
                recordTime
        );
    }
}
