package de.coerdevelopment.pirates.api;

public class Player {

    public int playerId;
    public int accountId;
    public GameWorld gameWorld;
    public String username;
    public int diamonds;

    public Player(int playerId, int accountId, GameWorld gameWorld, String username, int diamonds) {
        this.playerId = playerId;
        this.accountId = accountId;
        this.gameWorld = gameWorld;
        this.username = username;
        this.diamonds = diamonds;
    }

    public Player(int accountId, GameWorld gameWorld, String username, int diamonds) {
        this(-1, accountId,gameWorld,username,diamonds);
    }
}
