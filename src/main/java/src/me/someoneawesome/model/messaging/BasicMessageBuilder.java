package src.me.someoneawesome.model.messaging;

import org.bukkit.ChatColor;

import java.util.LinkedList;

public class BasicMessageBuilder {

    private LinkedList<String> lines = new LinkedList<>();

    private BasicMessageBuilder() {}

    public static BasicMessageBuilder builder() {
        return new BasicMessageBuilder();
    }

    public BasicMessageBuilder addLine(String message, char colorCodeIndicator) {
        lines.add(ChatColor.translateAlternateColorCodes(colorCodeIndicator, message));
        return this;
    }

    public BasicMessageBuilder addLine(String message, ChatColor chatColor) {
        lines.add(chatColor + message);
        return this;
    }

    public BasicMessageBuilder addLine(String message) {
        return addLine(message, '&');
    }

    public String[] build() {
        return lines.toArray(new String[lines.size()]);
    }
}
