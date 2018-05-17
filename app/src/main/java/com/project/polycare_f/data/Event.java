package com.project.polycare_f.data;

import java.util.List;

public class Event {
    private String title, location;
    private String category;
    private String description;
    private String reporter;
    private String importance;
    private String state;
    private String date;
    private String number,latitude, lontitude;
    private int id;


    public Event(int id, String title, String category, String description, String reporter, String importance, String state, String date, String number,
                 String latitude, String lontitude, String location) {
        this.title = title;
        this.category = category;
        this.description = description;
        this.reporter = reporter;
        this.importance = importance;
        this.state = state;
        this.date = date;
        this.number = number;
        this.id = id;
        this.latitude = latitude;
        this.lontitude = lontitude;
        this.location = location;
    }

    public String getTitle() {
        return title;
    }

    public String getCategory() {
        return category;
    }

    public String getDescription() {
        return description;
    }

    public String getReporter() {
        return reporter;
    }

    public String getImportance() {
        return importance;
    }

    public String getState() {
        return state;
    }

    public String getDate() {
        return date;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setReporter(String reporter) {
        this.reporter = reporter;
    }

    public void setImportance(String importance) {
        this.importance = importance;
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLontitude() {
        return lontitude;
    }

    public void setLontitude(String lontitude) {
        this.lontitude = lontitude;
    }

    @Override
    public boolean equals(Object obj) {
        if(this == obj){
            return true;
        }

        if(obj==null){
            return false;
        }

        if(obj instanceof Event){
            Event e = (Event) obj;
            if(e.id==id){
                return true;
            }
            return false;
        }

        return false;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }
}
