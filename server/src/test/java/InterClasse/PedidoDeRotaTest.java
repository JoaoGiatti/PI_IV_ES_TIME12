package InterClasse;

import com.maligno.client.PedidoDeRota;
import org.junit.*;
import java.io.OutputStream;
import java.io.PrintStream;

import static org.junit.Assert.*;

public class PedidoDeRotaTest {

    private PrintStream originalOut;

    @Before
    public void setUp() {
        originalOut = System.out;
        System.setOut(new PrintStream(OutputStream.nullOutputStream()));
    }

    @After
    public void tearDown() {
        System.setOut(originalOut);
    }

    @Test
    public void validar_DeveRetornarTrue_QuandoDadosCorretos() {
        PedidoDeRota pedido = new PedidoDeRota(
                "123",
                "Lucas",
                "Corrida",
                "Campinas",
                "Santos",
                10000.0,
                "01:00:00"
        );

        boolean resultado = pedido.validar();

        assertTrue(resultado);
    }

    @Test
    public void validar_DeveRetornarFalse_QuandoUidVazio() {
        PedidoDeRota pedido = new PedidoDeRota(
                "",
                "Lucas",
                "Desc",
                "A",
                "B",
                1000.0,
                "00:10:00"
        );

        boolean resultado = pedido.validar();

        assertFalse(resultado);
    }

    @Test
    public void validar_DeveRetornarFalse_QuandoDistanciaZero() {
        PedidoDeRota pedido = new PedidoDeRota(
                "123",
                "Lucas",
                "Desc",
                "A",
                "B",
                0.0,
                "00:10:00"
        );

        boolean resultado = pedido.validar();

        assertFalse(resultado);
    }

    @Test
    public void validar_DeveRetornarFalse_QuandoVelocidadeImpossivel() {
        PedidoDeRota pedido = new PedidoDeRota(
                "123",
                "Lucas",
                "Desc",
                "A",
                "B",
                1000.0,
                "00:01:00"
        );

        boolean resultado = pedido.validar();

        assertFalse(resultado);
    }
}
