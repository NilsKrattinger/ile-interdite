package ileinterdite.factory;

import java.util.ArrayList;
import ileinterdite.model.Treasure;

public class TreasureFactory {
    public static ArrayList<Treasure> treasureFactory() {
        ArrayList<Treasure> treasures = new ArrayList<>();
        treasures.add(new Treasure("PIERRE"));
        treasures.add(new Treasure("ZEPHIR"));
        treasures.add(new Treasure("CRISTAL"));
        treasures.add(new Treasure("CALICE"));
        return treasures;
    }
}
