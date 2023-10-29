package com.example.tp2.entities;

import com.example.tp2.R;

public class JoynessMonstre extends MonstreEntity {
    public JoynessMonstre(int munitions, int santé) {
        super("Joyness", munitions, santé);
    }

    @Override
    public int getPortraitID() {
        return R.drawable.ed5old;
    }
}
