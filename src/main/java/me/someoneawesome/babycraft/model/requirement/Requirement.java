package me.someoneawesome.babycraft.model.requirement;

public interface Requirement {
    boolean isFulfilled();
    String onFailMessage();
}
