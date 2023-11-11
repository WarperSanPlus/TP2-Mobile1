package com.example.tp2.entities.monsters;

import com.example.tp2.R;
import com.example.tp2.activities.FightActivity;

public class DragonCulaMonster extends MonstreEntity {
    public DragonCulaMonster(int munitions, int santé) {
        super("Dragon Cula", munitions, santé);
    }

    @Override
    public int getPortraitID() {
        return R.drawable.dragon_cula_monster;
    }

    @Override
    public int attaque() { return randomBetween(1, 10); }

    @Override
    protected boolean initialHelpfulRoll() {
        return false; // Always bad
    }
}
