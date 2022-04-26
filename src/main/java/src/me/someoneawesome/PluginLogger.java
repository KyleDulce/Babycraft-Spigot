package src.me.someoneawesome;

import org.bukkit.ChatColor;

import java.util.logging.Level;

public class PluginLogger {
    private final Class<?> logOrigin;
    private final String label;
    private final boolean HAS_LABEL;

    private PluginLogger(Class<?> origin, String label) {
        this.logOrigin = origin;
        this.label = label;
        HAS_LABEL = true;
    }

    private PluginLogger(Class<?> origin) {
        this.logOrigin = origin;
        this.label = null;
        HAS_LABEL = false;
    }

    public void debug(String message) {
        if(Babycraft.instance.debugMode) {
            Babycraft.instance.getLogger().log(Level.INFO, ChatColor.DARK_AQUA + message);
        }
    }

    public void info(String message) {
        Babycraft.instance.getLogger().log(Level.INFO, ChatColor.GREEN + message);
    }

    public void warn(String message) {
        Babycraft.instance.getLogger().log(Level.WARNING, ChatColor.YELLOW + message);
    }

    public void error(String message) {
        Babycraft.instance.getLogger().log(Level.WARNING, ChatColor.RED + message);
    }

    public void error(String message, Throwable throwable) {
        Babycraft.instance.getLogger().log(Level.WARNING, ChatColor.RED + message, throwable);
    }

    public static PluginLogger getLogger(Class<?> loggerOrigin, String label) {
        return new PluginLogger(loggerOrigin, label);
    }

    public static PluginLogger getLogger(Class<?> loggerOrigin) {
        return new PluginLogger(loggerOrigin);
    }
}
