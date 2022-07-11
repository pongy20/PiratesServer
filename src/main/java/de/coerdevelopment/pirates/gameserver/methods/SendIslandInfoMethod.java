package de.coerdevelopment.pirates.gameserver.methods;

import de.coerdevelopment.pirates.api.Island;
import de.coerdevelopment.pirates.api.Player;
import de.coerdevelopment.pirates.api.building.ProductionBuilding;
import de.coerdevelopment.pirates.api.repository.gameserver.IslandRepository;
import de.coerdevelopment.pirates.api.repository.gameserver.PlayerRepository;
import de.coerdevelopment.pirates.api.service.gameserver.IslandService;
import de.coerdevelopment.pirates.utils.PiratesMethod;
import de.coerdevelopment.standalone.net.Datapackage;
import de.coerdevelopment.standalone.net.tcp.TcpMethod;
import de.coerdevelopment.standalone.net.tcp.TcpThread;

public class SendIslandInfoMethod extends TcpMethod {

    public SendIslandInfoMethod() {
        super(PiratesMethod.SEND_ISLAND_INFO.getMethodId());
    }

    @Override
    public void onMethod(Datapackage incomingPackage, TcpThread clientThread) {
        Player player = PlayerRepository.getInstance().getPlayerByAccountId(clientThread.accountId);
        Island island = IslandRepository.getInstance().getIslandsByPlayer(player.playerId).get(0);
        Island loadedIsland = IslandService.getInstance().getIslandById(island.islandId);
        loadedIsland.getProductionBuildings().forEach(ProductionBuilding::setCurrentResourceStorage);
        Datapackage datapackage = new Datapackage(methodID, loadedIsland);
        clientThread.send(datapackage);
    }
}
