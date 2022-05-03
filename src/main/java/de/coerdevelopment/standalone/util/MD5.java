package de.coerdevelopment.standalone.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5 {

    private static MD5 instance;

    public static MD5 getInstance() {
        if (instance == null) {
            instance = new MD5();
        }
        return instance;
    }

    private MD5() {

    }

    /**
     * Hashes a string
     */
    public String md5(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] array = md.digest(password.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return null;
    }
}
