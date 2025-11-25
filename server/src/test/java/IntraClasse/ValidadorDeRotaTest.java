package IntraClasse;

import com.maligno.client.LatLng;
import com.maligno.client.Rota;
import com.maligno.client.ValidadorDeRota;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ValidadorDeRotaTest {

    private Rota criarRotaValida() {
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
    void isValid_QuandoRotaForNull_RetornaFalse() {
        // Arrange
        Rota rota = null;

        // Act
        boolean valido = ValidadorDeRota.isValid(rota);

        // Assert
        assertFalse(valido);
    }

    @Test
    void isValid_QuandoRotaForValida_RetornaTrue() {
        // Arrange
        Rota rota = criarRotaValida();

        // Act
        boolean valido = ValidadorDeRota.isValid(rota);

        // Assert
        assertTrue(valido);
    }

    @Test
    void isValid_QuandoUidForVazio_RetornaFalse() {
        // Arrange
        Rota rota = criarRotaValida();
        rota.setUid("   ");

        // Act
        boolean valido = ValidadorDeRota.isValid(rota);

        // Assert
        assertFalse(valido);
    }

    @Test
    void isValid_QuandoNomeForVazio_RetornaFalse() {
        // Arrange
        Rota rota = criarRotaValida();
        rota.setName("");

        // Act
        boolean valido = ValidadorDeRota.isValid(rota);

        // Assert
        assertFalse(valido);
    }

    @Test
    void isValid_QuandoDescricaoForVazia_RetornaFalse() {
        // Arrange
        Rota rota = criarRotaValida();
        rota.setDescription("   ");

        // Act
        boolean valido = ValidadorDeRota.isValid(rota);

        // Assert
        assertFalse(valido);
    }

    @Test
    void isValid_QuandoLocalInicialForVazio_RetornaFalse() {
        // Arrange
        Rota rota = criarRotaValida();
        rota.setStartLocation("");

        // Act
        boolean valido = ValidadorDeRota.isValid(rota);

        // Assert
        assertFalse(valido);
    }

    @Test
    void isValid_QuandoLocalFinalForVazio_RetornaFalse() {
        // Arrange
        Rota rota = criarRotaValida();
        rota.setEndLocation(null);

        // Act
        boolean valido = ValidadorDeRota.isValid(rota);

        // Assert
        assertFalse(valido);
    }

    @Test
    void isValid_QuandoDistanciaForZero_RetornaFalse() {
        // Arrange
        Rota rota = criarRotaValida();
        rota.setDistance(0.0);

        // Act
        boolean valido = ValidadorDeRota.isValid(rota);

        // Assert
        assertFalse(valido);
    }

    @Test
    void isValid_QuandoDistanciaForNegativa_RetornaFalse() {
        // Arrange
        Rota rota = criarRotaValida();
        rota.setDistance(-10.0);

        // Act
        boolean valido = ValidadorDeRota.isValid(rota);

        // Assert
        assertFalse(valido);
    }

    @Test
    void isValid_QuandoTempoForNulo_RetornaFalse() {
        // Arrange
        Rota rota = criarRotaValida();
        rota.setRecordTime(null);

        // Act
        boolean valido = ValidadorDeRota.isValid(rota);

        // Assert
        assertFalse(valido);
    }

    @Test
    void isValid_QuandoTempoForTextoLivre_RetornaFalse() {
        // Arrange
        Rota rota = criarRotaValida();
        rota.setRecordTime("abc");

        // Act
        boolean valido = ValidadorDeRota.isValid(rota);

        // Assert
        assertFalse(valido);
    }

    @Test
    void isValid_QuandoTempoEstiverEmFormatoIncorreto_RetornaFalse() {
        // Arrange
        Rota rota = criarRotaValida();
        rota.setRecordTime("25:00"); // faltando parte dos segundos

        // Act
        boolean valido = ValidadorDeRota.isValid(rota);

        // Assert
        assertFalse(valido);
    }

    @Test
    void isValid_QuandoTempoTiverNumerosForaDaFaixa_RetornaFalse() {
        // Arrange
        Rota rota = criarRotaValida();
        rota.setRecordTime("99:99:99");

        // Act
        boolean valido = ValidadorDeRota.isValid(rota);

        // Assert
        assertFalse(valido);
    }

    @Test
    void isValid_QuandoTempoForImpossivelParaDistancia_RetornaFalse() {
        // Arrange
        Rota rota = criarRotaValida();
        rota.setDistance(20000.0);      // 20 km
        rota.setRecordTime("00:15:00"); // 15 min → impossível

        // Act
        boolean valido = ValidadorDeRota.isValid(rota);

        // Assert
        assertFalse(valido);
    }

    @Test
    void isValid_QuandoListaDePontosForNula_RetornaFalse() {
        // Arrange
        Rota rota = criarRotaValida();
        rota.setPoints(null);

        // Act
        boolean valido = ValidadorDeRota.isValid(rota);

        // Assert
        assertFalse(valido);
    }

    @Test
    void isValid_QuandoListaDePontosForVazia_RetornaFalse() {
        // Arrange
        Rota rota = criarRotaValida();
        rota.setPoints(new ArrayList<>());

        // Act
        boolean valido = ValidadorDeRota.isValid(rota);

        // Assert
        assertFalse(valido);
    }

    @Test
    void isValid_QuandoAlgumPontoForNulo_RetornaFalse() {
        // Arrange
        List<LatLng> pontos = new ArrayList<>();
        pontos.add(new LatLng(-23.5, -46.6));
        pontos.add(null);

        Rota rota = criarRotaValida();
        rota.setPoints(pontos);

        // Act
        boolean valido = ValidadorDeRota.isValid(rota);

        // Assert
        assertFalse(valido);
    }

    @Test
    void isValid_QuandoLatitudeForInvalida_RetornaFalse() {
        // Arrange
        List<LatLng> pontos = Arrays.asList(
                new LatLng(100.0, -46.6),   // latitude inválida
                new LatLng(-23.5, -46.7)
        );

        Rota rota = criarRotaValida();
        rota.setPoints(pontos);

        // Act
        boolean valido = ValidadorDeRota.isValid(rota);

        // Assert
        assertFalse(valido);
    }

    @Test
    void isValid_QuandoLongitudeForInvalida_RetornaFalse() {
        // Arrange
        List<LatLng> pontos = Arrays.asList(
                new LatLng(-23.5, -200.0),  // longitude inválida
                new LatLng(-23.6, -46.7)
        );

        Rota rota = criarRotaValida();
        rota.setPoints(pontos);

        // Act
        boolean valido = ValidadorDeRota.isValid(rota);

        // Assert
        assertFalse(valido);
    }
}
