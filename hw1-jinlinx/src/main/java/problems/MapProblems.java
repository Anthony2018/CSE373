package problems;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Parts b.i and b.ii should go here.
 *
 * (Implement contains3 and intersect as described by the spec
 *  See the spec on the website for examples and more explanation!)
 */
public class MapProblems {

    public static Map<String, Integer> intersect(Map<String, Integer> m1, Map<String, Integer> m2) {
        Map<String, Integer> ans = new HashMap<String, Integer>();
        for (String key1 : m1.keySet()) {
            for (String key2 : m2.keySet()) {
                if (key1.equals(key2)) {
                    if (m1.get(key1) == m2.get(key2)) {
                        ans.put(key1, m1.get(key1));
                    }
                }
            }
        }
        return ans;
    }

    public static boolean contains3(List<String> input) {
        Map<String, Integer> map = new HashMap<String, Integer>();

        for (String tstr : input) {
            if (map.containsKey(tstr)) {
                map.put(tstr, map.get(tstr) + 1);

                if (map.get(tstr) == 3) {
                    return true; }
                } else {
                    map.put(tstr, 1);
                }
            }

            return false;
        }
}