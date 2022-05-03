package de.coerdevelopment.pirates.authserver.utils;

import de.coerdevelopment.standalone.util.MD5;

public class AuthKeyGenerator {
    
    private static AuthKeyGenerator instance;
    
    public static AuthKeyGenerator getInstance() {
        if (instance == null) {
            instance = new AuthKeyGenerator();
        }
        return instance;
    }

    private final char[] cryptMatrix = new char[]{'F','5','c','9','y','!','W','z','8','?'};
    
    private AuthKeyGenerator() {
        //Private Constructor used in singleton context
    }

    public String generateAuthKey(int accountId, long time) {
        String encryptedTime = encryptTime(time);
        return MD5.getInstance().md5(accountId + encryptedTime);
    }

    public String encryptTime(long time) {
        String timeS = time + "";
        String output = "";
        for (char c : timeS.toCharArray()) {
            output += cryptMatrix[Integer.parseInt(String.valueOf(c))];
        }
        return output;
    }

}
