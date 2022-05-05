package src.me.someoneawesome.requirement;

import java.util.NoSuchElementException;

public class RequirementCheck {

    private boolean success;
    private String message;

    public boolean isSuccess() {
        return success;
    }

    public String getMessage() {
        if(success)
            return message;
        else
            throw new NoSuchElementException("There is no element in this check");
    }

    static RequirementCheck successCheck() {
        RequirementCheck check =  new RequirementCheck();
        check.success = true;
        return check;
    }

    static RequirementCheck failedCheck(String message) {
        RequirementCheck check =  new RequirementCheck();
        check.success = false;
        check.message = message;
        return check;
    }
}
