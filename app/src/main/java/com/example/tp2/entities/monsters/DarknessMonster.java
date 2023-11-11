package com.example.tp2.entities.monsters;

import com.example.tp2.R;

public class DarknessMonster extends MonstreEntity {
    @Override
    public int getPortraitID() {
        return R.drawable.e_lostworld_9;
    }

    public DarknessMonster(int munitions, int santé) {
        super("Darkness", munitions, santé);
    }
}
