package de.coerdevelopment.pirates.api.service.gameserver;

import de.coerdevelopment.pirates.api.Player;
import de.coerdevelopment.pirates.api.repository.gameserver.IslandRepository;
import de.coerdevelopment.pirates.api.repository.gameserver.PlayerLoginRepository;
import de.coerdevelopment.pirates.api.repository.gameserver.PlayerRankRepository;
import de.coerdevelopment.pirates.api.repository.gameserver.PlayerRepository;

import java.util.Random;

public class PlayerService {

    private static PlayerService instance;

    public static PlayerService getInstance() {
        if (instance == null) {
            instance = new PlayerService();
        }
        return instance;
    }

    private PlayerRepository playerRepository;
    private PlayerRankRepository playerRankRepository;
    private PlayerLoginRepository playerLoginRepository;

    private PlayerService() {
        playerRepository = PlayerRepository.getInstance();
        playerLoginRepository = PlayerLoginRepository.getInstance();
        playerRankRepository = PlayerRankRepository.getInstance();
    }

    public boolean hasPlayerPlayedBefore(int accountId) {
        return playerRepository.getPlayerByAccountId(accountId) != null;
    }

    /**
     * Inits a player and returns the created player object
     * Steps:
     * 1. Init Player
     * 2. Init Start island
     */
    public Player initPlayer(int accountId) {
        playerRepository.insertPlayer(accountId, generateRandomUniquePlayername());
        Player createdPlayer = playerRepository.getPlayerByAccountId(accountId);
        IslandRepository.getInstance().createIsland(createdPlayer.playerId, generateRandomUniqueIslandname());
        return createdPlayer;
    }

    public void loadPlayer() {

    }

    private String generateRandomUniquePlayername() {
        Random random = new Random();
        String name;
        do {
            name = "Player#" + (random.nextInt(9000) + 1000);
        } while (playerRepository.getPlayerByUsername(name) != null);
        return name;
    }

    private String generateRandomUniqueIslandname() {
        Random random = new Random();
        String name;
        do {
            name = "Island#" + (random.nextInt(9000) + 1000);
        } while (IslandRepository.getInstance().doesNameExists(name));
        return name;
    }

}
