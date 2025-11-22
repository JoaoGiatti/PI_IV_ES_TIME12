package com.maligno.server;

import com.maligno.client.Parceiro;
import com.maligno.client.PedidoDeRota;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;

public class SupervisoraDeConexao extends Thread
{
    private Parceiro            usuario;
    private Socket              conexao;
    private ArrayList<Parceiro> usuarios;

    public SupervisoraDeConexao(Socket conexao, ArrayList<Parceiro> usuarios) throws Exception {
        if (conexao==null) throw new Exception("Conexao ausente");
        if (usuarios==null) throw new Exception("Usuarios ausentes");

        this.conexao  = conexao;
        this.usuarios = usuarios;
    }

    public void run () {
        DataInputStream receptor = null;
        try {
            receptor = new DataInputStream(conexao.getInputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        DataOutputStream transmissor = null;
        try {
            transmissor = new DataOutputStream(conexao.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        try {
            this.usuario = new Parceiro (this.conexao, receptor, transmissor);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        try {
            synchronized (this.usuarios) {
                this.usuarios.add (this.usuario);
            }

            for(;;) {
                String uid = receptor.readUTF();
                String name = receptor.readUTF();
                String description = receptor.readUTF();
                String startLocation = receptor.readUTF();
                String endLocation = receptor.readUTF();
                double distance = Double.parseDouble(receptor.readUTF());
                String recordTime = receptor.readUTF();

                System.out.println("\n========== 📥 ROTA RECEBIDA DO CLIENTE ==========");
                System.out.printf("UID ...............: %s%n", uid);
                System.out.printf("Nome ..............: %s%n", name);
                System.out.printf("Descrição .........: %s%n", description);
                System.out.printf("Origem ............: %s%n", startLocation);
                System.out.printf("Destino ...........: %s%n", endLocation);
                System.out.printf("Distância (km) ....: %.2f%n", distance);
                System.out.printf("Tempo de registro .: %s%n", recordTime);
                System.out.println("=================================================\n");

                PedidoDeRota pedido = new PedidoDeRota(uid, name, description, startLocation, endLocation, distance, recordTime);

                transmissor.writeUTF(pedido.validar() ? "true" : "false");
                transmissor.flush();

                System.out.println("📤 Resposta enviada");
            }
        }

        catch (Exception erro) {
            try {
                transmissor.close ();
                receptor.close ();
            }
            catch (Exception falha) {} // so tentando fechar antes de acabar a thread
        }
    }
}
