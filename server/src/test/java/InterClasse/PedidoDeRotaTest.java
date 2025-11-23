package InterClasse;

import Data.PedidoDeRotaMassaDados;
import com.maligno.client.PedidoDeRota;
import com.maligno.client.PedidoDeRota.ResultadoValidacao;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PedidoDeRotaTest {

    private PedidoDeRota criarPedidoValido() {
        return PedidoDeRotaMassaDados.pedidoValido();
    }

    private void imprimirResultado(String caso, ResultadoValidacao resultado) {
        System.out.println("=== " + caso + " ===");
        System.out.println("Válido? " + resultado.isValido());
        System.out.println("Erros : " + resultado.getErros());
        System.out.println("======================");
    }

    // Testes de caso feliz
    @Test
    @DisplayName("Deve retornar true para um pedido totalmente válido")
    void deveRetornarTrueQuandoPedidoValido() {
        // Arrange
        PedidoDeRota pedido = criarPedidoValido();

        // Act
        ResultadoValidacao resultado = pedido.validarComResultado();
        imprimirResultado("Pedido válido", resultado);

        // Assert
        assertTrue(resultado.isValido());
        assertTrue(resultado.getErros().isEmpty(), "Não deveria haver erros para pedido válido");
    }

    // Testes de campos obrigatórios
    @Test
    @DisplayName("Deve retornar false quando UID for nulo ou vazio")
    void deveRetornarFalseQuandoUidInvalido() {
        // Arrange
        PedidoDeRota pedidoNulo = PedidoDeRotaMassaDados.pedidoComCampos(
                null,
                "Nome",
                "Desc",
                "Start",
                "End",
                1000.0,
                "00:10:00"
        );

        PedidoDeRota pedidoVazio = PedidoDeRotaMassaDados.pedidoComCampos(
                "   ",
                "Nome",
                "Desc",
                "Start",
                "End",
                1000.0,
                "00:10:00"
        );

        // Act
        ResultadoValidacao resultadoNulo = pedidoNulo.validarComResultado();
        ResultadoValidacao resultadoVazio = pedidoVazio.validarComResultado();

        imprimirResultado("UID nulo", resultadoNulo);
        imprimirResultado("UID vazio", resultadoVazio);

        // Assert
        assertFalse(resultadoNulo.isValido());
        assertFalse(resultadoVazio.isValido());

        assertTrue(resultadoNulo.getErros().contains("UID é obrigatório."));
        assertTrue(resultadoVazio.getErros().contains("UID é obrigatório."));
    }

    @Test
    @DisplayName("Deve retornar false quando nome for nulo ou vazio")
    void deveRetornarFalseQuandoNomeInvalido() {
        // Arrange
        PedidoDeRota pedidoNulo =
                PedidoDeRotaMassaDados.pedidoComCampos("uid", null, "Desc", "Start", "End", 1000.0, "00:10:00");

        PedidoDeRota pedidoVazio =
                PedidoDeRotaMassaDados.pedidoComCampos("uid", "   ", "Desc", "Start", "End", 1000.0, "00:10:00");

        // Act
        ResultadoValidacao resultadoNulo = pedidoNulo.validarComResultado();
        ResultadoValidacao resultadoVazio = pedidoVazio.validarComResultado();

        imprimirResultado("Nome nulo", resultadoNulo);
        imprimirResultado("Nome vazio", resultadoVazio);

        // Assert
        assertFalse(resultadoNulo.isValido());
        assertFalse(resultadoVazio.isValido());

        assertTrue(resultadoNulo.getErros().contains("Nome é obrigatório."));
        assertTrue(resultadoVazio.getErros().contains("Nome é obrigatório."));
    }

    @Test
    @DisplayName("Deve retornar false quando descrição for nula ou vazia")
    void deveRetornarFalseQuandoDescricaoInvalida() {
        // Arrange
        PedidoDeRota pedidoNulo =
                PedidoDeRotaMassaDados.pedidoComCampos("uid", "Nome", null, "Start", "End", 1000.0, "00:10:00");

        PedidoDeRota pedidoVazio =
                PedidoDeRotaMassaDados.pedidoComCampos("uid", "Nome", "   ", "Start", "End", 1000.0, "00:10:00");

        // Act
        ResultadoValidacao resultadoNulo = pedidoNulo.validarComResultado();
        ResultadoValidacao resultadoVazio = pedidoVazio.validarComResultado();

        imprimirResultado("Descrição nula", resultadoNulo);
        imprimirResultado("Descrição vazia", resultadoVazio);

        // Assert
        assertFalse(resultadoNulo.isValido());
        assertFalse(resultadoVazio.isValido());

        assertTrue(resultadoNulo.getErros().contains("Descrição é obrigatória."));
        assertTrue(resultadoVazio.getErros().contains("Descrição é obrigatória."));
    }

    @Test
    @DisplayName("Deve retornar false quando startLocation for nulo ou vazio")
    void deveRetornarFalseQuandoStartLocationInvalida() {
        // Arrange
        PedidoDeRota pedidoNulo =
                PedidoDeRotaMassaDados.pedidoComCampos("uid", "Nome", "Desc", null, "End", 1000.0, "00:10:00");

        PedidoDeRota pedidoVazio =
                PedidoDeRotaMassaDados.pedidoComCampos("uid", "Nome", "Desc", "   ", "End", 1000.0, "00:10:00");

        // Act
        ResultadoValidacao resultadoNulo = pedidoNulo.validarComResultado();
        ResultadoValidacao resultadoVazio = pedidoVazio.validarComResultado();

        imprimirResultado("StartLocation nula", resultadoNulo);
        imprimirResultado("StartLocation vazia", resultadoVazio);

        // Assert
        assertFalse(resultadoNulo.isValido());
        assertFalse(resultadoVazio.isValido());

        assertTrue(resultadoNulo.getErros().contains("Local de início (startLocation) é obrigatório."));
        assertTrue(resultadoVazio.getErros().contains("Local de início (startLocation) é obrigatório."));
    }

    @Test
    @DisplayName("Deve retornar false quando endLocation for nulo ou vazio")
    void deveRetornarFalseQuandoEndLocationInvalida() {
        // Arrange
        PedidoDeRota pedidoNulo =
                PedidoDeRotaMassaDados.pedidoComCampos("uid", "Nome", "Desc", "Start", null, 1000.0, "00:10:00");

        PedidoDeRota pedidoVazio =
                PedidoDeRotaMassaDados.pedidoComCampos("uid", "Nome", "Desc", "Start", "   ", 1000.0, "00:10:00");

        // Act
        ResultadoValidacao resultadoNulo = pedidoNulo.validarComResultado();
        ResultadoValidacao resultadoVazio = pedidoVazio.validarComResultado();

        imprimirResultado("EndLocation nula", resultadoNulo);
        imprimirResultado("EndLocation vazia", resultadoVazio);

        // Assert
        assertFalse(resultadoNulo.isValido());
        assertFalse(resultadoVazio.isValido());

        assertTrue(resultadoNulo.getErros().contains("Local de término (endLocation) é obrigatório."));
        assertTrue(resultadoVazio.getErros().contains("Local de término (endLocation) é obrigatório."));
    }

    // Testes de distância
    @Test
    @DisplayName("Deve retornar false quando distância for nula ou menor/igual a zero")
    void deveRetornarFalseQuandoDistanciaInvalida() {
        // Arrange
        PedidoDeRota pedidoDistanciaNula =
                PedidoDeRotaMassaDados.pedidoComCampos("uid", "Nome", "Desc", "Start", "End", null, "00:10:00");

        PedidoDeRota pedidoDistanciaZero =
                PedidoDeRotaMassaDados.pedidoComCampos("uid", "Nome", "Desc", "Start", "End", 0.0, "00:10:00");

        PedidoDeRota pedidoDistanciaNegativa =
                PedidoDeRotaMassaDados.pedidoComCampos("uid", "Nome", "Desc", "Start", "End", -100.0, "00:10:00");

        // Act
        ResultadoValidacao resultadoNulo = pedidoDistanciaNula.validarComResultado();
        ResultadoValidacao resultadoZero = pedidoDistanciaZero.validarComResultado();
        ResultadoValidacao resultadoNegativo = pedidoDistanciaNegativa.validarComResultado();

        imprimirResultado("Distância nula", resultadoNulo);
        imprimirResultado("Distância zero", resultadoZero);
        imprimirResultado("Distância negativa", resultadoNegativo);

        // Assert
        assertFalse(resultadoNulo.isValido());
        assertFalse(resultadoZero.isValido());
        assertFalse(resultadoNegativo.isValido());

        assertTrue(resultadoNulo.getErros().contains("Distância é obrigatória."));
        assertTrue(resultadoZero.getErros().contains("Distância deve ser maior que zero."));
        assertTrue(resultadoNegativo.getErros().contains("Distância deve ser maior que zero."));
    }

    // Testes de recordTime
    @Test
    @DisplayName("Deve retornar false quando recordTime for nulo ou vazio")
    void deveRetornarFalseQuandoRecordTimeInvalido() {
        // Arrange
        PedidoDeRota pedidoNulo =
                PedidoDeRotaMassaDados.pedidoComCampos("uid", "Nome", "Desc", "Start", "End", 1000.0, null);

        PedidoDeRota pedidoVazio =
                PedidoDeRotaMassaDados.pedidoComCampos("uid", "Nome", "Desc", "Start", "End", 1000.0, "   ");

        // Act
        ResultadoValidacao resultadoNulo = pedidoNulo.validarComResultado();
        ResultadoValidacao resultadoVazio = pedidoVazio.validarComResultado();

        imprimirResultado("recordTime nulo", resultadoNulo);
        imprimirResultado("recordTime vazio", resultadoVazio);

        // Assert
        assertFalse(resultadoNulo.isValido());
        assertFalse(resultadoVazio.isValido());

        assertTrue(resultadoNulo.getErros().contains("Tempo de recorde (recordTime) é obrigatório."));
        assertTrue(resultadoVazio.getErros().contains("Tempo de recorde (recordTime) é obrigatório."));
    }

    @Test
    @DisplayName("Deve retornar false quando recordTime tiver formato inválido")
    void deveRetornarFalseQuandoRecordTimeFormatoInvalido() {
        // Arrange
        PedidoDeRota pedidoFormatoIncorreto =
                PedidoDeRotaMassaDados.pedidoComCampos("uid", "Nome", "Desc", "Start", "End", 1000.0, "invalido"
        );

        // Act
        ResultadoValidacao resultado = pedidoFormatoIncorreto.validarComResultado();
        imprimirResultado("recordTime formato inválido", resultado);

        // Assert
        assertFalse(resultado.isValido());
        assertTrue(
                resultado.getErros().stream()
                        .anyMatch(msg -> msg.contains("formato HH:mm:ss")),
                "Deveria ter erro sobre formato HH:mm:ss"
        );
    }

    // Testes de velocidade física
    @Test
    @DisplayName("Deve retornar false quando tempo total for zero (velocidade infinita)")
    void deveRetornarFalseQuandoTempoTotalZero() {
        // Arrange
        PedidoDeRota pedidoTempoZero =
                PedidoDeRotaMassaDados.pedidoComCampos("uid", "Nome", "Desc", "Start", "End", 1000.0, "00:00:00"
        );

        // Act
        ResultadoValidacao resultado = pedidoTempoZero.validarComResultado();
        imprimirResultado("Tempo total zero", resultado);

        // Assert
        assertFalse(resultado.isValido());
        assertTrue(
                resultado.getErros().stream()
                        .anyMatch(msg -> msg.contains("Tempo total (recordTime) deve ser maior que zero.")),
                "Deveria ter erro sobre tempo total maior que zero"
        );
    }

    @Test
    @DisplayName("Deve permitir velocidade igual ao limite de 12.5 m/s (Bolt)")
    void devePermitirVelocidadeIgualAoLimite() {
        // Arrange
        PedidoDeRota pedidoLimite = PedidoDeRotaMassaDados.pedidoComVelocidade(100.0, "00:00:08");

        // Act
        ResultadoValidacao resultado = pedidoLimite.validarComResultado();
        imprimirResultado("Velocidade igual ao limite", resultado);

        // Assert
        assertTrue(resultado.isValido());
        assertTrue(resultado.getErros().isEmpty());
    }

    @Test
    @DisplayName("Deve retornar false quando velocidade exceder 12.5 m/s")
    void deveRetornarFalseQuandoVelocidadeAcimaDoLimite() {
        // Arrange
        PedidoDeRota pedidoAcima = PedidoDeRotaMassaDados.pedidoComVelocidade(101.0, "00:00:08");

        // Act
        ResultadoValidacao resultado = pedidoAcima.validarComResultado();
        imprimirResultado("Velocidade acima do limite", resultado);

        // Assert
        assertFalse(resultado.isValido());
        assertTrue(
                resultado.getErros().stream()
                        .anyMatch(msg -> msg.contains("Velocidade média inválida")),
                "Deveria ter erro de velocidade média inválida"
        );
    }

    // Teste de compatibilidade com validar()
    @Test
    @DisplayName("validar() (boolean) deve ser consistente com validarComResultado()")
    void validarBooleanDeveSerConsistenteComValidarComResultado() {
        // Arrange
        PedidoDeRota pedido = criarPedidoValido();

        // Act
        boolean validoBoolean = pedido.validar();
        ResultadoValidacao resultado = pedido.validarComResultado();
        imprimirResultado("Compatibilidade validar()", resultado);

        // Assert
        assertEquals(validoBoolean, resultado.isValido());
    }
}
