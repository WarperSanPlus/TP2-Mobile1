package com.example.tp2.entities.monsters;

import com.example.tp2.R;

public class ThreeTreeMonster extends MonstreEntity {
    public ThreeTreeMonster(int munitions, int santé) {
        super("Three Tree", munitions, santé);
    }

    @Override
    public int getPortraitID() {
        return R.drawable.three_tree_monster;
    }
}
