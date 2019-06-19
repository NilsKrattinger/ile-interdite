package ileinterdite.controller;


import ileinterdite.factory.BoardFactory;
import ileinterdite.factory.DeckFactory;
import ileinterdite.factory.DiscardPileFactory;
import ileinterdite.model.*;
import ileinterdite.model.Cell;
import ileinterdite.model.Deck;
import ileinterdite.model.DiscardPile;
import ileinterdite.model.Grid;
import ileinterdite.model.adventurers.Adventurer;
import ileinterdite.model.adventurers.Engineer;
import ileinterdite.model.adventurers.Messager;
import ileinterdite.model.adventurers.Navigator;
import ileinterdite.util.*;
import ileinterdite.util.Utils.Action;
import ileinterdite.view.AdventurerView;
import ileinterdite.view.GameView;
import ileinterdite.view.GridView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Controller implements IObserver<Message> {
    private ControllerMainMenu controllerMainMenu;

    private Grid grid;
    private Utils.State[][] cellStates;

    private ArrayList<Adventurer> players;
    private Adventurer currentAdventurer;
    private Adventurer currentActionAdventurer;
    private ArrayList<Adventurer> adventurersNeedRescue;

    private HashMap<Utils.CardType, Deck> decks;
    private HashMap<Utils.CardType, DiscardPile> discardPiles;

    private GameView mainView;
    private GridView gridView;
    private HashMap<Adventurer, AdventurerView> adventurerViews;
    private AdventurerView currentAdventurerView;

    // Turn state
    private Action selectedAction;
    private static final int NB_ACTIONS_PER_TURN = 3;
    private int remainingActions;

    //Rising scale
    private int risingScale;
    private boolean totalFlood;

    private boolean powerEngineer = false;

    public Controller(ControllerMainMenu cm) {
        adventurerViews = new HashMap<>();
        this.controllerMainMenu = cm;
        adventurersNeedRescue = new ArrayList<>();
        Object[] builtStuff;
        builtStuff = BoardFactory.boardFactory();
        this.mainView = new GameView(1280, 720);
        this.gridView = new GridView();
        this.mainView.setGridView(this.gridView);
        this.players = (ArrayList<Adventurer>) builtStuff[0];
        this.grid = new Grid((Cell[][]) builtStuff[1], (ArrayList<Treasure>) builtStuff[2]);
        this.definePlayer(players);

        for (Adventurer adv : players) {
            AdventurerView view = new AdventurerView(adv);
            adventurerViews.put(adv, view);
            view.addObserver(this);

            if (currentAdventurerView == null) {
                currentAdventurerView = view;
                this.mainView.setAdventurerView(currentAdventurerView);
            }
        }

        this.initCard(this.grid);
        this.initBoard();
        this.risingScale = 1;
        this.totalFlood = false;

        this.gridView.addObserver(this);
        this.gridView.showGrid(this.grid.getCells());
        this.gridView.showAdventurers(players);
        this.mainView.setVisible();

        this.nextAdventurer();
    }

    private void changeCurrentAdventurer() {
        players.add(players.remove(0));
        currentAdventurer = players.get(0);
        currentAdventurerView = adventurerViews.get(currentAdventurer);
        this.mainView.setAdventurerView(currentAdventurerView);
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
        gridView.showSelectableCells(cellStates);
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
        gridView.showSelectableCells(cellStates);
    }


    /**
     * Lance les actions pour le don d'une carte par l'aventurier adventurer (vérification qu'il y a
     * bien un autre aventurier sur sa tuile, et lancement du choix de la carte à donner
     * @param adventurer : aventurier initiant le don de carte
     */
    public void initGiveCard(Adventurer adventurer) {
        currentActionAdventurer = adventurer;
        Cell adventurerCell = grid.getCell(adventurer.getX(),adventurer.getY());
        int nbOfAdventurersOnCell = adventurerCell.getAdventurers().size();
        if (nbOfAdventurersOnCell >= 2 || adventurer instanceof Messager) {
            ArrayList<Card> giverCards = currentAdventurer.getCards();
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
            adventurer.getCards().add(card);
        }
    }

    /**
     *
     * @param adventurer
     * @param card
     */
    public void initDiscard(Adventurer adventurer, Card card) {
        ArrayList<Card> handCards = adventurer.getCards();
        adventurer.getHand().clearHand();
        if (card != null) {
            handCards.add(card);
        }
        //adventurerView.askCardToDiscard(handCards);
        // TODO method askCardToDiscard()
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
    public void movement(int x, int y) {
        currentActionAdventurer.move(x, y);
        gridView.updateAdventurer(currentActionAdventurer);
    }

    /**
     * Asséchement de la grille en X,Y et actualisation de la vue
     *
     * @param x
     * @param y
     */
    public void dry(int x, int y){
        this.getGrid().dry(x,y);
        gridView.updateCell(x, y, Utils.State.NORMAL);
    }

    /**
     *
     */
    public void discard(Card card, Adventurer adventurer) {
        DiscardPile discardTreasureCards;
        if (card instanceof TreasureCard) {
            discardTreasureCards = this.discardPiles.get(Utils.CardType.Treasure);
        } else {
            discardTreasureCards = this.discardPiles.get(Utils.CardType.Flood);
        }
        discardTreasureCards.addCard(card);
        adventurer.getCards().remove(card);
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
        }

        return null; // This should never happen, as coordinates are sent by components
    }

    private boolean validateCellAction(int x, int y) {
        if (x >= 0 && y >= 0 && x < Grid.WIDTH && y < Grid.HEIGHT && isCellAvailable(x, y)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean handleAction(String msg) {
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
                        return true;
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
                        return true;
                    }
                }
                break;
            case GIVE_CARD_CARD_CHOICE:
                if (msg != null) {
                    selectedCard = this.currentAdventurer.getHand().getCard(msg);
                    if (selectedCard != null) {
                        for (Adventurer adv : players) {
                            if (currentAdventurer instanceof Messager || (adv != currentAdventurer) && (adv.getY() == currentAdventurer.getY() && adv.getX() == currentAdventurer.getX())) {
                            //adventurerView.chooseCardReceiver();
                            // TODO chooseCardReceiver(adv) method
                        }
                        }
                    }
                }
                break;
            case GIVE_CARD_RECEIVER_CHOICE:
                if (msg != null) {
                    Adventurer receiver = this.getAdventurer(msg);
                    int nbOfCardsInReceiverHand = receiver.getNumberOfCards();
                    if (nbOfCardsInReceiverHand == 5 && selectedCard != null) {
                        //initDiscard(receiver,selectedCard);
                        // TODO method initDiscard() (already in feature-discard-treasure-cards)
                    } else {
                        if (selectedCard != null) {
                            currentAdventurer.getCards().remove(selectedCard);
                            giveCard(receiver,selectedCard);
                        }
                    }
                    reduceNbActions();
                }
                break;
            case GET_TREASURE:
                break;
            case RESCUE:
                if (coords != null) {
                    int x = coords.x - 1;
                    int y = coords.y - 1;
                    if (validateCellAction(x, y)) {
                        movement(x, y);
                    }
                }
                adventurersNeedRescue.remove(0);
                if (! adventurersNeedRescue.isEmpty()){
                    initRescue(adventurersNeedRescue.get(0));}

                break;
        }

        return false;
    }

    public void drawTreasureCards(int nbCard) {
        int maximumNbCardFromHand = 5;
        int nbCardsInHand = this.currentAdventurer.getNumberOfCards();
        ArrayList<Card> drawedCards = this.decks.get(Utils.CardType.Treasure).drawCards(nbCard);
        for (Card card : drawedCards) {
            if (card.getCardName() != "Montée des eaux") {
                this.currentAdventurer.getCards().add(card);
                nbCardsInHand++;
            } else {
                this.increaseRisingScale();
                if (!discardPiles.get(Utils.CardType.Flood).getCards().isEmpty()) {
                    this.discardPiles.get(Utils.CardType.Flood).shuffle();
                    ArrayList<Card> discardFloodCards = this.discardPiles.get(Utils.CardType.Flood).getCards();
                    this.decks.get(Utils.CardType.Flood).addAtTheTop(discardFloodCards);
                    this.discardPiles.get(Utils.CardType.Flood).clearPile();
                }
                this.discardPiles.get(Utils.CardType.Treasure).addCard(card);
            }
        }
        if (nbCardsInHand > maximumNbCardFromHand) {
            for (int i = 0; i < nbCardsInHand - maximumNbCardFromHand; i++) {
                initDiscard(this.currentAdventurer, (Card) null);
            }
        }
    }

    /**
     * Change state of card et put in the discard pile if needed.
     * @param nbCard
     */
    public void drawFloodCards(int nbCard) {
        ArrayList<Card> drawedCard = decks.get(Utils.CardType.Flood).drawCards(nbCard);
        Utils.State state;
        for (Card card : drawedCard) {
            FloodCard floodCard;
            floodCard = (FloodCard) card;
            Cell linkedCell = floodCard.getLinkedCell();
            state = linkedCell.getState();
            switch (state) {
                case NORMAL:
                    floodCard.getLinkedCell().setState(Utils.State.FLOODED);
                    discardPiles.get(Utils.CardType.Flood).addCard(floodCard);
                    break;
                case FLOODED:
                    floodCard.getLinkedCell().setState(Utils.State.SUNKEN);
                    adventurersNeedRescue.addAll(floodCard.getLinkedCell().getAdventurers());
                    break;
                case SUNKEN:
                    if (Parameters.LOGS){
                        System.out.println("Carte inondation : " + card.getCardName() + " Suprimée : Tuile deja coulée");
                    }
                    break;
                default:
                    throw new RuntimeException();
            }
            gridView.updateCell(linkedCell.getName(), linkedCell.getState());
        }
    }


    public void nextAdventurer() {
        changeCurrentAdventurer();
        currentAdventurer.newTurn();
        setNbActions(NB_ACTIONS_PER_TURN);
        selectedAction = null;
        this.testDefeat();

        gridView.newTurn();
    }

    /**
     * @param nb
     */
    public void setNbActions(int nb) {
        this.remainingActions = Math.max(nb, NB_ACTIONS_PER_TURN);
        currentAdventurerView.setNbActions(this.remainingActions);
    }

    public void reduceNbActions() {
        this.remainingActions--;
        currentAdventurerView.setNbActions(this.remainingActions);
    }

    /**
     * Get the adventurer with the given class name.
     *
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
    public void update(IObservable<Message> o, Message m) {
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
                    currentAdventurerView.showAdventurers(players);
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
                collectTreasure(currentAdventurer);
                break;
            case VALIDATE_ACTION:
                if (selectedAction != null && handleAction(m.message)) {
                    currentActionAdventurer = null;
                    selectedAction = null;
                }
                break;
            case END_TURN:
                this.endTurn();
                return;
            case CANCEL_ACTION:
                selectedAction = null;
                break;
            case DISCARD:
                this.discard(currentAdventurer.getHand().getCard(m.message), currentAdventurer);
                break;
        }

        if (remainingActions == 0 && (!powerEngineer || selectedAction != Action.DRY && selectedAction != null)) {
            endTurn();
        }
    }

    private void initRescue(Adventurer adventurer) {
        currentActionAdventurer = adventurer;
        selectedAction = Action.RESCUE;
        cellStates = adventurer.getRescueCells();
        if(avalibleOne(cellStates)) {
            Utils.showInformation("ATTENTION l'aventurier " + adventurer.getName() + " bois la tasse, Choisisez vite une case jusqu'a la quelle il va nager !");
            gridView.showSelectableCells(cellStates);
        } else {
            //TODO FONCTION PERDUUUUUUU T'es NULLLLL
        }

    }

    private boolean avalibleOne(Utils.State[][] cellStates) {
        boolean avalibleCell = false;
        int i = 0;
        while (i < Grid.HEIGHT && !avalibleCell) {
            int j = 0;
            while (j < Grid.WIDTH && !avalibleCell) {
                avalibleCell = cellStates[i][j] == Utils.State.ACCESSIBLE;
                j++;
            }
            i++;
        }
        return avalibleCell;
    }

    /**
     *  Vérifie que l'aventurier peut récupérer un trésor, puis si c'est le cas, retire le trésor de la liste des trésors
     *  non récupérés, puis défausse les cartes utilisées par l'aventurier pour récupérer le trésor dans la défausse des
     *  cartes trésors
     * @param adventurer
     */
    private void collectTreasure(Adventurer adventurer) {
        Treasure collectableTreasure = adventurer.isAbleToCollectTreasure();
        if (collectableTreasure != null) {
            String collectableTreasureName = collectableTreasure.getName();
            this.grid.getTreasures().remove(collectableTreasure);
            int discardedCards = 0;
            for (Card card : adventurer.getCards()) {
                if (card.getCardName().equals(collectableTreasureName) && discardedCards <4 ) {
                    this.discardPiles.get(Utils.CardType.Treasure).addCard(card);
                    adventurer.getCards().remove(card);
                    discardedCards++;
                }
            }
            reduceNbActions();
            // TODO : methode pour montrer à l'utilisateur que le trésor a bien été récupéré / update de sa main
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

        if(!Parameters.DEMOMAP){
            this.drawFloodCards(6);
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

    /**
     * declenche la victoire
     */
    private void victory() {
        //adventurerView.displayVictory()
        //TODO adventurerView.displayVictory()

        //this.endGame()
        //TODO this.endGame()
    }

    /**
     * declenche la défaite
     */
    private void defeat() {
        //adventurerView.displayDefeat()
        //TODO adventurerView.displayVictory()

        //this.endGame()
        //TODO this.endGame()
    }

    /**
     * Check if its dead to win
     */
    public void testDefeat() {
        if (totalFlood || this.treasureSink() || this.heliCellSink()) {
            this.defeat();
        }
    }

    /**
     * Check si les tresors restants ont coulés
     * @return
     */
    private boolean treasureSink() {
        ArrayList<Treasure> treasuresNotFound = this.getGrid().getTreasures();
        HashMap<Cell, Treasure> notFoundTreasureCells = new HashMap<>();
        for (Cell[] cells : getGrid().getCells()) {
            for (Cell cell : cells) {
                if (cell instanceof TreasureCell && cell.getState() != Utils.State.SUNKEN && treasuresNotFound.contains(((TreasureCell) cell).getTreasure())) {
                    notFoundTreasureCells.put(cell, ((TreasureCell) cell).getTreasure());
                }
            }
        }
        for (Treasure treasure : treasuresNotFound) {
            if (!notFoundTreasureCells.containsValue(treasure)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check si l'heliport a coulé
     * @return
     */
    private boolean heliCellSink() {
        Cell heliCell = null;
        for (Cell[] cells : getGrid().getCells()) {
            for (Cell cell : cells) {
                if (cell.getName() != null && cell.getName().equals("Heliport")) {
                    heliCell = cell;
                }
            }
        }
        return heliCell.getState() == Utils.State.SUNKEN;
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
     *
     * @param grid
     */
    private void initCard(Grid grid) {
        Deck deckTmp;
        DiscardPile discardPileTmp;
        this.decks = new HashMap<>();
        deckTmp = DeckFactory.deckFactory(Utils.CardType.Flood, grid);
        deckTmp.shuffle();
        decks.put(deckTmp.getCardType(), deckTmp);

        this.discardPiles = new HashMap<>();
        discardPileTmp = DiscardPileFactory.discardPileFactory(Utils.CardType.Flood);
        deckTmp.setDiscardPile(discardPileTmp);
        discardPiles.put(discardPileTmp.getCardType(), discardPileTmp);

        deckTmp = DeckFactory.deckFactory(Utils.CardType.Treasure, grid);
        deckTmp.shuffle();
        decks.put(deckTmp.getCardType(), deckTmp);

        discardPileTmp = DiscardPileFactory.discardPileFactory(Utils.CardType.Treasure);
        deckTmp.setDiscardPile(discardPileTmp);
        discardPiles.put(discardPileTmp.getCardType(), discardPileTmp);
    }

    public void endTurn() {
        this.drawTreasureCards(2);

        this.drawFloodCards(getFloodedCardToPick());
        if(!adventurersNeedRescue.isEmpty()){
            initRescue(adventurersNeedRescue.get(0));
        } else {
            this.nextAdventurer();
        }
    }
}
