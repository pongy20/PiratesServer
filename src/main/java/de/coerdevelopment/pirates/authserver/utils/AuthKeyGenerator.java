package de.coerdevelopment.pirates.authserver.utils;

import de.coerdevelopment.standalone.util.MD5;
import de.coerdevelopment.standalone.util.SHA256;

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
        return SHA256.getInstance().stringToHash(accountId + encryptedTime);
    }

    public boolean isAuthKeyCorrect(int accountId, long time, String authKey) {
        String generatedAuthKey = generateAuthKey(accountId, time);
        return generatedAuthKey.equals(authKey);
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
