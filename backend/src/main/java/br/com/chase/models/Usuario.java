package br.com.chase.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Document(collection = "usuarios")
public class Usuario {
    @Id
    private String uid;
    private String email;
    private String displayName;
    private String photoUrl;
    private String bio;
    private Date createdAt;
    private List<String> medals;
    private double totalCalories;
    private double totalDistance;
    private double totalTime;

    public Usuario() {}
}
