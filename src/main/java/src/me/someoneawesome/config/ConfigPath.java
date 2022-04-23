package src.me.someoneawesome.config;

import java.util.UUID;

public class ConfigPath {

    //configs
    public static final String VERSION = "config-version";

    //main
    public static final String MAIN_MULTI_MARRIAGE = "allow-multiplayer-marriage";
    public static final String MAIN_CHOOSE_BABY = "choose-baby-gender";
    public static final String MAIN_ALLOW_SAME_GENDER_MARRIAGE = "allow-same-gender-marriage";
    public static final String MAIN_BROADCAST_MARRIAGE = "broadcast-marriage-annoucements";
    public static final String MAIN_REQUEST_TIMEOUT = "request-timeout-delay";

    //player
    public static String PLAYER_GENDER(UUID uuid) { return uuid.toString() + ".gender";}
    public static String PLAYER_PARTNER(UUID uuid) { return uuid.toString() + ".partner";}
    public static String PLAYER_PREGNANT_STATUS(UUID uuid) { return uuid.toString() + ".pregnant.status";}
    public static String PLAYER_PREGNANT_TIMELEFT(UUID uuid) { return uuid.toString() + ".pregnant.timeLeft";}
    public static String PLAYER_PREGNANT_PARTNER(UUID uuid) { return uuid.toString() + ".pregnant.partner";}
    public static String PLAYER_CHILDREN(UUID uuid) { return uuid.toString() + ".children";}

    //children
    public static String CHILD_NAME(UUID uuid) { return uuid.toString() + ".name";}
    public static String CHILD_PARENTS(UUID uuid) { return uuid.toString() + ".parents";}
    public static String CHILD_GENDER(UUID uuid) { return uuid.toString() + ".gender";}
    public static String CHILD_HOME_WORLD(UUID uuid) { return uuid.toString() + ".home.world";}
    public static String CHILD_HOME_X(UUID uuid) { return uuid.toString() + ".home.x";}
    public static String CHILD_HOME_Y(UUID uuid) { return uuid.toString() + ".home.y";}
    public static String CHILD_HOME_Z(UUID uuid) { return uuid.toString() + ".home.z";}
    public static String CHILD_COLOR(UUID uuid) { return uuid.toString() + ".color";}
}
