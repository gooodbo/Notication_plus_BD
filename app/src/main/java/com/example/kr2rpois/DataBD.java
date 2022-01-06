package com.example.kr2rpois;

public class DataBD {
    private String heading;
    private String text;
    private int mins;
    private int ours;
    private int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMins() {
        return mins;
    }

    public void setMins(int mins) {
        this.mins = mins;
    }

    public int getOurs() {
        return ours;
    }

    public void setOurs(int ours) {
        this.ours = ours;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }


    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}
