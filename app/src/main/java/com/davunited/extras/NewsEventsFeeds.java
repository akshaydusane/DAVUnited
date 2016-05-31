package com.davunited.extras;

import java.util.Date;

/**
 * Created by Ela on 23-05-2016.
 */
public class NewsEventsFeeds {

    private int event_id;
    private String title;
    private String description;
    private String date;

    public NewsEventsFeeds(){

    }

    public NewsEventsFeeds(String title, String description, String date){
        this.title = title;
        this.description = description;
        this.date = date;
    }


    public void setEvent_id(int event_id) {
        this.event_id = event_id;
    }

    public int getEvent_id() {
        return event_id;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDate() {
        return date;
    }

    @Override
    public String toString(){
        return  "Title: "+title+
                "Description: "+description+
                "Date: "+date;
    }
}
