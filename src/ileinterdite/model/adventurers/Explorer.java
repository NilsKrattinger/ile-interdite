package ileinterdite.model.adventurers;

import ileinterdite.model.Grid;
import ileinterdite.util.Utils;

import java.util.Collection;

public class Explorer extends Adventurer {

    /**
     * transforme le tableau d'état des tuiles donné en paramètre en un tableau qui indique pour chaque tuile, si elle est accessible ou non par l'explorateur
     * @param tab
     */
    @Override
    public void cellChoiceMoving(Utils.State[][] tab) {
        for (int i = 0; i < Grid.WIDTH; i++) {
            for (int j = 0; j< Grid.HEIGHT; j++) {
                Utils.State state = tab[i][j];
                if ((state == Utils.State.FLOODED || state == Utils.State.NORMAL)
                        && this.getX() >= i-1 && this.getX() <= i+1
                        && this.getY() >= j-1 && this.getY() <= j+1
                        && (this.getX() != i || this.getY() != j)) {
                    tab[i][j] = Utils.State.ACCESSIBLE;
                } else {
                    tab[i][j] = Utils.State.INACCESSIBLE;
                }
            }
        }
    }

    /**
     * transforme le tableau d'état des tuiles donné en paramètre en un tableau qui indique pour chaque tuile, si elle est assechable ou non par l'explorateur
     * @param tab
     */
    @Override
    public void cellChoiceDrying(Utils.State[][] tab) {
        for (int i = 0; i < Grid.WIDTH; i++) {
            for (int j = 0; j< Grid.HEIGHT; j++) {
                Utils.State state = tab[i][j];
                if (state == Utils.State.FLOODED
                        && this.getX() >= i-1 && this.getX() <= i+1
                        && this.getY() >= j-1 && this.getY() <= j+1) {
                    tab[i][j] = Utils.State.ACCESSIBLE;
                } else {
                    tab[i][j] = Utils.State.INACCESSIBLE;
                }
            }
        }
    }

}