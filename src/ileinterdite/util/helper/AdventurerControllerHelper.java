package ileinterdite.util.helper;

import ileinterdite.model.adventurers.Adventurer;
import ileinterdite.util.IObserver;
import ileinterdite.util.Message;
import ileinterdite.util.Parameters;
import ileinterdite.view.AdventurerView;
import ileinterdite.view.HandView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class AdventurerControllerHelper {
    private static HashMap<Adventurer, AdventurerView> adventurerViews; //< An association between adventurers and their view
    private static HashMap<Adventurer, HandView> adventurerHandViews; //< An association between adventurers and their view representing their hand

    /**
     * Get the list of adventurers with their name for the game
     * @param players The list of generated adventurers
     * @param names The names of the players
     * @return An ArrayList containing the correct number of adventurers with the right names
     */
    public static ArrayList<Adventurer> getPlayers(ArrayList<Adventurer> players, ArrayList<String> names) {
        randomPlayer(players, names.size());

        for (int i = 0; i < names.size(); i++) {
            players.get(i).setName(names.get(i));
        }
        return players;
    }

    /**
     * Shuffle the players and remove the unnecessary players
     * @param adventurers The list of adventurers to be shuffled
     * @param nbPlayers The number of players in the game
     */
    private static void randomPlayer(ArrayList<Adventurer> adventurers, int nbPlayers) {
        if (Parameters.RANDOM) {
            Collections.shuffle(adventurers);
        }
        while (adventurers.size() > nbPlayers) {
            adventurers.remove(adventurers.size() - 1);
        }
    }

    /**
     * Associate all adventurers with their corresponding view
     * @param adventurers The list of adventurers in the game
     * @param observer The observer that will listen to all interactions
     */
    public static void createViews(ArrayList<Adventurer> adventurers, IObserver<Message> observer) {
        adventurerViews = new HashMap<>();
        adventurerHandViews = new HashMap<>();

        for (Adventurer adv : adventurers) {
            HandView handview = new HandView(adv);
            adventurerHandViews.put(adv, handview);
            handview.addObserver(observer);

            AdventurerView view = new AdventurerView(adv, handview);
            adventurerViews.put(adv, view);
            view.addObserver(observer);
        }
    }

    /**
     * Get the link between adventurers and their view
     * @return A Hashmap linking adventurers with their views, with the observer listening to them
     */
    public static HashMap<Adventurer, AdventurerView> getAdventurerViews() {
        return adventurerViews;
    }

    /**
     * Get the link between adventurers and their hand view
     * @return A Hashmap linking adventurers with their views, with the observer listening to them
     */
    public static HashMap<Adventurer, HandView> getAdventurerHandViews() {
        return adventurerHandViews;
    }
}
