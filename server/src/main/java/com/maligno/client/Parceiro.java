package com.maligno.client;

import java.io.*;
import java.net.Socket;

public class Parceiro {
    private Socket conexao;
    private DataInputStream  receptor;
    private DataOutputStream  transmissor;

    public Parceiro(Socket conexao, DataInputStream receptor, DataOutputStream transmissor) throws Exception {
        if (conexao == null) throw new Exception ("Conexao ausente");
        if (receptor == null) throw new Exception ("Receptor ausente");
        if (transmissor == null) throw new Exception ("Transmissor ausente");

        this.conexao = conexao;
        this.receptor = receptor;
        this.transmissor = transmissor;
    }

    public void receba(String x) throws Exception {
        try {
            this.transmissor.writeUTF(x);
            this.transmissor.flush();
        }
        catch (IOException erro) {
            throw new Exception ("Erro de transmissao");
        }
    }

    public void enviar(String x) throws Exception {
        try {
            transmissor.writeUTF(x);
            transmissor.flush();
        } catch (IOException erro) {
            throw new Exception("Erro de transmissao: " + erro.getMessage());
        }
    }

    public void adeus () throws Exception {
        try {
            this.transmissor.close();
            this.receptor.close();
            this.conexao.close();
        }
        catch (Exception erro) {
            throw new Exception ("Erro de desconexao");
        }
    }
}
