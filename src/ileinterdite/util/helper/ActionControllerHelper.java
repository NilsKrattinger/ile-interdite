package ileinterdite.util.helper;

import ileinterdite.model.Grid;
import ileinterdite.util.Tuple;
import ileinterdite.util.Utils;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ActionControllerHelper {

    /**
     * Split the message contents to retrieve the position
     *
     * @param msg Message The message received from the view
     * @return Tuple&lt;Integer, Integer&gt; with read coordinated or (-1,-1) if not understood
     */
    public static Tuple<Integer, Integer> getPositionFromMessage(String msg) {
        Pattern p = Pattern.compile("(\\d).+(\\d)");
        Matcher m = p.matcher(msg);
        if (m.find()) {
            int x = Integer.valueOf(m.group(1)) - 1; // Messages are sent in human-readable format, which starts at 1
            int y = Integer.valueOf(m.group(2)) - 1;
            return new Tuple<>(x, y);
        }

        return new Tuple<>(-1, -1); // This should never happen, as coordinates are sent by components
    }

    /**
     * Checks if the entered position is valid
     * @param pos The position to check
     * @param states The state of the cells for this action
     * @return true if the position is in bounds and the cell at the given coordinates is accessible
     */
    public static boolean checkPosition(Tuple<Integer, Integer> pos, Utils.State[][] states) {
        return pos.x >= 0 && pos.y >= 0
                && pos.x < Grid.WIDTH && pos.y < Grid.HEIGHT // Position in bounds
                && states[pos.y][pos.x] == Utils.State.ACCESSIBLE; // && cell accessible
    }
}
