package ileinterdite.factory;

import ileinterdite.model.adventurers.*;
import ileinterdite.util.Utils;

import java.io.BufferedReader;
import java.io.IOException;

public class AdventurersFactory {

    public static Adventurer[] adventurerFactory() {

        Adventurer[] adventurers = new Adventurer[6];
        adventurers[0] = new Diver();
        adventurers[1] = new Engineer();
        adventurers[2] = new Explorer();
        adventurers[3] = new Messager();
        adventurers[4] = new Navigator();
        adventurers[5] = new Pilot();
        return adventurers;
    }

}
