package me.someoneawesome.babycraft.config.interfaces;

import me.someoneawesome.babycraft.config.ConfigManager;
import me.someoneawesome.babycraft.config.ConfigPath;
import me.someoneawesome.babycraft.model.Gender;

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

    public String getMaleCallme() {
        return manager.getConfigObject(label).getString(ConfigPath.MAIN_DEFAULT_CALLME_MALE, Gender.MALE.getParentCallme());
    }

    public String getFemaleCallme() {
        return manager.getConfigObject(label).getString(ConfigPath.MAIN_DEFAULT_CALLME_FEMALE, Gender.FEMALE.getParentCallme());
    }

    public String getOtherCallme() {
        return manager.getConfigObject(label).getString(ConfigPath.MAIN_DEFAULT_CALLME_OTHER, Gender.OTHER.getParentCallme());
    }

    public String getNoneCallme() {
        return manager.getConfigObject(label).getString(ConfigPath.MAIN_DEFAULT_CALLME_NONE, Gender.NULL.getParentCallme());
    }

    public double haveChildMaxDistance() {
        return manager.getConfigObject(label).getDouble(ConfigPath.MAIN_HAVECHILD_MAX_DISTANCE, 10D);
    }

    public double getConfigSavePeriod() {
        return manager.getConfigObject(label).getDouble(ConfigPath.MAIN_HAVECHILD_MAX_DISTANCE, -1D);
    }
}
