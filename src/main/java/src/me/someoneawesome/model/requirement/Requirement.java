package src.me.someoneawesome.model.requirement;

public interface Requirement {
    boolean isFulfilled();
    String onFailMessage();
}
