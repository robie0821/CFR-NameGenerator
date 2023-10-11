package main.vo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class FaceVo {
    private String gender;
    private double genderConfidence;
    private String age;
    private double ageConfidence;
    private String emotion;
    private double emotionConfidence;
}