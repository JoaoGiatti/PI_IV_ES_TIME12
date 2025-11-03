package com.maligno.server;

import com.maligno.client.Parceiro;
import com.maligno.statement.ComunicadoDeDesligamento;
import com.maligno.utils.Teclado;

import java.util.ArrayList;
import java.util.List;

public class Servidor {
    public static String PORTA_PADRAO = "3000";
    private static final String CMD_DESATIVAR = "desativar";
    
    public static void main (String[] args) {
        ArrayList<Parceiro> users = new ArrayList<> ();

        AceitadoraDeConexao aceitadoraDeConexao;
        try {
            aceitadoraDeConexao = new AceitadoraDeConexao (Servidor.PORTA_PADRAO, users);
            aceitadoraDeConexao.start();
        }
        catch (Exception erro) {
            System.err.println("Escolha uma porta apropriada e liberada para uso!\n");
            return;
        }

        loopDeComandos(users);
    }

    private static void loopDeComandos(ArrayList<Parceiro> users) {
        while (true) {
            System.out.println("O servidor anti-cheat do Chase esta ativo! Para desativa-lo,");
            System.out.println("use o comando \"desativar\"\n");
            System.out.print("> ");

            String comando;
            try {
                comando = Teclado.getUmString();
            } catch (Exception e) {
                System.err.println("Não foi possível ler o comando.\n");
                continue;
            }

            if (CMD_DESATIVAR.equalsIgnoreCase(comando)) {
                desligarServidorUsuarios(users);
            } else {
                System.err.println("Comando inválido!\n");
            }
        }
    }

    private static void desligarServidorUsuarios(List<Parceiro> users) {
        final ComunicadoDeDesligamento aviso = new ComunicadoDeDesligamento();

        synchronized (users) {
            for (Parceiro u : users) {
                try {
                    u.receba(aviso);
                    u.adeus();
                } catch (Exception ignored) {}
            }
        }

        System.out.println("O servidor anti-cheat do Chase foi desativado!\n");
        System.exit(0);
    }
}
