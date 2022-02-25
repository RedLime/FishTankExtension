package com.redlimerl.mcsr.fishtank;

import com.redlimerl.speedrunigt.api.SpeedRunIGTApi;
import com.redlimerl.speedrunigt.timer.running.RunCategory;

public class FishTankImpl implements SpeedRunIGTApi {
    @Override
    public RunCategory registerCategory() {
        return FishTank.FISH_TANK_CATEGORY;
    }
}
