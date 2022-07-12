package src.me.someoneawesome.model.requirement;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import src.me.someoneawesome.config.ConfigInterface;
import src.me.someoneawesome.model.Gender;
import src.me.someoneawesome.model.permissions.BcPermission;

import java.util.LinkedList;

public class RequirementVerifierBuilder {

    LinkedList<Requirement> requirements = new LinkedList<>();
    ConfigInterface codeInterface;

    private RequirementVerifierBuilder() {
        codeInterface = ConfigInterface.instance;
    }

    public static RequirementVerifierBuilder builder() {
        return new RequirementVerifierBuilder();
    }

    public RequirementVerifier build() {
        return new RequirementVerifier(requirements.toArray(new Requirement[requirements.size()]));
    }

    public RequirementVerifierBuilder playerHasPermission(Player player, BcPermission permission, String failMessage) {
        requirements.add(new Requirement() {
            @Override
            public boolean isFulfilled() {
                return player.hasPermission(permission.toString());
            }

            @Override
            public String onFailMessage() {
                return failMessage;
            }
        });
        return this;
    }

    public RequirementVerifierBuilder locationsWithinRadius(Location loc1, Location loc2, double radius, String failMessage) {
        requirements.add(new Requirement() {
            @Override
            public boolean isFulfilled() {
                return loc1.distance(loc2) <= radius;
            }

            @Override
            public String onFailMessage() {
                return failMessage;
            }
        });
        return this;
    }

    public RequirementVerifierBuilder playersWithinRadius(Player player1, Player player2, double radius, String failMessage) {
        return locationsWithinRadius(player1.getLocation(), player2.getLocation(), radius, failMessage);
    }

    public RequirementVerifierBuilder playerExistInConfig(Player player, String failMessage) {
        requirements.add(new Requirement() {
            @Override
            public boolean isFulfilled() {
                return codeInterface.players.contains(player.getUniqueId());
            }

            @Override
            public String onFailMessage() {
                return failMessage;
            }
        });
        return this;
    }

    public RequirementVerifierBuilder playerGenderNotNull(Player player, String failMessage) {
        requirements.add(new Requirement() {
            @Override
            public boolean isFulfilled() {
                return codeInterface.players.contains(player.getUniqueId()) &&
                        codeInterface.players.getPlayerGender(player.getUniqueId()) != Gender.NULL;
            }

            @Override
            public String onFailMessage() {
                return failMessage;
            }
        });
        return this;
    }

    public RequirementVerifierBuilder haveChildSameGenderCheck(Player player, Player other, String failMessage) {
        requirements.add(new Requirement() {
            @Override
            public boolean isFulfilled() {
                if(player.hasPermission(BcPermission.BABYCRAFT_SAME_GENDER.toString()) &&
                other.hasPermission(BcPermission.BABYCRAFT_SAME_GENDER.toString())) {
                    return true;
                } else {
                    Gender playerGender = ConfigInterface.instance.players.getPlayerGender(player.getUniqueId());
                    Gender otherGender = ConfigInterface.instance.players.getPlayerGender(other.getUniqueId());
                    if(Gender.areEqualGenders(playerGender, otherGender)) {
                        return false;
                    }
                    return true;
                }
            }

            @Override
            public String onFailMessage() {
                return failMessage;
            }
        });
        return this;
    }
}
