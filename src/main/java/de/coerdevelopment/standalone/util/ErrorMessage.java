package de.coerdevelopment.standalone.util;

public enum ErrorMessage {

    DATAPACKAGE_DATA_NULL("Unexpected datapackage arrived with null data."),
    DATAPACKAGE_UNEXPECTED_DATA("Datapackage with unexpected data arrived"),
    LOBBY_UNVALID_CODE("The provided lobbycode is not valid."),
    LOBBY_CANT_CREATE_CODE("Unable to create a new lobbycode.");

    String description;

    ErrorMessage(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

}
