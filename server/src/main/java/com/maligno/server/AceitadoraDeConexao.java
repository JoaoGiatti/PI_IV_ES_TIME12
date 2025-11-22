package com.maligno.server;

import com.maligno.client.Parceiro;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class AceitadoraDeConexao extends Thread
{
    private ServerSocket        pedido;
    private ArrayList<Parceiro> usuarios;

    public AceitadoraDeConexao(String porta, ArrayList<Parceiro> usuarios) throws Exception {
        if (porta == null) throw new Exception ("Porta ausente");

        try {
            this.pedido = new ServerSocket (Integer.parseInt(porta));
        }
        catch (Exception  erro) {
            throw new Exception ("Porta invalida");
        }

        if (usuarios == null) throw new Exception ("Usuarios ausentes");

        this.usuarios = usuarios;
    }

    public void run () {
        for(;;) {
            Socket conexao;
            try {
                conexao = this.pedido.accept();
            }
            catch (Exception erro) {
                continue;
            }

            SupervisoraDeConexao supervisoraDeConexao = null;
            try {
                supervisoraDeConexao = new SupervisoraDeConexao(conexao, usuarios);
            }
            catch (Exception erro) {} // sei que passei parametros corretos para o construtor

            supervisoraDeConexao.start();
        }
    }
}
