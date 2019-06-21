package ileinterdite.util;

import ileinterdite.model.adventurers.Adventurer;

import java.util.ArrayList;

public class StartMessage extends Message {


    public StartMessage() {
        super();
        adventurers = new ArrayList<>();
    }



    public ArrayList<Adventurer> adventurers;
    public ArrayList<String> playerName;
    public boolean randomOption;
    public boolean demoOption;
    public boolean logOption;
    public int difficulty;
}

