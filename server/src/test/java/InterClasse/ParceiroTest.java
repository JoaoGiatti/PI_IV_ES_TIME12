package InterClasse;

import com.maligno.client.Parceiro;
import org.junit.Test;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.net.Socket;

import static org.mockito.Mockito.*;

public class ParceiroTest {

    @Test(expected = Exception.class)
    public void construtor_DeveFalhar_QuandoReceptorNull() throws Exception {
        Socket socket = mock(Socket.class);
        DataOutputStream transmissor = mock(DataOutputStream.class);

        new Parceiro(socket, null, transmissor);
    }

    @Test
    public void enviar_DeveEscreverNoTransmissor() throws Exception {
        Socket socket = mock(Socket.class);
        DataInputStream receptor = mock(DataInputStream.class);
        DataOutputStream transmissor = mock(DataOutputStream.class);

        Parceiro parceiro = new Parceiro(socket, receptor, transmissor);

        parceiro.enviar("msg");

        verify(transmissor).writeUTF("msg");
        verify(transmissor).flush();
    }

    @Test
    public void adeus_DeveFecharStreamsESocket() throws Exception {
        Socket socket = mock(Socket.class);
        DataInputStream receptor = mock(DataInputStream.class);
        DataOutputStream transmissor = mock(DataOutputStream.class);

        Parceiro parceiro = new Parceiro(socket, receptor, transmissor);

        parceiro.adeus();

        verify(transmissor).close();
        verify(receptor).close();
        verify(socket).close();
    }
}
