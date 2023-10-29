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
    // region Enemy Fragment
    public abstract int getPortraitID();
    public int getFrameID() { return R.drawable.enemy_frame_container; }

    private EnemyFragment MonstreFragment;
    public EnemyFragment getMonstreFragment() { return MonstreFragment; }
    public void setMonstreFragment(EnemyFragment monstreFragment) {
        MonstreFragment = monstreFragment;
        MonstreFragment.setHealthLabel(getHealth() + ""); // First update
    }
    // endregion
    // region Health
    @Override
    public void setHealth(int health) {
        super.setHealth(health);

        EnemyFragment f = getMonstreFragment();

        if (f != null)
            f.setHealthLabel(getHealth() + "");
    }
    // endregion

    public MonstreEntity(String race, int munitions, int santé) {
        super(munitions, santé);
        setRace(race);
        setHelpful(FightActivity.Instance.RNG.nextBoolean());
    }

    @Override
    public int attaque(Scanner reader) {
        //afficherÉtat();
        if (isHelpful()) {
            System.out.println("\t***** Attaque du Monstre:-->Héros soigné");
            return -2;
        }

        if (getAmmoLeft() == 0) {
            System.out.println("\t***** Attaque du Monstre:-->Héros non touché");
            return 0;
        }

        System.out.println("\t***** Attaque du Monstre:-->Héros blessé");
        setAmmoLeft(getAmmoLeft() - 1);
        return 1;
    }

    @Override
    public String obtenirÉtat() {
        return "\t" + this;
    }
}
