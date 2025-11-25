package InterClasse;

import com.maligno.client.LatLng;
import com.maligno.client.Rota;
import com.maligno.infra.AceitadoraDeConexao;
import com.maligno.infra.Parceiro;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ServidorIntegracaoTest {

    private static final String HOST = "localhost";
    private static final int PORT = 3000;

    private static AceitadoraDeConexao aceitadoraDeConexao;
    private static ArrayList<Parceiro> usuarios;

    @BeforeAll
    static void servidor_QuandoIniciado_FicaProntoParaReceberConexoes() throws Exception {
        // Arrange
        usuarios = new ArrayList<>();

        // Act
        aceitadoraDeConexao = new AceitadoraDeConexao(String.valueOf(PORT), usuarios);
        aceitadoraDeConexao.start();
        Thread.sleep(200); // pequeno delay para subir a porta

        // Assert (implícito): se der problema, os testes de conexão vão falhar
    }

    @AfterAll
    static void servidor_QuandoFinaliza_InterrompeThreadDeAceitacao() {
        // Arrange & Act
        if (aceitadoraDeConexao != null && aceitadoraDeConexao.isAlive()) {
            aceitadoraDeConexao.interrupt();
        }
        // Assert: encerramento acontece ao final dos testes
    }

    @Test
    void validacaoRemota_QuandoRotaValida_RetornaTrue() throws Exception {
        // Arrange
        Rota rotaValida = new Rota(
                "user123",
                "Corrida da Manhã",
                "Treino leve de 5k",
                "Parque A",
                "Parque B",
                5000.0,
                "00:25:00",
                Arrays.asList(
                        new LatLng(-23.561684, -46.625378),
                        new LatLng(-23.562000, -46.626000),
                        new LatLng(-23.563000, -46.627000)
                )
        );

        // Act
        String resposta = enviarRotaParaServidor(rotaValida);

        // Assert
        assertEquals("true", resposta);
    }

    @Test
    void validacaoRemota_QuandoTempoImpossivelParaDistancia_RetornaFalse() throws Exception {
        // Arrange
        Rota rotaImpossivel = new Rota(
                "user123",
                "Rota Impossível",
                "20km em 15min",
                "Ponto A",
                "Ponto B",
                20000.0,
                "00:15:00", // impossível
                Arrays.asList(
                        new LatLng(-23.561684, -46.625378),
                        new LatLng(-23.562000, -46.626000)
                )
        );

        // Act
        String resposta = enviarRotaParaServidor(rotaImpossivel);

        // Assert
        assertEquals("false", resposta);
    }

    @Test
    void validacaoRemota_QuandoPontosInvalidos_RetornaFalse() throws Exception {
        // Arrange
        List<LatLng> pontosInvalidos = Arrays.asList(
                new LatLng(200.0, -46.625378),   // latitude inválida
                new LatLng(-23.562000, -46.626000)
        );

        Rota rotaComPontosInvalidos = new Rota(
                "user123",
                "Rota com pontos inválidos",
                "Teste de pontos fora do intervalo",
                "Lugar X",
                "Lugar Y",
                3000.0,
                "00:20:00",
                pontosInvalidos
        );

        // Act
        String resposta = enviarRotaParaServidor(rotaComPontosInvalidos);

        // Assert
        assertEquals("false", resposta);
    }

    private String enviarRotaParaServidor(Rota rota) throws Exception {
        try (Socket socket = new Socket(HOST, PORT);
             DataOutputStream out = new DataOutputStream(socket.getOutputStream());
             DataInputStream in = new DataInputStream(socket.getInputStream())) {

            // Envio dos campos básicos
            out.writeUTF(rota.getUid());
            out.writeUTF(rota.getName());
            out.writeUTF(rota.getDescription());
            out.writeUTF(rota.getStartLocation());
            out.writeUTF(rota.getEndLocation());
            out.writeUTF(Double.toString(rota.getDistance()));
            out.writeUTF(rota.getRecordTime());

            // Envio da lista de pontos
            List<LatLng> pontos = rota.getPoints();
            if (pontos == null) {
                pontos = new ArrayList<>();
            }

            out.writeInt(pontos.size());
            for (LatLng ponto : pontos) {
                out.writeDouble(ponto.getLatitude());
                out.writeDouble(ponto.getLongitude());
            }

            out.flush();

            // Resposta do servidor
            return in.readUTF();
        }
    }
}
