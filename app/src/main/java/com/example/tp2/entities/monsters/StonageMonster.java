package com.example.tp2.entities.monsters;

import com.example.tp2.R;

public class StonageMonster extends MonstreEntity {
    private int attackCount = 0;

    public StonageMonster(int munitions, int santé) {
        super("Stonage", munitions, santé);
    }

    @Override
    public int getPortraitID() {
        return R.drawable.stonage_monster;
    }

    // 5 => 10 => 15 => 20 => 25 => 0 => 0 => 0 => 5 => ...
    @Override
    public int attaque() {
        int amount = 5 * (attackCount + 1);

        attackCount++;

        if (attackCount > 5) {
            attackCount = -3;
        }

        if (attackCount < 0) {
            amount = 0;
        }
        return amount;
    }

    @Override
    protected boolean initialHelpfulRoll() {
        return false; // Always bad
    }
}
