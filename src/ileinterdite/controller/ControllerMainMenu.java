package ileinterdite.controller;

import ileinterdite.util.IObservable;
import ileinterdite.util.IObserver;
import ileinterdite.view.AdventurerView;
import ileinterdite.view.GridView;

import java.util.ArrayList;

public class ControllerMainMenu implements IObserver<ArrayList<String>> {

    private ArrayList<String> playerName = new ArrayList<>();

    public ArrayList<String> getPlayersName() {
        return playerName;
    }

    @Override
    public void update(IObservable<ArrayList<String>> o, ArrayList<String> message) {
        playerName = message;

        GridView gridView = new GridView();
        Controller c = new Controller(this, gridView);

        gridView.addObserver(c);
        gridView.setVisible();
    }
}
