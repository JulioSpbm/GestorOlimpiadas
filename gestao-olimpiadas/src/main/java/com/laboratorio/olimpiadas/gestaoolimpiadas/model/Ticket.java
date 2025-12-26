package com.laboratorio.olimpiadas.gestaoolimpiadas.model;

import java.util.Date;

public class Ticket {
    private String id;
    private Date startDate;
    private Date endDate;
    private String location;
    private String seat;
    private String qrCodeBase64;
    private String GameId;

    public Ticket(String id, Date startDate, Date endDate, String location, String seat, String qrCodeBase64) {
        this.id = id;
        this.startDate = startDate;
        this.endDate = endDate;
        this.location = location;
        this.seat = seat;
        this.qrCodeBase64 = qrCodeBase64;
    }

    public Ticket() {

    }

    public void setId(String id) {
        this.id = id;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setSeat(String seat) {
        this.seat = seat;
    }

    public void setQrCodeBase64(String qrCodeBase64) {
        this.qrCodeBase64 = qrCodeBase64;
    }

    public String getId() {
        return id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public String getLocation() {
        return location;
    }

    public String getSeat() {
        return seat;
    }

    public String getQrCodeBase64() {
        return qrCodeBase64;
    }

    public String getGameId() {
        return GameId;
    }
    public void setGameId(String GameId){
        this.GameId = GameId;
    }
}
