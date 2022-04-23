package src.me.someoneawesome.config.interfaces;

import src.me.someoneawesome.config.ConfigManager;

public class PlayersConfigInterface {
    private final String label = "players";
    private final ConfigManager manager;

    public PlayersConfigInterface(ConfigManager manager) {
        this.manager = manager;
    }
}
