package me.someoneawesome.babycraft;

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
            Babycraft.instance.getLogger().log(Level.INFO, formMessage(ChatColor.DARK_AQUA, message));
        }
    }

    public void info(String message) {
        Babycraft.instance.getLogger().log(Level.INFO, formMessage(ChatColor.GREEN, message));
    }

    public void warn(String message) {
        Babycraft.instance.getLogger().log(Level.WARNING, formMessage(ChatColor.YELLOW, message));
    }

    public void error(String message) {
        Babycraft.instance.getLogger().log(Level.WARNING, formMessage(ChatColor.RED, message));
    }

    public void error(String message, Throwable throwable) {
        Babycraft.instance.getLogger().log(Level.WARNING, formMessage(ChatColor.RED, message), throwable);
    }

    private String formMessage(ChatColor color, String message) {
        if(label == null) {
            return String.format("%s[%s] %s", color, logOrigin.getSimpleName(), message);
        } else {
            return String.format("%s[%s/%s] %s", color, label, logOrigin.getSimpleName(), message);
        }
    }

    public static PluginLogger getLogger(Class<?> loggerOrigin, String label) {
        return new PluginLogger(loggerOrigin, label);
    }

    public static PluginLogger getLogger(Class<?> loggerOrigin) {
        return new PluginLogger(loggerOrigin);
    }
}
