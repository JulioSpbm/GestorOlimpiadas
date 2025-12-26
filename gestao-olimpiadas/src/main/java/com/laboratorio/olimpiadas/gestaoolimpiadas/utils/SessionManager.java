package com.laboratorio.olimpiadas.gestaoolimpiadas.utils;

public class SessionManager {
    private static SessionManager instance;
    private String clientId;
    private String clientEmail;

    private SessionManager() {}

    public static SessionManager getInstance() {
        if (instance == null) {
            instance = new SessionManager();
        }
        return instance;
    }

    public void setClientData(String clientId, String clientEmail) {
        this.clientId = clientId;
        this.clientEmail = clientEmail;
    }

    public String getClientId() {
        return clientId;
    }

    public String getClientEmail() {
        return clientEmail;
    }
}
