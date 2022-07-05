package de.coerdevelopment.pirates.api.service.gameserver;

import de.coerdevelopment.pirates.api.repository.gameserver.PlayerLoginRepository;
import de.coerdevelopment.pirates.api.repository.gameserver.PlayerRankRepository;
import de.coerdevelopment.pirates.api.repository.gameserver.PlayerRepository;

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

}
