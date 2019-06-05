package ileinterdite.controller;

import ileinterdite.view.AdventurerView;
import ileinterdite.view.GridView;

import java.util.ArrayList;
import java.util.Observable;
import java.util.Observer;

public class ControllerMainMenu implements Observer {

    ArrayList<String> playerName = new ArrayList<>();

    public ArrayList<String> getPlayersName() {
        return playerName;
    }

    @Override
    public void update(Observable observable, Object o) {
        playerName = (ArrayList<String>) o;

        AdventurerView adventurerView = new AdventurerView();
        GridView gridView = new GridView();
        Controller c = new Controller(this, adventurerView, gridView, 4);
        adventurerView.addObserver(c);
        adventurerView.setVisible();

        gridView.addObserver(c);
        gridView.setVisible();
    }
}
