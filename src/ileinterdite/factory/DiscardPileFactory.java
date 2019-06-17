package ileinterdite.factory;
import ileinterdite.model.*;
import ileinterdite.util.Utils;

public class DiscardPileFactory {

    public static DiscardPile discardPileFactory(Utils.CardType type) {
        return new DiscardPile(type);
    }


}

