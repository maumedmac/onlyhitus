package com.lazarowicz.onlyhitus.util;

public class StationInstancer {
    private String text;
    private int image;
    private String url;

    public String getName() {
        return text;
    }

    public void setName(String text) {
        this.text = text;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPlaying() {
        return url + "/currentsong";
    }
}