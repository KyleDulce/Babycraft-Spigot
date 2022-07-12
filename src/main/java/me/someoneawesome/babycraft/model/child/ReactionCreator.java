package me.someoneawesome.babycraft.model.child;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import me.someoneawesome.babycraft.config.ConfigInterface;
import me.someoneawesome.babycraft.model.Gender;

public class ReactionCreator {
    private static final int RADIUS = 15;

    //used in msgs for chat
    //used for determining parents' genders
    private static final String PARENTGENDER = "\\{parent-gender}";
    private static final String OPPOSITEPARENTGENDER = "\\{opposite-parent-gender}";

    //gender of opposite gender of child but give em a lover name ex.Boyfriend etc
    private static final String OWNLOVER = "\\{lover}";

    //gender of own child and opposite ex. boy, girl
    private static final String OWNGENDER = "\\{own-gender}";
    private static final String OWNOPPOSITEGENDER = "\\{opposite-own-gender}";

    //gender pronouns of opposite gender of child ex. he, she (SUB, subjective), him, her (OBJ, objective)
    private static final String OWNOPPOSITEGENDERPROSUB = "\\{opposite-sub-pronoun}";
    private static final String OWNOPPOSITEGENDERPROOBJ = "\\{opposite-obj-pronoun}";

    public static void sendMessage(ActionMessage action, Child child, Player personDirected) {
        String message = action.nextMessage();

        Gender childGender = child.getGender();
        Gender personDirectedGender = ConfigInterface.instance.players.getPlayerGender(personDirected.getUniqueId());
        String parentCallme = ConfigInterface.instance.players.getPlayerCallme(personDirected.getUniqueId());

        String otherParentCallme = child.isParent(personDirected) ?
                ConfigInterface.instance.players.getPlayerCallme(child.getOtherParent(personDirected)) :
                "my parents";

        message = message.replaceAll(PARENTGENDER, child.isParent(personDirected) ? parentCallme : personDirectedGender.getAddress());
        message = message.replaceAll(OPPOSITEPARENTGENDER, otherParentCallme);
        message = message.replaceAll(OWNLOVER, childGender.getLoverLabel());
        message = message.replaceAll(OWNGENDER, childGender.getNoun());
        message = message.replaceAll(OWNOPPOSITEGENDER, childGender.getOppositeGender().getNoun());
        message = message.replaceAll(OWNOPPOSITEGENDERPROSUB, childGender.getOppositeGender().getSubjectivePronoun());
        message = message.replaceAll(OWNOPPOSITEGENDERPROOBJ, childGender.getOppositeGender().getObjectivePronoun());

        child.sayMessage(ChatColor.translateAlternateColorCodes('&',
                String.format("%s <%s> &6%s", childGender.getCodedChatColor(), child.getName(), message)));
    }
}
