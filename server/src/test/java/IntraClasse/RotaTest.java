package IntraClasse;

import com.maligno.client.LatLng;
import com.maligno.client.Rota;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RotaTest {

    private Rota criarRotaValidaMock() {
        List<LatLng> pontos = Arrays.asList(
                new LatLng(-23.561684, -46.625378),
                new LatLng(-23.562000, -46.626000),
                new LatLng(-23.563000, -46.627000)
        );

        return new Rota(
                "user123",
                "Corrida da manhã",
                "Treino leve",
                "Parque A",
                "Parque B",
                5000.0,
                "00:25:00",
                pontos
        );
    }

    @Test
    void construtorCompleto_QuandoParametrosValidos_PreencheTodosOsCamposCorretamente() {
        // Arrange
        String uid = "user123";
        String name = "Corrida da manhã";
        String description = "Treino leve";
        String startLocation = "Parque A";
        String endLocation = "Parque B";
        double distance = 5000.0;
        String recordTime = "00:25:00";
        List<LatLng> pontos = Arrays.asList(
                new LatLng(-23.5, -46.6),
                new LatLng(-23.6, -46.7)
        );

        // Act
        Rota rota = new Rota(uid, name, description, startLocation, endLocation, distance, recordTime, pontos);

        // Assert
        assertAll(
                () -> assertEquals(uid, rota.getUid()),
                () -> assertEquals(name, rota.getName()),
                () -> assertEquals(description, rota.getDescription()),
                () -> assertEquals(startLocation, rota.getStartLocation()),
                () -> assertEquals(endLocation, rota.getEndLocation()),
                () -> assertEquals(distance, rota.getDistance()),
                () -> assertEquals(recordTime, rota.getRecordTime()),
                () -> assertEquals(pontos, rota.getPoints())
        );
    }

    @Test
    void construtorCompleto_QuandoListaDePontosForNull_InicializaListaVazia() {
        // Arrange
        String uid = "user123";
        String name = "Corrida";
        String description = "Desc";
        String startLocation = "A";
        String endLocation = "B";
        double distance = 1000.0;
        String recordTime = "00:05:00";

        // Act
        Rota rota = new Rota(uid, name, description, startLocation, endLocation, distance, recordTime, null);

        // Assert
        assertNotNull(rota.getPoints(), "A lista de pontos não deve ser nula");
        assertTrue(rota.getPoints().isEmpty(), "A lista de pontos deve ser vazia quando null for informado");
    }

    @Test
    void setters_QuandoChamados_AtualizamCamposCorretamente() {
        // Arrange
        Rota rota = new Rota("", "", "", "", "", 0.0, "", null);

        String uid = "u1";
        String name = "Nome";
        String description = "Desc";
        String startLocation = "Origem";
        String endLocation = "Destino";
        double distance = 1234.5;
        String recordTime = "01:02:03";
        List<LatLng> pontos = new ArrayList<>();
        pontos.add(new LatLng(-23.0, -46.0));

        // Act
        rota.setUid(uid);
        rota.setName(name);
        rota.setDescription(description);
        rota.setStartLocation(startLocation);
        rota.setEndLocation(endLocation);
        rota.setDistance(distance);
        rota.setRecordTime(recordTime);
        rota.setPoints(pontos);

        // Assert
        assertAll(
                () -> assertEquals(uid, rota.getUid()),
                () -> assertEquals(name, rota.getName()),
                () -> assertEquals(description, rota.getDescription()),
                () -> assertEquals(startLocation, rota.getStartLocation()),
                () -> assertEquals(endLocation, rota.getEndLocation()),
                () -> assertEquals(distance, rota.getDistance()),
                () -> assertEquals(recordTime, rota.getRecordTime()),
                () -> assertEquals(pontos, rota.getPoints())
        );
    }

    @Test
    void equals_QuandoObjetosPossuemMesmosValores_RetornaTrue() {
        // Arrange
        Rota rota1 = criarRotaValidaMock();
        Rota rota2 = criarRotaValidaMock();

        // Act
        boolean iguais = rota1.equals(rota2);

        // Assert
        assertTrue(iguais, "Rotas com os mesmos dados devem ser consideradas iguais");
        assertEquals(rota1.hashCode(), rota2.hashCode(), "hashCode deve ser igual para objetos iguais");
    }

    @Test
    void equals_QuandoAlgumCampoForDiferente_RetornaFalse() {
        // Arrange
        Rota rota1 = criarRotaValidaMock();
        Rota rota2 = criarRotaValidaMock();

        rota1.setUid("uidDiferente");

        // Act
        boolean iguais = rota1.equals(rota2);

        // Assert
        assertFalse(iguais, "Rotas com dados diferentes não devem ser consideradas iguais");
    }

    @Test
    void toString_QuandoChamado_ContemInformacoesPrincipais() {
        // Arrange
        Rota rota = criarRotaValidaMock();

        // Act
        String str = rota.toString();

        // Assert
        assertAll(
                () -> assertTrue(str.contains("user123"), "toString deve conter o uid"),
                () -> assertTrue(str.contains("Corrida da manhã"), "toString deve conter o nome"),
                () -> assertTrue(str.contains("Parque A"), "toString deve conter a origem"),
                () -> assertTrue(str.contains("Parque B"), "toString deve conter o destino")
        );
    }
}
