// (YYYY/MM/DD) [AUTHOR] : MODIFICATION
// (2023/10/27) [SAMUEL GAUTHIER] : Added javadoc for the class Monstre
//

package com.example.tp2.entities;

import androidx.fragment.app.Fragment;

import com.example.tp2.activities.FightActivity;
import com.example.tp2.R;
import com.example.tp2.fragments.EnemyFragment;

import java.util.Scanner;

/**
 * Définition d'un monstre
*/
public abstract class MonstreEntity extends Entity {
    // region Race
    private String race;

    public String getRace() { return race; }

    public void setRace(String race) {
        this.race = race;
    }
    // endregion

    // region Is Helpful
    private boolean isHelpful;

    public boolean isHelpful() { return isHelpful; }

    public void setHelpful(boolean helpful) { isHelpful = helpful; }
    // endregion

    // region Enemy IDs
    public abstract int getPortraitID();
    public int getFrameID() { return R.drawable.enemy_frame_container; }

    // endregion

    // region Health
    @Override
    public void setHealth(int health) {
        super.setHealth(health);

        EnemyFragment f = (EnemyFragment) getDisplayFragment();

        if (f != null)
            f.setHealthLabel(getHealth() + "");
    }
    // endregion

    public MonstreEntity(String race, int munitions, int santé) {
        super(munitions, santé);
        setRace(race);
        setHelpful(FightActivity.Instance.RNG.nextBoolean());
    }

    public int attaque() {
        if (isHelpful()) {
            this.logMessage(getRace() + " healed the player 2 HP.");
            return -2;
        }

        if (getAmmoLeft() == 0) {
            this.logMessage(getRace() + " is out of ammos.");
            return 0;
        }

        int dmg = 1;
        removeAmmo(dmg);

        this.logMessage(getRace() + " did " + dmg + " damage(s).");

        return 1;
    }

    @Override
    public String obtenirÉtat() {
        return "\t" + this;
    }
}
