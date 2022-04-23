package src.me.someoneawesome;

import java.util.logging.Logger;

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

    }

    public void info(String message) {

    }

    public void warn(String message) {

    }

    public void error(String message) {

    }

    public void error(String message, Throwable throwable) {

    }

    public static PluginLogger getLogger(Class<?> loggerOrigin, String label) {
        return new PluginLogger(loggerOrigin, label);
    }

    public static PluginLogger getLogger(Class<?> loggerOrigin) {
        return new PluginLogger(loggerOrigin);
    }
}
