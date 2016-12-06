package com.kristi4082.cots;

import java.io.Serializable;

/**
 * Created by Aprilita on 12/6/2016.
 */

public class Animal implements Serializable {
    private String name;
    private String imageUrl;

    public Animal() {
    }

    public Animal(String name, String imageUrl) {
        this.name = name;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
