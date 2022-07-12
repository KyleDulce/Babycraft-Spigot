package me.someoneawesome.babycraft.model.requirement;

public class RequirementVerifier {
    private Requirement[] requirements;

    public RequirementCheck doesMeetRequirements() {
        for(Requirement r : requirements) {
            if(!r.isFulfilled()) {
                return RequirementCheck.failedCheck(r.onFailMessage());
            }
        }
        return RequirementCheck.successCheck();
    }

    RequirementVerifier(Requirement[] requirements) {
        this.requirements = requirements;
    };
}
