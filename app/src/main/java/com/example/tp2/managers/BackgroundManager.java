package com.example.tp2.managers;

import com.example.tp2.R;

public class BackgroundManager {
    public enum World {
        DESERT,
        FOREST,
        VOLCANO
    }

    public static int getBackgroundImage(World world, boolean isMapBG) {
        switch (world){
            case DESERT:
                return isMapBG ? R.drawable.background_desert : R.drawable.bg_game_desert;
            case VOLCANO:
                return isMapBG ? R.drawable.background_volcano : R.drawable.bg_game_volcano;
            default:
                return isMapBG ? R.drawable.background_forest : R.drawable.bg_game_forest;
        }
    }
}
