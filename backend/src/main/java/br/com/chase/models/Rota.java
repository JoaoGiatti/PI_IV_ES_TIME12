package br.com.chase.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Document(collection = "routes")
public class Rota {
    @Id
    private String rid;
    private String uid;
    private String name;
    private String description;
    private String startLocation;
    private String endLocation;
    private List<LatLng> points;
    private double distance;
    private String recordTime;
    private List<RotaRecord> top3;
    private int competitors;
    private double bestAverageSpeed;
    private double estimatedCalories;
    private boolean isPublic;
    private Date createdAt;

}
