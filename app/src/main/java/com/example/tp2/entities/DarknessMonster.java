// (YYYY/MM/DD) [AUTHOR] : MODIFICATION
//

package com.example.tp2.entities;

import com.example.tp2.R;

public class DarknessMonster extends Monstre {

    @Override
    public int getPortraitID() {
        return R.drawable.e_lostworld_9;
    }

    @Override
    public int getFrameID() {
        return R.drawable.enemy_frame_container;
    }

    public DarknessMonster(String race, String nom, int munitions, int santé) {
        super(race, nom, munitions, santé);
    }
}
