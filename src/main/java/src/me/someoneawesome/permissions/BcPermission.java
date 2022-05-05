package src.me.someoneawesome.permissions;

public enum BcPermission {

    BABYCRAFT_SOLO("babyCraft.solo"),
    BABYCRAFT_SAME_GENDER("babyCraft.sameGender"),
    BABYCRAFT_CHANGE_GENDER("babyCraft.changeGender"),
    BABYCRAFT_SAME_GENDER_MARRIAGE("babyCraft.sameGenderMarriage"),
    BABYCRAFT_MULTI_MARRIAGE("babyCraft.multimarriage"),

    ADMIN_RELOAD("babyCraft.Admin.reload"),
    ADMIN_DESPAWN_ALL("babyCraft.Admin.DespawnAll"),
    ADMIN_SAVE_CONFIG("babyCraft.Admin.saveConfig"),
    ADMIN_SETTINGS("babyCraft.Admin.settings")
    ;

    private final String permissionPath;
    BcPermission(String permissionPath) {
        this.permissionPath = permissionPath;
    }

    public String toString() {
        return permissionPath;
    }
}
