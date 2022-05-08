package de.coerdevelopment.pirates.api.building;

import de.coerdevelopment.pirates.api.ResourceType;

public class ResourceCost {

    public ResourceStack wood;
    public ResourceStack ore;
    public ResourceStack fabric;
    public ResourceStack ducat;
    public int diamonds;

    public ResourceCost(ResourceStack wood, ResourceStack ore, ResourceStack fabric, ResourceStack ducat) {
        this(wood, ore, fabric, ducat, 0);
    }

    public ResourceCost(ResourceStack wood, ResourceStack ore, ResourceStack fabric, ResourceStack ducat, int diamonds) {
        this.wood = wood;
        this.ore = ore;
        this.fabric = fabric;
        this.ducat = ducat;
        this.diamonds = diamonds;
    }
    public ResourceCost(int wood, int ore, int fabric, int ducat, int diamonds) {
        this(new ResourceStack(ResourceType.WOOD, wood), new ResourceStack(ResourceType.ORE, ore), new ResourceStack(ResourceType.FABRIC, fabric), new ResourceStack(ResourceType.DUCAT, ducat), diamonds);
    }
    public ResourceCost(int wood, int ore, int fabric, int ducat) {
        this(new ResourceStack(ResourceType.WOOD, wood), new ResourceStack(ResourceType.ORE, ore), new ResourceStack(ResourceType.FABRIC, fabric), new ResourceStack(ResourceType.DUCAT, ducat));
    }
}
