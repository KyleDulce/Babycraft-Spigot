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
    public static final String MAIN_HAVECHILD_MAX_DISTANCE = "haveChild-max-distance";
    public static final String MAIN_SAVE_PERIOD = "save-period";
    public static final String MAIN_DEBUG_LOGS = "debug-logs";
    public static final String MAIN_DEFAULT_CALLME_MALE = "defaults.callmes.male";
    public static final String MAIN_DEFAULT_CALLME_FEMALE = "defaults.callmes.female";
    public static final String MAIN_DEFAULT_CALLME_OTHER = "defaults.callmes.other";
    public static final String MAIN_DEFAULT_CALLME_NONE = "defaults.callmes.none";

    //player
    public static String PLAYER_ROOT(UUID uuid) { return uuid.toString();}
    public static String PLAYER_CALLME(UUID uuid) { return uuid.toString() + ".callme";}
    public static String PLAYER_GENDER(UUID uuid) { return uuid.toString() + ".gender";}
    public static String PLAYER_PARTNER(UUID uuid) { return uuid.toString() + ".partner";}
    public static String PLAYER_PREGNANT_STATUS(UUID uuid) { return uuid.toString() + ".pregnant.status";}
    public static String PLAYER_PREGNANT_TIMELEFT(UUID uuid) { return uuid.toString() + ".pregnant.timeLeft";}
    public static String PLAYER_PREGNANT_PARTNER(UUID uuid) { return uuid.toString() + ".pregnant.partner";}
    public static String PLAYER_CHILDREN(UUID uuid) { return uuid.toString() + ".children";}

    //children
    public static String CHILD_ROOT(UUID uuid) { return uuid.toString();}
    public static String CHILD_NAME(UUID uuid) { return uuid.toString() + ".name";}
    public static String CHILD_PARENTS(UUID uuid) { return uuid.toString() + ".parents";}
    public static String CHILD_GENDER(UUID uuid) { return uuid.toString() + ".gender";}
    public static String CHILD_ARMOR_HEAD(UUID uuid) { return uuid.toString() + ".armor.head";}
    public static String CHILD_ARMOR_BODY(UUID uuid) { return uuid.toString() + ".armor.body";}
    public static String CHILD_ARMOR_LEGS(UUID uuid) { return uuid.toString() + ".armor.legs";}
    public static String CHILD_ARMOR_FEET(UUID uuid) { return uuid.toString() + ".armor.feet";}
    public static String CHILD_HOME_WORLD(UUID uuid) { return uuid.toString() + ".home.world";}
    public static String CHILD_HOME_X(UUID uuid) { return uuid.toString() + ".home.x";}
    public static String CHILD_HOME_Y(UUID uuid) { return uuid.toString() + ".home.y";}
    public static String CHILD_HOME_Z(UUID uuid) { return uuid.toString() + ".home.z";}
    public static String CHILD_COLOR(UUID uuid) { return uuid.toString() + ".color";}
}
