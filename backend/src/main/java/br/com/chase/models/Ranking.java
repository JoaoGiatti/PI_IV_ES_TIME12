package br.com.chase.models;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Getter
@Setter
public class Ranking {
    @Id
    private String uid;
    private String userName;
    private String photoUrl;
    private String totalTime;
    private double averageSpeed;

    public Ranking() {}

    public Ranking(String uid, String userName, String photoUrl, String totalTime, double averageSpeed) {
        this.uid = uid;
        this.userName = userName;
        this.photoUrl = photoUrl;
        this.totalTime = totalTime;
        this.averageSpeed = averageSpeed;
    }
}
