package com.maligno.infra;

import com.maligno.client.LatLng;
import com.maligno.client.Rota;
import com.maligno.client.ValidadorDeRota;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

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
                String desc = receptor.readUTF();
                String startLocation = receptor.readUTF();
                String endLocation = receptor.readUTF();
                double distance = Double.parseDouble(receptor.readUTF());
                String time = receptor.readUTF();

                int pointsSize = receptor.readInt();
                List<LatLng> points = new ArrayList<>();

                for (int i = 0; i < pointsSize; i++) {
                    double lat = receptor.readDouble();
                    double lng = receptor.readDouble();
                    points.add(new LatLng(lat, lng));
                }

                Rota route = new Rota(uid, name, desc, startLocation, endLocation, distance, time, points);

                boolean valido = ValidadorDeRota.isValid(route);

                transmissor.writeUTF(valido? "true" : "false");

                System.out.println(route);
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
