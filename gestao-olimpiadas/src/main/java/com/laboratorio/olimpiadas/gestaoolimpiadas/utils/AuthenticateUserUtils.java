package com.laboratorio.olimpiadas.gestaoolimpiadas.utils;

public class AuthenticateUserUtils {
    private static String numMecanografico;

    public static void setNumMecanografico(String numMecanografico) {
        AuthenticateUserUtils.numMecanografico = numMecanografico;
    }

    public static String getNumMecanografico() {
        return numMecanografico;
    }
}
