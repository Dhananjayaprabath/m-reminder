package com.example.mynotes;

public class Model {

        String title;
        String description;
        String id;
        String time;
        String date;
        String day;
        String colours;
        String noti;



    public Model(String id, String title, String description, String time, String date, String day, String colours, String noti) {

        this.id=id;
        this.title = title;
        this.description = description;
        this.time = time;
        this.date = date;
        this.day=day;
        this.colours=colours;
        this.noti = noti;


    }



    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }


    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getColours() {
        return colours;
    }

    public void setColours(String colours) {
        this.colours = colours;
    }

    public String getNoti() {
        return noti;
    }

    public void setNoti(String noti) {
        this.noti = noti;
    }


}
