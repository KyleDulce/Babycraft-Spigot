package src.me.someoneawesome.config.interfaces;

import src.me.someoneawesome.config.ConfigManager;

public class MainConfigurationInterface {
    private final String label = "main";
    private final ConfigManager manager;

    public MainConfigurationInterface(ConfigManager manager) {
        this.manager = manager;
    }
}
