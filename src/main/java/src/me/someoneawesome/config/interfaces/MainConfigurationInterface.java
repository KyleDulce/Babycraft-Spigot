package src.me.someoneawesome.config.interfaces;

import src.me.someoneawesome.config.ConfigManager;
import src.me.someoneawesome.config.ConfigPath;

public class MainConfigurationInterface {
    private final String label = "main";
    private final ConfigManager manager;

    public MainConfigurationInterface(ConfigManager manager) {
        this.manager = manager;
    }

    public long getRequestTimeout() {
        return manager.getConfigObject(label).getLong(ConfigPath.MAIN_REQUEST_TIMEOUT, 60l) * 20;
    }

    public boolean getAllowMultiMarriage() {
        return manager.getConfigObject(label).getBoolean(ConfigPath.MAIN_MULTI_MARRIAGE, true);
    }

    public boolean getChooseBabyGender() {
        return manager.getConfigObject(label).getBoolean(ConfigPath.MAIN_CHOOSE_BABY, false);
    }

    public boolean getSameGenderMarriage() {
        return manager.getConfigObject(label).getBoolean(ConfigPath.MAIN_ALLOW_SAME_GENDER_MARRIAGE, true);
    }

    public boolean getBroadcastMarriageAnnouncement() {
        return manager.getConfigObject(label).getBoolean(ConfigPath.MAIN_BROADCAST_MARRIAGE, true);
    }

    public boolean getDebugLogs() {
        return manager.getConfigObject(label).getBoolean(ConfigPath.MAIN_DEBUG_LOGS, false);
    }
}
