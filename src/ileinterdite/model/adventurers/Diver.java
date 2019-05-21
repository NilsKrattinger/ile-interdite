package ileinterdite.model.adventurers;

import ileinterdite.model.Grid;
import ileinterdite.util.Utils;

public class Diver extends Adventurer {
    public void cellChoiceMoving(Utils.State[][] tab) {
        treatBoard(this.getX(),this.getY(),tab);
        for (int i=0; i < Grid.WIDTH; i++) {
            for (int j=0; j < Grid.HEIGHT; j++) {
                if (tab[i][j] != Utils.State.ACCESSIBLE) {
                    tab[i][j] = Utils.State.INACCESSIBLE;
                }
            }
        }
    }

    public void treatBoard(int x, int y, Utils.State[][] tab) {
        if (x >= 0 && x < Grid.WIDTH && y >= 0 && y < Grid.WIDTH) {
            Utils.State state = tab[x][y];
            if ((x != this.getX() || y != this.getY()) && state != Utils.State.SUNKEN) {
                tab[x][y] = Utils.State.ACCESSIBLE;
            } else {
                tab[x][y] = Utils.State.INACCESSIBLE;
            }

            if ((x == this.getX() && y == this.getY()) || state == Utils.State.FLOODED || state == Utils.State.SUNKEN) {
                treatBoard(x - 1, y, tab);
                treatBoard(x + 1, y, tab);
                treatBoard(x, y - 1, tab);
                treatBoard(x, y + 1, tab);
            }
        }
    }

}