package de.coerdevelopment.pirates.api;

import de.coerdevelopment.pirates.gameserver.PiratesGameServer;

public class Player {

    public int playerId;
    public int accountId;
    public GameWorld gameWorld;
    public String username;
    public int diamonds;

    public Player(int playerId, int accountId, String username, int diamonds) {
        this.playerId = playerId;
        this.accountId = accountId;
        this.gameWorld = PiratesGameServer.instance.gameWorld;
        this.username = username;
        this.diamonds = diamonds;
    }

    public Player(int accountId, String username, int diamonds) {
        this(-1, accountId,username,diamonds);
    }
}
