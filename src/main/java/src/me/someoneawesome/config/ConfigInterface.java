package src.me.someoneawesome.config;

import src.me.someoneawesome.config.interfaces.ChildrenConfigInterface;
import src.me.someoneawesome.config.interfaces.MainConfigurationInterface;
import src.me.someoneawesome.config.interfaces.PlayersConfigInterface;

public class ConfigInterface {
    public static ConfigInterface instance;

    public final MainConfigurationInterface main;
    public final PlayersConfigInterface players;
    public final ChildrenConfigInterface children;

    public ConfigInterface() {
        instance = this;
        ConfigManager manager = ConfigManager.instance;

        main = new MainConfigurationInterface(manager);
        players = new PlayersConfigInterface(manager);
        children = new ChildrenConfigInterface(manager);
    }

}
