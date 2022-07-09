package src.me.someoneawesome.model.comparator;

import java.util.Comparator;

public class AlphabeticalComparator implements Comparator<String> {
    @Override
    public int compare(String o1, String o2) {
        o1 = o1.toLowerCase();
        o2 = o2.toLowerCase();

        if(o1.equals(o2)) {
            return 0;
        }

        int length = Math.min(o1.length(), o2.length());

        for(int x = 0; x < length; x++) {
            if(o1.charAt(x) != o2.charAt(x)) {
                return o1.charAt(x) - o2.charAt(x);
            }
        }

        return Integer.compare(o1.length(), o2.length());
    }
}
