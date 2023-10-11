package main.vo;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ImageVo {
    private String value;
    private double confidence;
    private byte[] imageBytes;
}