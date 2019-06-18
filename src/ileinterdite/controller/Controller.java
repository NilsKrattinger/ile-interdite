package ileinterdite.controller;

import ileinterdite.factory.BoardFactory;
import ileinterdite.factory.DeckFactory;
import ileinterdite.factory.DiscardPileFactory;
import ileinterdite.model.Cell;
import ileinterdite.model.Deck;
import ileinterdite.model.DiscardPile;
import ileinterdite.model.Grid;
import ileinterdite.model.Card;
import ileinterdite.model.adventurers.Adventurer;
import ileinterdite.model.adventurers.Engineer;
import ileinterdite.model.adventurers.Navigator;
import ileinterdite.util.Message;
import ileinterdite.util.Tuple;
import ileinterdite.util.Utils;
import ileinterdite.util.Utils.Action;
import ileinterdite.util.Utils.Pawn;
import ileinterdite.view.AdventurerView;
import ileinterdite.view.GridView;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller implements Observer {
    private ControllerMainMenu controllerMainMenu;

    private Grid grid;
    private Utils.State[][] cellStates;

    private ArrayList<Adventurer> players;
    private Adventurer currentAdventurer;
    private Adventurer currentActionAdventurer;

    private HashMap<Utils.CardType, Deck> decks;
    private HashMap<Utils.CardType, DiscardPile> discardPiles;

    private GridView gridView;
    private AdventurerView adventurerView;

    // Turn state
    private Action selectedAction;
    private static final int NB_ACTIONS_PER_TURN = 3;
    private int remainingActions;

    //Rising scale
    private int risingScale;
    private boolean totalFlood;

    private boolean powerEngineer = false;

    public Controller(ControllerMainMenu cm, AdventurerView view, GridView gview, int nbPlayers) {
        this.controllerMainMenu = cm;

        Object[] builtStuff;
        builtStuff = BoardFactory.boardFactory();
        this.adventurerView = view;
        this.gridView = gview;
        this.players = (ArrayList<Adventurer>) builtStuff[0];
        this.grid = new Grid((Cell[][]) builtStuff[1], null);
        this.definePlayer(players);
        this.initCard(this.grid);
        this.initBoard();

        this.risingScale = 1;
        this.totalFlood = false;

        this.nextAdventurer();
    }

       private void changeCurrentAdventurer() {
        players.add(players.remove(0));
        currentAdventurer = players.get(0);
        Pawn currentPawn = currentAdventurer.getPawn();
        adventurerView.setColor(currentPawn.getColor(), currentPawn.getTextColor());
        adventurerView.setText(currentAdventurer.getName(), currentAdventurer.getClassName());
    }

    /**
     * Lance les actions pour le deplacement de l'aventurier.
     * puis l'interaction avec l'interface
     *
     * @param adventurer
     */
    public void initMovement(Adventurer adventurer) {
        currentActionAdventurer = adventurer;
        cellStates = adventurer.getAccessibleCells();
        gridView.showSelectableCells(cellStates, grid, new Tuple<>(adventurer.getX(), adventurer.getY()));
    }

    /**
     * Lance les actions pour le deplacement de l'aventurier.
     * puis l'interaction avec l'interface
     *
     * @param adventurer
     */
    public void initDryable(Adventurer adventurer) {
        currentActionAdventurer = adventurer;
        cellStates = adventurer.getDryableCells();
        gridView.showSelectableCells(cellStates, grid, new Tuple<>(adventurer.getX(), adventurer.getY()));
    }


    /**
     *  Lance les actions pour le don d'une carte par l'aventurier adventurer (vérification qu'il y a
     *  bien un autre aventurier sur sa tuile, et lancement du choix de la carte à donner
     * @param adventurer : aventurier initiant le don de carte
     */
    public void initGiveCard(Adventurer adventurer) {
        currentActionAdventurer = adventurer;
        Cell adventurerCell = grid.getCell(adventurer.getX(),adventurer.getY());
        int nbOfAdventurersOnCell = adventurerCell.getAdventurers().size();
        if (nbOfAdventurersOnCell >= 2) {
            ArrayList<Card> giverCards = currentAdventurer.getHand().getCards();
            //adventurerView.showTradableCards(giverCards);
            // TODO showTradableCards() method
        }
    }

    /**
     * Ajoute la carte card à la main de aventurier adventurer s'il a moins de 5 cartes dans sa main
     * @param adventurer
     * @param card
     */
    public void giveCard(Adventurer adventurer, Card card) {
        if (adventurer != null && card != null && adventurer.getNumberOfCards()<5) {
            adventurer.getHand().getCards().add(card);
        }
    }

    /**
     * Renvoie un boolean si la case choisie par l'utilisateur est accesible
     *
     * @param x,y
     * @return boolean
     */
    public boolean isCellAvailable(int x, int y) {
        return cellStates[y][x] == Utils.State.ACCESSIBLE;
    }

    /**
     * @param x
     * @param y deplacement de l'avanturier en X,Y et actualisation de la vue
     */
    public void movement(int x, int y){
        currentActionAdventurer.move(x,y);
        gridView.updateAdventurer(currentActionAdventurer);
    }

    /**
     * Asséchement de la grille en X,Y et actualisation de la vue
     *
     * @param x
     * @param y
     */
    public void dry(int x, int y) {
        this.getGrid().dry(x, y);
        gridView.updateDriedCell(x, y);
    }

    /**
     * Split the message contents to retrieve the position
     *
     * @param msg Message The message received from the view
     * @return Tuple&lt;Integer, Integer&gt;
     */
    private Tuple<Integer, Integer> getPositionFromMessage(String msg) {
        Pattern p = Pattern.compile("(\\d).+(\\d)");
        Matcher m = p.matcher(msg);
        if (m.find()) {
            int x = Integer.valueOf(m.group(1));
            int y = Integer.valueOf(m.group(2));
            return new Tuple<>(x, y);
        } else {
            Utils.showInformation("Les coordonnées entrées sont incorrectes.");
            return null;
        }
    }

    private boolean validateCellAction(int x, int y) {
        if (x >= 0 && y >= 0 && x < Grid.WIDTH && y < Grid.HEIGHT && isCellAvailable(x, y)) {
            return true;
        } else {
            Utils.showInformation("Les coordonnées sont invalides.");
            return false;
        }
    }

    public void handleAction(String msg) {
        Tuple<Integer, Integer> coords = getPositionFromMessage(msg);
        Card selectedCard = null;
        switch (selectedAction) {
            case MOVE:
                if (coords != null) {
                    int x = coords.x - 1;
                    int y = coords.y - 1;
                    if (validateCellAction(x, y)) {
                        movement(x, y);
                        reduceNbActions();
                        powerEngineer = false;
                    }
                }
                break;
            case DRY:
                if (coords != null) {
                    int x = coords.x - 1;
                    int y = coords.y - 1;
                    if (validateCellAction(x, y)) {
                        dry(x, y);
                        if (!powerEngineer) {
                            reduceNbActions();
                            if (currentActionAdventurer instanceof Engineer) {
                                powerEngineer = true;
                            }
                        } else {
                            powerEngineer = false;
                        }
                    }
                }
                break;
            case GIVE_CARD_CARD_CHOICE:
                if (msg != null) {
                    selectedCard = this.currentAdventurer.getHand().getCard(msg);
                    if (selectedCard != null) {
                        //adventurerView.chooseCardReceiver();
                        // TODO chooseCardReceiver() method
                    }
                }
                break;
            case GIVE_CARD_RECEIVER_CHOICE:
                if (msg != null) {
                    Adventurer receiver = this.getAdventurer(msg);
                    int nbOfCardsInReceiverHand = receiver.getNumberOfCards();
                    if (nbOfCardsInReceiverHand == 5 && selectedCard != null) {
                        //initDiscard(receiver,selectedCard);
                        // TODO method initDiscard() (already in feature-discard-treasure-cards
                    } else {
                        if (selectedCard != null) {
                            currentAdventurer.getHand().getCards().remove(selectedCard);
                            giveCard(receiver,selectedCard);
                        }
                    }
                    reduceNbActions();
                }
                break;
            case GET_TREASURE:
                break;
        }
    }

    public void drawTreasureCards() {

    }

    public void drawFloodCards() {

    }

    public void nextAdventurer() {
        changeCurrentAdventurer();
        currentAdventurer.newTurn();
        setNbActions(NB_ACTIONS_PER_TURN);
        selectedAction = null;
    }

    /**
     * @param nb
     */
    public void setNbActions(int nb) {
        this.remainingActions = Math.max(nb, NB_ACTIONS_PER_TURN);
    }

    public void reduceNbActions() {
        this.remainingActions--;
    }

    /**
     * Get the adventurer with the given class name.
     * @return Null if not found
     */
    private Adventurer findAdventurerByClassName(String name) {
        Adventurer adv;
        int i = 0;
        do {
            adv = players.get(i);
            i++;
        } while (i < players.size() && !name.equalsIgnoreCase(adv.getClassName()));

        return (name.equalsIgnoreCase(adv.getClassName())) ? adv : null;
    }

    @Override
    public void update(Observable o, Object arg) {
        Message m = (Message) arg;
        switch (m.action) {
            case NAVIGATOR_CHOICE:
                selectedAction = Action.MOVE;
                // The message contains a string with the format "ClassName (PlayerName)"
                currentActionAdventurer = findAdventurerByClassName(m.message.substring(0, m.message.indexOf(' ')));
                if (currentActionAdventurer != null) {
                    initMovement(currentActionAdventurer);
                }
                break;
            case MOVE:
                if (currentAdventurer instanceof Navigator) {
                    adventurerView.showAdventurers(players);
                } else {
                    selectedAction = Action.MOVE;
                    initMovement(currentAdventurer);
                }
                break;
            case DRY:
                selectedAction = Action.DRY;
                initDryable(currentAdventurer);
                break;
            case START_GIVE_CARD:
                selectedAction = Action.START_GIVE_CARD;
                initGiveCard(currentAdventurer);
                break;
            case GET_TREASURE:
                selectedAction = Action.GET_TREASURE;
                break;
            case VALIDATE_ACTION:
                if (selectedAction != null) {
                    handleAction(m.message);
                    currentActionAdventurer = null;
                }
                selectedAction = null;
                break;
            case END_TURN:
                nextAdventurer();
                break;
            case CANCEL_ACTION:
                selectedAction = null;
                break;
        }

        if (remainingActions == 0 && (!powerEngineer || selectedAction != Action.DRY && selectedAction != null)) {
            nextAdventurer();
        }
    }

    private Grid getGrid() {
        return grid;
    }

    private void initBoard() {
        for (Adventurer adventurer : players) {
            adventurer.setGrid(this.grid);
        }
        Cell[][] cells = this.getGrid().getCells();
        for (int j = 0; j < Grid.HEIGHT; j++) {
            for (int i = 0; i < Grid.WIDTH; i++) {
                if (players.contains(cells[j][i].getAdventurerSpawn())) {
                    cells[j][i].spawnAdventurer(i, j);
                }
            }
        }
    }

    private void increaseRisingScale() {
        setRisingScale(getRisingScale() + 1);
        if (risingScale >= 10) {
            totalFlood = true;
        }
    }

    private int getFloodedCardToPick() {
        if (getRisingScale() <= 2) {
            return 2;
        } else if (getRisingScale() <= 5) {
            return 3;
        } else if (getRisingScale() <= 7) {
            return 4;
        } else {
            return 5;
        }
    }

    public int getRisingScale() {
        return risingScale;
    }

    public void setRisingScale(int risingScale) {
        this.risingScale = risingScale;
    }

    private ArrayList<Adventurer> randomPlayer(ArrayList<Adventurer> players, int nbPlayers) {
        Collections.shuffle(players);
        while (players.size() > nbPlayers) {
            players.remove(players.size() - 1);
        }
        return players;
    }

    public ArrayList<Adventurer> definePlayer(ArrayList<Adventurer> players) {

        ArrayList<String> playersName = controllerMainMenu.getPlayersName();

        players = randomPlayer(players, playersName.size());
        for (int i = 0; i < playersName.size(); i++) {
            players.get(i).setName(playersName.get(i));
        }
        return players;
    }


    public Adventurer getAdventurer(String adventurerName) {
        for (Adventurer adventurer : this.getPlayers()) {
            if (adventurer.getName().equals(adventurerName)) {
                return adventurer;
            }
        }
        return null;
    }

    public ArrayList<Adventurer> getPlayers() {
        return this.players;
    }
    /**
     * Create all of the deck and cards
     * @param grid
     */
    private void initCard(Grid grid) {
        Deck deckTmp;
        DiscardPile discardPileTmp;
        this.decks = new HashMap<>();
        deckTmp = DeckFactory.deckFacoty(Utils.CardType.Flood,grid);
        decks.put(deckTmp.getCardType(),deckTmp);

        this.discardPiles = new HashMap<>();
        discardPileTmp = DiscardPileFactory.discardPileFactory(Utils.CardType.Flood);
        discardPiles.put(discardPileTmp.getCardType(),discardPileTmp);


        //TODO IMPLEMENT TREASURE CARD
    }
}
