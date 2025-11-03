package com.maligno.statement;

public class ComunicadoDeResultado extends Comunicado {
    private double valorResultante;

    public ComunicadoDeResultado(double valorResultante) {
        this.valorResultante = valorResultante;
    }

    public double getValorResultante () {
        return this.valorResultante;
    }
    
    public String toString () {
		return ("" + this.valorResultante);
	}
}
