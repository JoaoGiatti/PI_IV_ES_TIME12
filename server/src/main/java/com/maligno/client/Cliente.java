package com.maligno.client;

import com.maligno.statement.*;
import com.maligno.utils.Teclado;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class Cliente {
	public static final String HOST_PADRAO  = "localhost";
	public static final int PORTA_PADRAO = 3000;

	public static void main (String[] args) {
        Socket conexao;
		ObjectOutputStream transmissor;
		ObjectInputStream receptor;
		Parceiro servidor;
		TratadoraDeComunicadoDeDesligamento tratadoraDeComunicadoDeDesligamento;

        try {
			conexao = new Socket (Cliente.HOST_PADRAO, Cliente.PORTA_PADRAO);
			transmissor = new ObjectOutputStream(conexao.getOutputStream());
			receptor = new ObjectInputStream(conexao.getInputStream());
			servidor = new Parceiro(conexao, receptor, transmissor);
			tratadoraDeComunicadoDeDesligamento = new TratadoraDeComunicadoDeDesligamento(servidor);
        } catch (Exception erro) {
            System.err.println ("Indique o servidor e a porta corretos!\n");
            return;
        }
		
        tratadoraDeComunicadoDeDesligamento.start();

        char opcao=' ';
        do {
            System.out.print ("Sua opcao (+, -, *, /, =, [T]erminar)? ");

            try {
				opcao = Character.toUpperCase(Teclado.getUmChar());
		    } catch (Exception erro) {
				System.err.println ("Opcao invalida!\n");
				continue;
			}

			if ("+-*/=T".indexOf(opcao)==-1) {
				System.err.println ("Opcao invalida!\n");
				continue;
			}

			try {
				double valor;
				if ("+-*/".indexOf(opcao)!=-1) {
					System.out.print ("Valor? ");
					try {
						valor = Teclado.getUmDouble();
						System.out.println();
						
						if (opcao=='/' && valor==0) {
							System.err.println ("Valor invalido!\n");
							continue;
						}
					} catch (Exception erro) {
						System.err.println ("Valor invalido!\n");
						continue;
					}

					servidor.receba (new PedidoDeOperacao(opcao, valor));
				}
				else if (opcao == '=') {
					servidor.receba (new PedidoDeResultado());
					Comunicado comunicado;

					do {
						comunicado = (Comunicado)servidor.espie();
					} while (!(comunicado instanceof ComunicadoDeResultado));
					ComunicadoDeResultado comunicadoDeResultado = (ComunicadoDeResultado)servidor.envie ();
					System.out.println ("Resultado atual: "+ comunicadoDeResultado.getValorResultante()+"\n");
				}
			} catch (Exception erro) {
				System.err.println ("Erro de comunicacao com o servidor;");
				System.err.println ("Tente novamente!");
				System.err.println ("Caso o erro persista, termine o programa");
				System.err.println ("e volte a tentar mais tarde!\n");
			}
        }

        while (opcao != 'T');

		try {
			servidor.receba(new PedidoParaSair());
		} catch (Exception erro) {}
		
		System.out.println ("Obrigado por usar este programa!");
		System.exit(0);
	}
}
