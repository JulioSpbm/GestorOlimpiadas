package com.laboratorio.olimpiadas.gestaoolimpiadas.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

public class PasswordUtils {

    /**
     * Gera o hash da palavra-passe com SHA-256.
     * A palavra-passe colocada com salt para reforçar a segurança antes de ser encriptada.
     *
     * @param password palavra-passe a ser encriptada.
     * @param salt     salt associado à palavra-passe para aumentar a segurança do hash.
     * @return o hash SHA-256 da palavra-passe concatenada com o salt.
     * @throws NoSuchAlgorithmException se o algoritmo SHA-256 não estiver disponível.
     */
    public static String hashPassword(String password, String salt) throws NoSuchAlgorithmException {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");

            String passwordSalt = password + salt;
            byte[] hashBytes = md.digest(passwordSalt.getBytes("UTF-8"));

            StringBuilder sb = new StringBuilder();
            for (byte b : hashBytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (Exception e) {
            throw new NoSuchAlgorithmException("Erro ao calcular o hash", e);
        }
    }


    private static final int SALT_LENGTH = 16;

    /**
     * Gera um salt aleatório de 16 caracteres.
     *
     * @return O salt gerado como uma string.
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        StringBuilder sb = new StringBuilder();
        for (byte b : salt) {
            sb.append(String.format("%02x", b));
        }
        return sb.toString();
    }
}