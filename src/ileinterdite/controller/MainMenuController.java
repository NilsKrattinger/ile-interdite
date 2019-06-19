package ileinterdite.controller;

import ileinterdite.util.IObservable;
import ileinterdite.util.IObserver;
import ileinterdite.util.Parameters;
import ileinterdite.util.StartMessage;

import java.util.ArrayList;

public class MainMenuController implements IObserver<StartMessage> {

    private ArrayList<String> playerName = new ArrayList<>();

    public ArrayList<String> getPlayersName() {
        return playerName;
    }

    @Override
    public void update(IObservable<StartMessage> o, StartMessage message) {
        playerName = message.playerName;
        Parameters.LOGS = message.logOption;
        Parameters.DEMOMAP = message.demoOption;
        Parameters.RANDOM = message.randomOption;
        int difficulty = message.difficulty;

        GameController c = new GameController(this, difficulty);
    }
}
