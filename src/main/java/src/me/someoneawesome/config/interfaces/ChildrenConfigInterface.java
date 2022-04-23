package src.me.someoneawesome.config.interfaces;

import src.me.someoneawesome.config.ConfigManager;

public class ChildrenConfigInterface {
    private final String label = "children";
    private final ConfigManager manager;

    public ChildrenConfigInterface(ConfigManager manager) {
        this.manager = manager;
    }
}
