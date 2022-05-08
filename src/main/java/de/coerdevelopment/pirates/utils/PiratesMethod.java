package de.coerdevelopment.pirates.utils;

public enum PiratesMethod {

    AUTH_CLIENT("AUTH_CLIENT"),
    REGISTER_CLIENT("REGISTER_ACCOUNT"),
    LOGIN_PLAYER("LOGIN_PLAYER"),
    NOT_AUTHORIZED("NOT_AUTHORIZED");

    String methodId;

    PiratesMethod(String methodId) {
        this.methodId = methodId;
    }

    public String getMethodId() {
        return methodId;
    }

}
