package com.davunited.extras;

/**
 * Created by Ela on 25-05-2016.
 */
public class InstituteFeeds {

    String ins_name;
    String ins_state;
    String ins_website;
    Double lat;
    Double log;

    public InstituteFeeds(){

    }

    public InstituteFeeds(String ins_name, String ins_state, String ins_website,Double lat,Double log){
        this.ins_name = ins_name;
        this.ins_state = ins_state;
        this.ins_website = ins_website;
        this.lat = lat;
        this.log = log;
    }

    public void setIns_name(String ins_name) {
        this.ins_name = ins_name;
    }

    public String getIns_name() {
        return ins_name;
    }

    public void setIns_state(String ins_state) {
        this.ins_state = ins_state;
    }

    public String getIns_state() {
        return ins_state;
    }

    public void setIns_website(String ins_website) {
        this.ins_website = ins_website;
    }

    public String getIns_website() {
        return ins_website;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLat() {
        return lat;
    }

    public void setLog(Double log) {
        this.log = log;
    }

    public Double getLog() {
        return log;
    }
}
