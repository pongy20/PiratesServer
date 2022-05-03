package de.coerdevelopment.pirates.utils;

public enum PiratesMethod {

    AUTH_CLIENT("auth_client");

    String methodId;

    PiratesMethod(String methodId) {
        this.methodId = methodId;
    }

    public String getMethodId() {
        return methodId;
    }

}
