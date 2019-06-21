package ileinterdite.controller;

import ileinterdite.factory.BoardFactory;
import ileinterdite.util.IObservable;
import ileinterdite.util.IObserver;
import ileinterdite.util.Parameters;
import ileinterdite.util.StartMessage;
import ileinterdite.view.PlayerSelectionView;

import java.awt.*;
import java.util.ArrayList;

public class MainMenuController implements IObserver<StartMessage> {

    private ArrayList<String> playerName = new ArrayList<>();
    private Boolean playerSlection = false;
    private int difficulty = 0;

    public ArrayList<String> getPlayersName() {
        return playerName;
    }

    @Override
    public void update(IObservable<StartMessage> o, StartMessage message) {
        BoardFactory.initBoardFactory();
        if(!playerSlection) {
            playerName = message.playerName;
            Parameters.LOGS = message.logOption;
            Parameters.DEMOMAP = message.demoOption;
            Parameters.RANDOM = message.randomOption;
            difficulty = message.difficulty;
            if (!Parameters.RANDOM) {
                new PlayerSelectionView(BoardFactory.getAdventurers(), playerName.size(),this);
                playerSlection = true;
            } else {
                GameController c = new GameController(this, difficulty);
            }
        } else {
            System.out.println("YASSS");
            BoardFactory.setAdventurers(message.adventurers);
            GameController c = new GameController(this, difficulty);
        }









    }

    public void startGame(){

    }

    public void startPlayerChoice(){

    }
}
