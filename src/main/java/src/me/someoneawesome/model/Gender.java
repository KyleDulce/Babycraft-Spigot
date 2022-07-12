package src.me.someoneawesome.model;

public enum Gender {
    MALE("male", "&b",
            "boy", "he",
            "him", "boyfriend",
            "Dad", "Mr"),

    FEMALE("female", "&d",
            "girl", "she",
            "her", "girlfriend",
            "Mom", "Ms"),

    OTHER("other", "&e",
            "person", "they",
            "them", "date",
            "Person", "person"),

    NULL("person", "&f",
            "person", "they",
            "them", "date",
            "???", "person");

    private final String label;
    private final String codedChatColor;
    private final String noun;
    private final String subjectivePronoun;
    private final String objectivePronoun;
    private final String loverLabel;
    private final String parentCallme;
    private final String address;

    Gender(String label, String codedLabel,
           String noun, String subjectivePronoun,
           String objectivePronoun, String loverLabel,
           String parentCallme, String address) {
        this.label = label;
        codedChatColor = codedLabel;
        this.noun = noun;
        this.subjectivePronoun = subjectivePronoun;
        this.objectivePronoun = objectivePronoun;
        this.loverLabel = loverLabel;
        this.parentCallme = parentCallme;
        this.address = address;
    }

    @Override
    public String toString() {
        return label;
    }

    public String getCodedChatColor() {
        return codedChatColor;
    }

    public String getNoun() {
        return noun;
    }

    public String getSubjectivePronoun() {
        return subjectivePronoun;
    }

    public String getObjectivePronoun() {
        return objectivePronoun;
    }

    public String getLoverLabel() {
        return loverLabel;
    }

    public String getParentCallme() {
        return parentCallme;
    }

    public String getAddress() {
        return address;
    }

    public Gender getOppositeGender() {
        if(this == MALE) {
            return FEMALE;
        } else if(this == FEMALE) {
            return MALE;
        } else {
            return OTHER;
        }
    }

    public static boolean areEqualGenders(Gender g1, Gender g2) {
        if(g1 == NULL || g2 == NULL) {
            throw new IllegalArgumentException("Cannot compare Null Gender to another gender!");
        } else if((g1 == MALE && g2 == FEMALE) || (g1 == FEMALE && g2 == MALE)) {
            return false;
        } else if(g1 == OTHER || g2 == OTHER) {
            return false;
        } else {
            return true;
        }
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
