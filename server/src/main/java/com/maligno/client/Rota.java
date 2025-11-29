package com.maligno.client;

import java.util.ArrayList;
import java.util.List;

public class Rota {

    private String uid;
    private String name;
    private String description;
    private String startLocation;
    private String endLocation;
    private double distance;     // em metros
    private String recordTime;   // "HH:mm:ss"
    private List<LatLng> points;

    // Construtor de rota. Aqui não lançamos Exception porque o servidor é responsável
    // por validar se a rota é válida ou não. Valores nulos, incorretos ou tentativas
    // de cheating são tratados pelo ValidadorDeRota, que devolve uma resposta adequada
    // ao cliente. Se lançássemos exceções para cada rota inválida (null ou valores
    // inconsistentes), o servidor poderia parar sua execução e deixaria de cumprir
    // o papel de validar e responder corretamente.
    public Rota(String uid,
                String name,
                String description,
                String startLocation,
                String endLocation,
                double distance,
                String recordTime,
                List<LatLng> points) {
        this.uid = uid;
        this.name = name;
        this.description = description;
        this.startLocation = startLocation;
        this.endLocation = endLocation;
        this.distance = distance;
        this.recordTime = recordTime;
        this.points = (points != null) ? points : new ArrayList<LatLng>();
    }

    // -----------------------------------------------------------------------------------------------------------------

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStartLocation() {
        return startLocation;
    }

    public void setStartLocation(String startLocation) {
        this.startLocation = startLocation;
    }

    public String getEndLocation() {
        return endLocation;
    }

    public void setEndLocation(String endLocation) {
        this.endLocation = endLocation;
    }

    public double getDistance() {
        return distance;
    }

    public void setDistance(double distance) {
        this.distance = distance;
    }

    public String getRecordTime() {
        return recordTime;
    }

    public void setRecordTime(String recordTime) {
        this.recordTime = recordTime;
    }

    public List<LatLng> getPoints() {
        return points;
    }

    public void setPoints(List<LatLng> points) {
        this.points = points;
    }

    // -----------------------------------------------------------------------------------------------------------------

    @Override
    public String toString() {

        return String.format(
                "\n========== 📥 ROTA RECEBIDA DO CLIENTE ==========\n" +
                        "UID ...............: %s%n" +
                        "Nome ..............: %s%n" +
                        "Descrição .........: %s%n" +
                        "Origem ............: %s%n" +
                        "Destino ...........: %s%n" +
                        "Distância (km) ....: %.2f%n" +
                        "Tempo de registro .: %s%n" +
                "=================================================\n",
                uid,
                name,
                description,
                startLocation,
                endLocation,
                distance,
                recordTime
        );
    }

    @Override
    public boolean equals(Object obj) {
        if(obj == this) return true;
        if(obj == null) return false;
        if(obj.getClass() != this.getClass()) return false;

        Rota other = (Rota) obj;

        if(!this.uid.equals(other.uid)) return false;
        if(!this.name.equals(other.name)) return false;
        if(!this.description.equals(other.description)) return false;
        if(!this.startLocation.equals(other.startLocation)) return false;
        if(!this.endLocation.equals(other.endLocation)) return false;
        if(this.distance != other.distance) return false;
        if(!this.recordTime.equals(other.recordTime)) return false;

       for(int i = 0; i < this.points.size(); ++i) {
           if(!this.points.get(i).equals(other.points.get(i))) return false;
       }

        return true;
    }

    @Override
    public int hashCode() {
        int ret = 1;

        ret = ret * 5 + this.uid.hashCode();
        ret = ret * 5 + this.name.hashCode();
        ret = ret * 5 + this.description.hashCode();
        ret = ret * 5 + this.startLocation.hashCode();
        ret = ret * 5 + this.endLocation.hashCode();
        ret = ret * 5 + ((Double)this.distance).hashCode();
        ret = ret * 5 + this.recordTime.hashCode();

        for(LatLng point : this.points){
            ret = ret * 5 + point.hashCode();
        }

        if(ret < 0) ret = -ret;
        return ret;
    }

    // -----------------------------------------------------------------------------------------------------------------

    // Construtor de cópia de rota...
    public Rota(Rota rota) throws Exception {
        if (rota == null) throw new Exception("Objeto Nulo no construtor de Copia!");

        this.uid = rota.uid;
        this.name = rota.name;
        this.description = rota.description;
        this.startLocation = rota.startLocation;
        this.endLocation = rota.endLocation;
        this.distance = rota.distance;
        this.recordTime = rota.recordTime;

        this.points = new ArrayList<>();
        for (LatLng p : rota.points) {
            this.points.add((LatLng) p.clone());
        }
    }

    @Override
    public Object clone() {
        try {
            return new Rota(this);
        } catch (Exception erro) {} // sei que o cc só da erro quando recebe null, e this nunca é null

        return null;
    }
}
