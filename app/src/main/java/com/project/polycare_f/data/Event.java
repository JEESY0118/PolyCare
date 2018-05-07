package com.project.polycare_f.data;

public class Event {
    private String title;
    private String category;
    private String description;
    private String reporter;
    private String importance;
    private String state;
    private String date;
    private String number;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    private int id;

    public Event(int id, String title, String category, String description, String reporter, String importance, String state, String date, String number) {
        this.title = title;
        this.category = category;
        this.description = description;
        this.reporter = reporter;
        this.importance = importance;
        this.state = state;
        this.date = date;
        this.number = number;
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
}
