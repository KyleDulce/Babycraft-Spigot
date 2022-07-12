package me.someoneawesome.babycraft.config;

import me.someoneawesome.babycraft.exceptions.FailedToReadConfigException;
import org.bukkit.Color;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import reactor.core.publisher.Mono;
import reactor.core.publisher.SynchronousSink;
import me.someoneawesome.babycraft.Babycraft;
import me.someoneawesome.babycraft.PluginLogger;
import me.someoneawesome.babycraft.util.BabycraftUtils;

import java.io.*;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;

public class ConfigObject {
    private static final PluginLogger LOGGER = PluginLogger.getLogger(ConfigObject.class, "Configuration Manager");
    private final Babycraft plugin;

    private FileConfiguration fileConfig;
    private File file;
    private final PluginConfig.ConfigType configProps;
    private boolean isDirty = false;

    private ConfigObject(PluginConfig.ConfigType configProps) {
        this.configProps = configProps;
        this.plugin = Babycraft.instance;
    }

    private Optional<File> createIfNotExists() {
        if(!plugin.getDataFolder().exists()) {
            LOGGER.debug("Data Folder '" + configProps.filename + "' does not exist, creating");
            plugin.getDataFolder().mkdir();
        }

        this.file = new File(plugin.getDataFolder(), configProps.filename);
        if(!file.exists()) {
            LOGGER.info("File '" + configProps.filename + "' does not exist, creating");
            Optional<InputStream> optionalInputStream = BabycraftUtils.getResourceAsOptional(configProps.filename);
            if(!createNewFile(file, optionalInputStream)) {
                return Optional.empty();
            }
        }
        return Optional.of(file);
    }

    private boolean createNewFile(File file) {
        return createNewFile(file, Optional.empty());
    }

    private boolean createNewFile(File file, Optional<InputStream> fileContents) {
        LOGGER.info("Creating file:'" + file.getName() + "'");
        try {
            file.createNewFile();
            if(fileContents.isPresent()) {
                FileWriter fileWriter = new FileWriter(file);
                BufferedWriter writer = new BufferedWriter(fileWriter);
                BufferedReader reader = new BufferedReader(new InputStreamReader(fileContents.get()));
                LOGGER.debug("Writing to new file");

                String line = reader.readLine();
                while(line != null) {
                    writer.write(line);
                    writer.write("\n");
                    line = reader.readLine();
                }

                writer.close();
                reader.close();

                LOGGER.debug("File '" + configProps.filename + "' has been written to");
            }
            LOGGER.info("File '" + configProps.filename + "' has been successfully created");
            return true;
        } catch (Exception e) {
            LOGGER.error("An error occurred during the creation of '" + configProps.filename + "'", e);
            return false;
        }
    }

    public void backupConfiguration() {
        String newfilename = configProps.filename + "-old";
        File newFile = new File(plugin.getDataFolder(), newfilename + ".txt");
        int fileIndex = 0;

        while(newFile.exists()) {
            fileIndex++;
            newFile = new File(plugin.getDataFolder(), newfilename + fileIndex + ".txt");
        }

        LOGGER.warn("Backing up " + configProps.filename + " into " + newFile.getName());

        boolean result;
        if(!file.exists()) {
            result = createNewFile(newFile);
        } else {
            result = createNewFile(newFile, BabycraftUtils.getResourceAsOptional(configProps.filename));
        }

        if(!result) {
            LOGGER.error("Failed to backup old config!");
        } else {
            LOGGER.info("Backup into " + newFile.getName() + " successful");
        }
    }

    public boolean resetConfigToDefault() {
        LOGGER.warn("Resetting config to default values and backing up old config");
        backupConfiguration();
        file.delete();
        if(!createNewFile(file, BabycraftUtils.getResourceAsOptional(configProps.filename)) || !loadFromFile()) {
            return false;
        }
        LOGGER.info("Config successfully reset!");
        return true;
    }

    public void reloadConfiguration() {
        LOGGER.info("Reloading '" + configProps.filename + "'");
        Optional<File> optionalFile = createIfNotExists();
        if(optionalFile.isEmpty()) {
            LOGGER.warn("Cannot recreate config file, reloading failed");
            return;
        }
        if(!loadFromFile()) {
            LOGGER.info("Loading failed, recreating configuration file");
            if(!resetConfigToDefault()) {
                plugin.fetalErrorDisable("Failed to recover new config values");
            }
        } else {
            LOGGER.info("Reload of " + configProps.filename + " Complete!");
        }
    }

    private boolean loadFromFile() {
        LOGGER.info("Loading " + configProps.filename);
        try {
            fileConfig = YamlConfiguration.loadConfiguration(file);
        } catch (Exception e) {
            LOGGER.error("Loading of " + fileConfig + "failed", e);
            return false;
        }
        return true;
    }

    public void save() {
        LOGGER.info("Saving " + configProps.filename);
        try {
            fileConfig.save(file);
            LOGGER.info(configProps.filename + " has been saved");
        } catch (IOException e) {
            LOGGER.error("Could not save " + configProps.filename, e);
        }
    }

    public static Mono<ConfigObject> setupConfigObject(PluginConfig.ConfigType configProps) {
        ConfigObject result = new ConfigObject(configProps);
        return Mono.just("")
                .map(call -> result.createIfNotExists())
                .handle(getFetalOptionalConsumer(new FailedToReadConfigException("Failed to create file: " + result.configProps.filename)))
                .map(call -> result.loadFromFile())
                .flatMap(bool -> {
                    if(!bool) {
                        if(!result.resetConfigToDefault()) {
                            return Mono.error(new FailedToReadConfigException("Failed to reset file: " + result.configProps.filename));
                        }
                    }
                    return Mono.just(result);
                })
                .doOnNext(configObject -> LOGGER.info("Config " + configObject.configProps.filename + " was loaded successfully"));
    }

    private static <T> BiConsumer<Optional<T>, SynchronousSink<T>> getFetalOptionalConsumer(Throwable errorThrow) {
        return (optional, sink) -> {
            if(optional.isEmpty()){
                sink.error(errorThrow);
            } else {
                sink.next(optional.get());
            }
        };
    }

    public void setDirty() {
        isDirty = true;
    }

    public void clearDirty() {
        isDirty = false;
    }

    public boolean isDirty() {
        return isDirty;
    }

    public void set(String path, Object obj) {
        fileConfig.set(path, obj);
        if(ConfigInterface.instance.main.getConfigSavePeriod() <= 0) {
            save();
        } else {
            setDirty();
        }
    }

    public int getInt(String path, int def) {
        return fileConfig.getInt(path, def);
    }

    public long getLong(String path, long def) {
        return fileConfig.getLong(path, def);
    }

    public boolean getBoolean(String path, boolean def) {
        return fileConfig.getBoolean(path, def);
    }

    public String getString(String path, String def) {
        return fileConfig.getString(path, def);
    }

    public double getDouble(String path, double def) {
        return fileConfig.getDouble(path, def);
    }

    public Color getColor(String path, Color def) {
        return fileConfig.getColor(path, def);
    }

    public ItemStack getItemStack(String path) {
        return fileConfig.getItemStack(path);
    }

    public List<String> getStringList(String path) {
        return fileConfig.getStringList(path);
    }

    public boolean contains(String path) {
        return fileConfig.contains(path);
    }

    public PluginConfig.ConfigType getConfigProps() {
        return configProps;
    }
}
