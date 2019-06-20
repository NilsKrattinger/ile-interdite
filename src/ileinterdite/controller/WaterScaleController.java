package ileinterdite.controller;

import ileinterdite.view.WaterScaleView;

public class WaterScaleController {

    private GameController controller;
    private WaterScaleView scaleView;
    private int waterScale; //< The number where the scale is at

    public WaterScaleController(GameController c, int startingScale) {
        this.controller = c;
        waterScale = startingScale;
        scaleView = new WaterScaleView();
        scaleView.setScale(waterScale);
        controller.getWindow().setWaterScaleView(scaleView);
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

    public int getWaterScale() {
        return waterScale;
    }

    public boolean isDeadly() {
        return waterScale >= 10;
    }



}
