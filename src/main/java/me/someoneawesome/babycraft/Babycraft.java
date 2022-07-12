package me.someoneawesome.babycraft;

import me.someoneawesome.babycraft.commands.CommandManager;
import me.someoneawesome.babycraft.config.ConfigInterface;
import me.someoneawesome.babycraft.config.ConfigManager;
import me.someoneawesome.babycraft.config.PluginConfig;
import me.someoneawesome.babycraft.event.listeners.EntityListener;
import me.someoneawesome.babycraft.event.listeners.InventoryListener;
import me.someoneawesome.babycraft.event.listeners.PlayerListener;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

public class Babycraft extends JavaPlugin {
    public static Babycraft instance;

    private final PluginLogger LOGGER;

    private final ConfigManager configManager;

    private final CommandManager commandManager;

    private boolean disabled = false;
    boolean debugMode = false;

    public Babycraft() {
        instance = this;
        LOGGER = PluginLogger.getLogger(Babycraft.class);

        LOGGER.info("Loading Babycraft");

        new PluginConfig();
        configManager = new ConfigManager();
        new ConfigInterface();

        commandManager = new CommandManager();
        LOGGER.info("Babycraft Loaded");
    }

    @Override
    public void onEnable() {
        LOGGER.info("Enabling Babycraft");

        if(disabled) {
            fetalErrorDisable("Cannot enable babycraft, problem with plugin jar was found and cannot recover, " +
                    " try redownloading the plugin and try again");
            return;
        }
        configManager.loadConfigs();
        debugMode = ConfigInterface.instance.main.getDebugLogs();
        PluginManager pluginManager = getServer().getPluginManager();

        getCommand("babycraft").setExecutor(commandManager);
        getCommand("babycraft").setTabCompleter(commandManager);
        getCommand("bcadmin").setExecutor(commandManager);
        getCommand("bcadmin").setTabCompleter(commandManager);

        pluginManager.registerEvents(new EntityListener(), this);
        pluginManager.registerEvents(new InventoryListener(), this);
        pluginManager.registerEvents(new PlayerListener(), this);

        LOGGER.info("Babycraft Enabled");
    }

    @Override
    public void onDisable() {
        LOGGER.info("Disabling Babycraft");

        configManager.saveConfigs();

        LOGGER.info("Babycraft Disabled");
    }

    public void fetalErrorDisable(String message) {
        StringBuilder builder = new StringBuilder();
        builder.append("Fetal: '")
                .append(message)
                .append("'\nDisabling Self");
        LOGGER.error(builder.toString());
        this.getServer().getPluginManager().disablePlugin(this);
    }

    public void fetalErrorDisable(String message, boolean perm) {
        if(perm) {
            disabled = true;
        }
        fetalErrorDisable(message);
    }
}
