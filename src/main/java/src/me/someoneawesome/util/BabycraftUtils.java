package src.me.someoneawesome.util;

import src.me.someoneawesome.Babycraft;

import java.io.InputStream;
import java.util.Optional;

public class BabycraftUtils {
    public static Optional<InputStream> getResourceAsOptional(String filename) {
        InputStream stream = Babycraft.instance.getResource(filename);
        if(stream != null) {
            return Optional.of(stream);
        } else {
            return Optional.empty();
        }
    }

    public static boolean isStringNullOrEmpty(String str) {
        return str == null || str.isEmpty() || str.isBlank();
    }
}
