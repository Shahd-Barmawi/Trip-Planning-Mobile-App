package com.example.first;

public class TripTask {

    private String title;
    private String city;
    private String date;
    private String timeOfDay;
    private boolean important;
    private boolean done;
    private String type;

    public TripTask(String title, String city, String date, String timeOfDay,
                    boolean important, boolean done, String type) {
        this.title = title;
        this.city = city;
        this.date = date;
        this.timeOfDay = timeOfDay;
        this.important = important;
        this.done = done;
        this.type = type;
    }

    public String getTitle() {
        return title;
    }
    public void setTitle(String title) {
        this.title = title;
    }

    public String getCity() {
        return city;
    }
    public void setCity(String city) {
        this.city = city;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getTimeOfDay() {
        return timeOfDay;
    }
    public void setTimeOfDay(String timeOfDay) {
        this.timeOfDay = timeOfDay;
    }

    public boolean isImportant() {
        return important;
    }
    public void setImportant(boolean important) {
        this.important = important;
    }

    public boolean isDone() {
        return done;
    }
    public void setDone(boolean done) {
        this.done = done;
    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
}
