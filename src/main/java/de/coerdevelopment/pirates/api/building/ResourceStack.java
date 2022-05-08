package de.coerdevelopment.pirates.api.building;

import de.coerdevelopment.pirates.api.ResourceType;

public class ResourceStack {

    public int amount;
    public ResourceType type;

    public ResourceStack(ResourceType type, int amount) {
        this.type = type;
        this.amount = amount;
    }
}
