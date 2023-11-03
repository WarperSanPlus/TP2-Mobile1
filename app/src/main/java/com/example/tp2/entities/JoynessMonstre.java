package com.example.tp2.entities;

import com.example.tp2.R;

public class JoynessMonstre extends MonstreEntity {
    public JoynessMonstre(int munitions, int santé) {
        super("Joyness", munitions, santé);
    }

    @Override
    public int getPortraitID() {
        return R.drawable.joyness_monster;
    }

    @Override
    public int attaque() {
        logMessage(getRace() + " healed the player 2 HP.");
        return -2; // Always healing
    }
}
