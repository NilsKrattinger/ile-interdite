package ileinterdite.controller;

import ileinterdite.view.WaterScaleView;

public class WaterScaleController {

    private WaterScaleView scaleView;
    private int waterScale; //< The number where the scale is at

    public WaterScaleController(GameController c, int startingScale) {
        waterScale = startingScale;
        scaleView = new WaterScaleView();
        scaleView.setScale(waterScale);
        c.getWindow().setWaterScaleView(scaleView);
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
        scaleView.setScale(waterScale);
    }

    private int getWaterScale() {
        return waterScale;
    }

    public boolean isDeadly() {
        return waterScale >= 10;
    }


}
