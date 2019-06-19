package ileinterdite.controller;

public class WaterScaleController {

    private int waterScale; //< The number where the scale is at

    public WaterScaleController(int startingScale) {
        waterScale = startingScale;
    }

    public int getFloodedCardToPick() {
        if (getWaterScale() <= 2) {
            return 2;
        } else if (getWaterScale() <= 5) {
            return 3;
        } else if (getWaterScale() <= 7) {
            return 4;
        } else {
            return 5;
        }
    }

    public void increaseWaterScale() {
        waterScale++;
    }

    public int getWaterScale() {
        return waterScale;
    }

    public boolean isDeadly() {
        return waterScale >= 10;
    }

}
