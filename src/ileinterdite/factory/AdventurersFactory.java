package ileinterdite.factory;

import ileinterdite.model.adventurers.*;
import ileinterdite.util.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.util.ArrayList;

public class AdventurersFactory {

    public static ArrayList<Adventurer> adventurerFactory() {

        ArrayList<Adventurer> adventurers = new ArrayList<>();
        adventurers.add(new Diver());
        adventurers.add(new Engineer());
        adventurers.add(new Explorer());
        adventurers.add(new Messager());
        adventurers.add(new Navigator());
        adventurers.add(new Pilot());
        return adventurers;
    }

}
