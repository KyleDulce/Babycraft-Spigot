package me.someoneawesome.babycraft.config;

import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import me.someoneawesome.babycraft.Babycraft;
import me.someoneawesome.babycraft.PluginLogger;

import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.List;

public class PluginConfig {
    private static final String PLUGIN_CONFIG_FILE = "pluginConfig.yml";
    public static PluginConfig instance;

    private PluginLogger LOGGER = PluginLogger.getLogger(PluginConfig.class, "Plugin Property Handler");

    public PluginConfig() {
        instance = this;
        Mono.just(PLUGIN_CONFIG_FILE)
                .map(filename -> Babycraft.instance.getResource(filename))
                .map(resourceStream -> new InputStreamReader(resourceStream))
                .map(inputStreamReader -> YamlConfiguration.loadConfiguration(inputStreamReader))
                .doOnNext(this::setupConfigVariables)
                .doOnError(throwable -> {
                    LOGGER.error("Failed to load Plugin Proptery file", throwable);
                    Babycraft.instance.fetalErrorDisable("Failed to load Plugin Property file. Try redownloading the plugin", true);
                })
                .onErrorStop()
                .doOnNext(yamlConfiguration -> LOGGER.info("Plugin Properties Loaded"))
                .block();
    }

    public PluginConfig(PluginLogger logger) {
        this.LOGGER = logger;
    }

    public void setupConfigVariables(FileConfiguration configObj) {
        //configs
        configTypes = Flux.just("main", "players", "children")
                .map(name -> {
                    ConfigType type = new ConfigType();
                    type.label = name;
                    return type;
                })
                .doOnNext(type -> type.filename = configObj.getString("config." + type.label + ".filename"))
                .doOnNext(type -> type.version = Byte.valueOf(configObj.getString("config." + type.label + ".version")))
                .buffer()
                .blockLast();

        chestplates = getMaterialListFromPath("utils.chestplates", configObj)
                        .map(HashSet::new)
                        .blockLast();
        leggings = getMaterialListFromPath("utils.leggings", configObj)
                        .map(HashSet::new)
                        .blockLast();
        boots = getMaterialListFromPath("utils.boots", configObj)
                        .map(HashSet::new)
                        .blockLast();
        speakingRadius = configObj.getInt("children.speakingRadius");
        despawnAllKeyword = configObj.getString("children.despawnAllKeyword");
        boyNames = configObj.getStringList("children.boyNames");
        girlNames = configObj.getStringList("children.girlNames");
    }

    private List<ConfigType> configTypes;
    private HashSet<Material> chestplates;
    private HashSet<Material> leggings;
    private HashSet<Material> boots;
    private int speakingRadius;
    private String despawnAllKeyword;
    private List<String> boyNames;
    private List<String> girlNames;

    public List<ConfigType> configTypes() {
        return configTypes;
    }

    public HashSet<Material> getChestplates() {
        return chestplates;
    }

    public HashSet<Material> getLeggings() {
        return leggings;
    }

    public HashSet<Material> getBoots() {
        return boots;
    }

    public int getSpeakingRadius() {
        return speakingRadius;
    }

    public String getDespawnAllKeyword() {
        return despawnAllKeyword;
    }

    public List<String> getBoyNames() {
        return boyNames;
    }

    public List<String> getGirlNames() {
        return girlNames;
    }

    public class ConfigType {
        public byte version;
        public String filename;
        public String label;
    }

    private Flux<List<Material>> getMaterialListFromPath(String path, FileConfiguration configObj) {
        return Flux.fromIterable(configObj.getStringList(path))
                .map(Material::getMaterial)
                .buffer();
    }
}
