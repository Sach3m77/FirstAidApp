package com.example.firstaidapp.models;

import android.content.Context;

import java.io.Serializable;

public class Ailment implements Serializable {

    private Context mcontext;
    private int id;
    private String image;
    private String title;
    private String info;
    private String symptoms;
    private String rescueActivities;
    private String rescueActivitiesImage;

    public Ailment(int id, String image, String title, String info, String symptoms, String rescueActivities, String rescueActivitiesImage) {
        this.id = id;
        this.image = image;
        this.title = title;
        this.info = info;
        this.symptoms = symptoms;
        this.rescueActivities = rescueActivities;
        this.rescueActivitiesImage = rescueActivitiesImage;
    }

    public String getImage() {
        return image;
    }

    public String getTitle() {
        return title;
    }

    public String getInfo() {
        return info;
    }

    public String getRescueActivitiesImage() {
        return rescueActivitiesImage;
    }

    public String[] getSymptomsArray() {
        String[] symptomsArray = symptoms.split(";");
        return symptomsArray;
    }

    public String[] getRescueActivitiesArray() {
        String[] rescueActivitiesArray = rescueActivities.split(";");
        return rescueActivitiesArray;
    }

}
