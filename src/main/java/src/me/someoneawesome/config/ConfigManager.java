package src.me.someoneawesome.config;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import src.me.someoneawesome.PluginLogger;

import java.util.HashMap;

public class ConfigManager {

    private static final PluginLogger LOGGER = PluginLogger.getLogger(ConfigManager.class, "Configuration Manager");
    public static ConfigManager instance;

    private final HashMap<String, ConfigObject> configObjects = new HashMap<>();

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
                }).blockLast();
    }

    public Mono<Void> saveConfigs() {
        return Flux.fromIterable(configObjects.values())
                .doOnNext(ConfigObject::save)
                .then();
    }

    public Mono<Void> reloadConfigs() {
        return Flux.fromIterable(configObjects.values())
                .doOnNext(ConfigObject::reloadConfiguration)
                .then();
    }

    public ConfigObject getConfigObject(String type) {
        return configObjects.get(type);
    }
}
