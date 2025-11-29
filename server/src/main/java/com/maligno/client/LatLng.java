package com.maligno.client;

public class LatLng {

    private double latitude;
    private double longitude;

    // Construtor de latlng. Aqui não lançamos Exception porque o servidor é responsável
    // por validar se a rota é válida ou não. valores incorretos são tratados pelo ValidadorDeRota,
    // que devolve uma resposta adequada ao cliente. Se lançássemos exceções para cada rota inválida
    // (valores inconsistentes), o servidor poderia parar sua execução e deixaria de cumprir
    // o papel de validar e responder corretamente.
    public LatLng(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    // -----------------------------------------------------------------------------------------------------------------

    public double getLatitude() {
        return this.latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return this.longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {
        return this.latitude + ", " + this.longitude;
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(obj == null) return false;
        if(obj.getClass() != this.getClass()) return false;

        LatLng other = (LatLng) obj;

        return this.latitude == other.latitude && this.longitude == other.longitude;
    }

    @Override
    public int hashCode() {
        int ret = 1;

        ret = ret * 5 + ((Double)this.latitude).hashCode();
        ret = ret * 5 + ((Double)this.longitude).hashCode();

        if(ret < 0) ret = -ret;
        return ret;
    }

    // -----------------------------------------------------------------------------------------------------------------

    // Construtor de cópia de latlng...
    public LatLng(LatLng latLng) throws Exception {
        if (latLng == null) throw new Exception("Objeto Nulo no construtor de Copia!");

        this.latitude = latLng.latitude;
        this.longitude = latLng.longitude;
    }

    @Override
    public Object clone() {
        try {
            return new LatLng(this);
        } catch (Exception erro) {} // sei que o cc só da erro quando recebe null, e this nunca é null

        return null;
    }
}
