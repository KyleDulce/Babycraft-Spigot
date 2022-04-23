package src.me.someoneawesome;

import org.bukkit.plugin.java.JavaPlugin;

public class Babycraft extends JavaPlugin {
    public static Babycraft instance;

    private final PluginLogger LOGGER;

    public Babycraft() {
        instance = this;
        LOGGER = PluginLogger.getLogger(Babycraft.class);
    }

    @Override
    public void onEnable() {
        LOGGER.info("Enabling Babycraft");

        LOGGER.info("Babycraft Enabled");
    }

    @Override
    public void onDisable() {
        LOGGER.info("Disabling Babycraft");

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
}
