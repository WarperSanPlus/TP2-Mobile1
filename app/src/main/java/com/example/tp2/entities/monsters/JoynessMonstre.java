package com.example.tp2.entities.monsters;

import com.example.tp2.R;
import com.example.tp2.activities.FightActivity;

public class JoynessMonstre extends MonstreEntity {
    public JoynessMonstre(int munitions, int santé) {
        super("Joyness", munitions, santé);
    }

    @Override
    public int getPortraitID() {
        return R.drawable.joyness_monster;
    }

    @Override
    protected boolean initialHelpfulRoll() {
        return true; // Always helpful
    }

    @Override
    protected int onHelpfulAttack() {
        return -randomBetween(2, 4);
    }
}
