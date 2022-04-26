package src.me.someoneawesome;

import org.bukkit.plugin.java.JavaPlugin;
import src.me.someoneawesome.commands.CommandManager;
import src.me.someoneawesome.config.ConfigInterface;
import src.me.someoneawesome.config.ConfigManager;
import src.me.someoneawesome.config.PluginConfig;

public class Babycraft extends JavaPlugin {
    public static Babycraft instance;

    private final PluginLogger LOGGER;

    private final ConfigManager configManager;

    private final CommandManager commandManager;

    private boolean disabled = false;

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

        getCommand("babycraft").setExecutor(commandManager);
        getCommand("babycraft").setTabCompleter(commandManager);
        getCommand("bcadmin").setExecutor(commandManager);
        getCommand("bcadmin").setTabCompleter(commandManager);

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
