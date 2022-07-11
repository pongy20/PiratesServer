package de.coerdevelopment.pirates.gameserver.methods;

import com.google.gson.Gson;
import de.coerdevelopment.pirates.api.Island;
import de.coerdevelopment.pirates.api.building.BuildingType;
import de.coerdevelopment.pirates.api.building.ProductionBuilding;
import de.coerdevelopment.pirates.api.service.gameserver.IslandService;
import de.coerdevelopment.pirates.utils.PiratesMethod;
import de.coerdevelopment.standalone.net.Datapackage;
import de.coerdevelopment.standalone.net.tcp.TcpMethod;
import de.coerdevelopment.standalone.net.tcp.TcpThread;
import de.coerdevelopment.standalone.util.DebugMessage;

public class CollectResourceMethod extends TcpMethod {

    public CollectResourceMethod() {
        super(PiratesMethod.COLLECT_RESOURCE.getMethodId());
    }

    @Override
    public void onMethod(Datapackage incomingPackage, TcpThread clientThread) {
        String rawData = incomingPackage.getData();
        String[] data;
        Gson gson = new Gson();
        try {
            data = gson.fromJson(rawData, String[].class);
        } catch (Exception e) {
            sendErrorPackage(clientThread);
            return;
        }
        if (data.length != 2) {
            sendErrorPackage(clientThread);
            return;
        }
        int islandId = 0;
        BuildingType type;
        try {
            islandId = Integer.parseInt(data[0]);
            type = BuildingType.valueOf(data[1]);
            if (!type.equals(BuildingType.LUMBERJACK) && !type.equals(BuildingType.MINE) && !type.equals(BuildingType.FARM)) {
                sendErrorPackage(clientThread);
                return;
            }
        } catch (Exception e) {
            sendErrorPackage(clientThread);
            return;
        }
        Island island = IslandService.getInstance().getIslandById(islandId);
        if (island == null || island.owner.accountId != clientThread.accountId) {
            sendErrorPackage(clientThread);
            return;
        }
        int collected = IslandService.getInstance().collectResources(island, type);
        ProductionBuilding pb = (ProductionBuilding) island.getBuildingByType(type);
        clientThread.send(new Datapackage(PiratesMethod.UPDATE_WORLD.getMethodId(), island));
    }

    private void sendErrorPackage(TcpThread clientThread) {
        clientThread.send(new Datapackage(methodID, 0));
    }

}
