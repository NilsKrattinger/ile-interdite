package ileinterdite.factory;

import ileinterdite.model.Card;
import ileinterdite.model.Cell;
import ileinterdite.model.FloodCard;
import ileinterdite.model.Grid;
import ileinterdite.util.Utils;

import java.util.Stack;

public class FloodCardFactory {

    public Stack<Card> floodCardFactory(Grid grid) {
        Cell[][] cells = new Cell[Grid.HEIGHT][Grid.WIDTH];
        Stack<Card> cards = new Stack<>();


        for (int i = 0; i < Grid.WIDTH; i++) {
            for (int j = 0; j < Grid.HEIGHT; j++) {
                if (!(cells[i][j].getState() == Utils.State.NON_EXISTENT)) {
                    cards.add(new FloodCard(cells[i][j], grid));
                    cards.lastElement().setCellName(cells[i][j].getName());
                }
            }
        }
        return cards;
    } // TODO DECK FACTORY
}