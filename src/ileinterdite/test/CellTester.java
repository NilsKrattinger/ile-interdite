/*package ileinterdite.test;

import ileinterdite.model.Cell;
import ileinterdite.model.adventurers.Adventurer;
import ileinterdite.model.adventurers.Navigator;
import ileinterdite.util.Utils;
import org.testng.Assert;
import org.testng.annotations.Test;

public class CellTester {
    @Test
    public void testAdventurer() {
        Adventurer adv = new Navigator();
        Cell cell = new Cell();

        Assert.assertFalse(cell.isAdventurerOnCell(adv));
        cell.addAdventurer(adv);
        Assert.assertTrue(cell.isAdventurerOnCell(adv));
    }

    @Test
    public void testState() {
        Cell cell = new Cell();

        Assert.assertEquals(Utils.State.NORMAL, cell.getState());
        cell.setState(Utils.State.SUNKEN);
        Assert.assertEquals(Utils.State.SUNKEN, cell.getState());
    }
}
*/