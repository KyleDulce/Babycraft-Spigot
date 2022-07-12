package me.someoneawesome.babycraft.model.messaging;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import me.someoneawesome.babycraft.util.BabycraftUtils;

import java.util.LinkedList;
import java.util.Queue;

public class FormattedMessage {

    private TextComponent[] textComponents;

    private FormattedMessage() {}

    public static FormattedMessageBuilder builder() {
        return new FormattedMessageBuilder();
    }

    public static FormattedMessage buildBasicMessage(String message, ChatColor color) {
        return FormattedMessage.builder()
                .appendMessage(buildBasicComponent(message, color))
                .build();
    }

    public static FormattedTextComponent buildBasicComponent(String message, ChatColor color) {
        return FormattedTextComponent.builder()
                .setContent(message)
                .setColor(color)
                .build();
    }

    public void sendMessage(CommandSender receiver) {
        receiver.spigot().sendMessage(textComponents);
    }

    public void sendMessageActionBar(Player p) {
        p.spigot().sendMessage(textComponents);
    }

    public void broadcastMessage() {
        Bukkit.spigot().broadcast(textComponents);
    }

    public static class FormattedMessageBuilder {
        private Queue<FormattedTextComponent> components = new LinkedList<>();

        public FormattedMessageBuilder appendMessage(FormattedTextComponent textComponent) {
            if(textComponent != null) {
                this.components.add(textComponent);
            }
            return this;
        }

        public FormattedMessage build() {
            TextComponent[] resultComponents = new TextComponent[components.size()];
            int index = 0;
            for(FormattedTextComponent textComponent : components) {
                resultComponents[index++] = textComponent.component;
            }
            FormattedMessage message = new FormattedMessage();
            message.textComponents = resultComponents;
            return message;
        }

        protected FormattedMessageBuilder() {}
    }

    public static class FormattedTextComponentBuilder {
        protected String message = null;
        protected ChatColor color = ChatColor.WHITE;
        protected boolean bold = false;
        protected boolean italic = false;
        protected boolean underlined = false;
        protected ClickEvent clickEvent = null;
        protected FormattedMessage hoverEvent = null;

        protected FormattedTextComponentBuilder() {}

        public FormattedTextComponent build() {
            TextComponent component;
            if(message == null) {
                return null;
            } else {
                component = new TextComponent(message);
            }

            if(clickEvent != null) {
                component.setClickEvent(clickEvent);
            }

            if(hoverEvent != null) {
                component.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, hoverEvent.textComponents));
            }

            component.setColor(color.asBungee());
            component.setBold(bold);
            component.setItalic(italic);
            component.setUnderlined(underlined);

            FormattedTextComponent result = new FormattedTextComponent();
            result.component = component;
            return result;
        }

        public FormattedTextComponentBuilder setContent(String message) {
            if(!BabycraftUtils.isStringNullOrEmpty(message)) {
                this.message = message;
            }
            return this;
        }

        public FormattedTextComponentBuilder setColor(ChatColor color) {
            if(color != null || color.isColor()) {
                this.color = color;
            }
            return this;
        }

        public FormattedTextComponentBuilder setBold() {
            bold = true;
            return this;
        }

        public FormattedTextComponentBuilder setItalics() {
            italic = true;
            return this;
        }

        public FormattedTextComponentBuilder setUnderlined() {
            underlined = true;
            return this;
        }

        public FormattedTextComponentBuilder setClickEvent_runCommand(String command) {
            clickEvent = new ClickEvent(ClickEvent.Action.RUN_COMMAND, command);
            return this;
        }

        public FormattedTextComponentBuilder setClickEvent_suggestCommand(String command) {
            clickEvent = new ClickEvent(ClickEvent.Action.SUGGEST_COMMAND, command);
            return this;
        }

        public FormattedTextComponentBuilder setHoverEvent(FormattedMessage text) {
            this.hoverEvent = text;
            return this;
        }
    }

    public static class FormattedTextComponent {
        protected TextComponent component;
        public static FormattedTextComponentBuilder builder() {
            return new FormattedTextComponentBuilder();
        }

        protected FormattedTextComponent() {}
    }
}
