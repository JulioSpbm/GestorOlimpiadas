package com.laboratorio.olimpiadas.gestaoolimpiadas.model;

import com.sun.java.accessibility.util.EventID;

import java.util.Date;

public class Prova {
    private String id;
    private int eventID;
    private Date startdate;
    private Date enddate;
    private String local;
    private String sport;
    private int capacity;

    public Prova(String id, int eventID, Date startdate, Date enddate, String local, String sport, int capacity) {
        this.id = id;
        this.eventID = eventID;
        this.startdate = startdate;
        this.enddate = enddate;
        this.local = local;
        this.sport = sport;
        this.capacity = capacity;
    }

    public Prova() {
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }

    public int getEventID() {
        return eventID;
    }

    // Getters e Setters
    public Date getStartdate() {
        return startdate;
    }

    public void setStartdate(Date startdate) {
        this.startdate = startdate;
    }

    public Date getEnddate() {
        return enddate;
    }

    public void setEnddate(Date enddate) {
        this.enddate = enddate;
    }

    public String getLocal() {
        return local;
    }

    public void setLocal(String local) {
        this.local = local;
    }

    public String getSport() {
        return sport;
    }

    public void setSport(String sport) {
        this.sport = sport;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}