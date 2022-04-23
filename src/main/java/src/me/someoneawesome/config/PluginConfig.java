package src.me.someoneawesome.config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import src.me.someoneawesome.Babycraft;
import src.me.someoneawesome.PluginLogger;

import java.io.InputStreamReader;
import java.util.List;

public class PluginConfig {
    private static final String PLUGIN_CONFIG_FILE = "pluginConfig.yml";
    public static PluginConfig instance;

    private final PluginLogger LOGGER = PluginLogger.getLogger(PluginConfig.class, "Plugin Property Handler");

    public PluginConfig() {
        instance = this;
        Mono.just(PLUGIN_CONFIG_FILE)
                .map(filename -> Babycraft.instance.getResource(filename))
                .map(resourceStream -> new InputStreamReader(resourceStream))
                .map(inputStreamReader -> YamlConfiguration.loadConfiguration(inputStreamReader))
                .doOnNext(this::setupConfigVariables)
                .doOnError(throwable -> {
                    LOGGER.error("Failed to load Plugin Proptery file", throwable);
                    Babycraft.instance.fetalErrorDisable("Failed to load Plugin Property file. Try redownloading the plugin");
                })
                .onErrorStop()
                .doOnNext(yamlConfiguration -> LOGGER.info("Plugin Properties Loaded"))
                .block();
    }

    private void setupConfigVariables(FileConfiguration configObj) {
        //configs
        configTypes = Flux.just("main", "players", "children")
                .map(name -> {
                    ConfigType type = new ConfigType();
                    type.label = name;
                    return type;
                })
                .doOnNext(type -> type.filename = configObj.getString("config." + type.label + ".filename"))
                .doOnNext(type -> type.filename = configObj.getString("config." + type.label + ".version"))
                .buffer()
                .blockLast();


    }

    private List<ConfigType> configTypes;

    public List<ConfigType> configTypes() {
        return configTypes;
    }

    public class ConfigType {
        public byte version;
        public String filename;
        public String label;
    }
}
