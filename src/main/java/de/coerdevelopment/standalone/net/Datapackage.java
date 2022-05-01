package de.coerdevelopment.standalone.net;

import de.coerdevelopment.standalone.json.JsonConverter;
import de.coerdevelopment.standalone.util.ErrorMessage;

import java.io.Serializable;

public class Datapackage implements Serializable {
    private String methodID;
    private String data;

    public Datapackage(String methodID, String json) {
        this.methodID = methodID;
        this.data = json;
    }
    public Datapackage(ErrorMessage errorMessage) {
        this(errorMessage.toString(), null);
    }
    public Datapackage(String methodID, Object data) {
        this(methodID, JsonConverter.getInstance().convertObjectToJson(data));
    }
    public Datapackage(String methodID) {
        this(methodID, null);
    }

    public String getData() {
        return data;
    }

    public String getMethodID() {
        return methodID;
    }
}
