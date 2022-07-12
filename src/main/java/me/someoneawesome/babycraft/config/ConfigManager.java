package me.someoneawesome.babycraft.config;

import org.bukkit.Bukkit;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import me.someoneawesome.babycraft.Babycraft;
import me.someoneawesome.babycraft.PluginLogger;

import java.util.HashMap;

public class ConfigManager {

    private static final PluginLogger LOGGER = PluginLogger.getLogger(ConfigManager.class, "Configuration Manager");
    public static ConfigManager instance;

    private final HashMap<String, ConfigObject> configObjects = new HashMap<>();

    private int saveTaskId = -1;

    public ConfigManager() {
        instance = this;
    }

    public void loadConfigs() {
        Flux.fromIterable(PluginConfig.instance.configTypes())
                .flatMap(type -> ConfigObject.setupConfigObject(type))
                .doOnNext(configObject -> configObjects.put(configObject.getConfigProps().label, configObject))
                .doOnNext(configObject -> {
                    int version = configObject.getInt(ConfigPath.VERSION, -1);
                    if(version < 0) {
                        LOGGER.warn("Invalid configuration! Resetting config");
                        configObject.resetConfigToDefault();
                    } else if(version < configObject.getConfigProps().version) {
                        LOGGER.warn("Old version found, updating to current version");
                        ConfigUpdater.updateConfig(configObject);
                    } else if(version > configObject.getConfigProps().version) {
                        LOGGER.warn("Future version config used, please update Babycraft to the latest version before using this config");
                        configObject.resetConfigToDefault();
                    } else {
                        LOGGER.info("Using config " + configObject.getConfigProps().filename + " version " + version);
                    }
                })
                .buffer()
                .doOnNext(configs -> startRepeatingTask())
                .blockLast();
    }

    private void stopRepeatingTask() {
        if(saveTaskId >= 0) {
            LOGGER.info("Stopping save task");
            Bukkit.getScheduler().cancelTask(saveTaskId);
            saveTaskId = -1;
        }
    }

    private void startRepeatingTask() {
        LOGGER.info("Starting save task");
        double savePeriod = ConfigInterface.instance.main.getConfigSavePeriod();
        if(savePeriod > 0D) {
            long savePeriodLong = ((long) savePeriod) * 20L;
            LOGGER.info("Saving config every " + savePeriodLong + " server ticks");
            saveTaskId = Bukkit.getScheduler().scheduleSyncRepeatingTask(Babycraft.instance, () -> scheduledConfigSave().block(),
                    savePeriodLong, savePeriodLong);
            if(saveTaskId <= -1) {
                LOGGER.warn("Save period scheduling failed, saving on every write");
                ConfigInterface.instance.main.setConfigSavePeriod(-1D);
                saveConfigs().block();
            }
        }
    }

    public Mono<Void> saveConfigs() {
        return Flux.fromIterable(configObjects.values())
                .doOnNext(ConfigObject::save)
                .then();
    }

    public Mono<Void> reloadConfigs() {
        return Mono.just(configObjects)
                .doOnNext(obj -> stopRepeatingTask())
                .flatMapIterable(HashMap::values)
                .doOnNext(ConfigObject::reloadConfiguration)
                .buffer()
                .doOnNext(configs -> startRepeatingTask())
                .then();
    }

    public Mono<Void> scheduledConfigSave() {
        return Flux.fromIterable(configObjects.values())
                .doOnNext(config -> LOGGER.info("Running scheduled save of config files that received changes"))
                .filter(ConfigObject::isDirty)
                .doOnNext(ConfigObject::save)
                .doOnNext(ConfigObject::clearDirty)
                .then();
    }

    public Mono<Void> resetConfigs() {
        return Flux.fromIterable(configObjects.values())
                .doOnNext(ConfigObject::resetConfigToDefault)
                .then();
    }

    public ConfigObject getConfigObject(String type) {
        return configObjects.get(type);
    }
}
