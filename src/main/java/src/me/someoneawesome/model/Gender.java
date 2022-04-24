package src.me.someoneawesome.model;

public enum Gender {
    MALE("male", "&bmale"),
    FEMALE("female", "&dfemale"),
    OTHER("other", "&f"),
    NULL("", "");

    private final String str;
    private final String CodedChatColor;

    Gender(String s, String s2) {
        str = s;
        CodedChatColor = s2;
    }

    @Override
    public String toString() {
        return str;
    }

    public String toStringColoredCode() {
        return CodedChatColor;
    }

    public static Gender fromString(String s) {
        if(s.equalsIgnoreCase(MALE.toString())) {
            return MALE;
        } else if(s.equalsIgnoreCase(FEMALE.toString())) {
            return FEMALE;
        } else if(s.equalsIgnoreCase(OTHER.toString())) {
            return OTHER;
        }
        return NULL;
    }
}
