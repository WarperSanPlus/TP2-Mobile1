// (YYYY/MM/DD) [AUTHOR] : MODIFICATION
// (2023/10/27) [SAMUEL GAUTHIER] : Added javadoc for the class Monstre
//

package com.example.tp2.entities.monsters;

import androidx.fragment.app.Fragment;

import com.example.tp2.activities.FightActivity;
import com.example.tp2.R;
import com.example.tp2.entities.Entity;
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

    private void setHelpful(boolean helpful) { isHelpful = helpful; }
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
        setHelpful(initialHelpfulRoll());
    }

    public int attaque() {
        if (isHelpful())
            return onHelpfulAttack();
        if (getAmmoLeft() == 0)
            return 0;
        return 1;
    }

    // region Virtuals
    /**
     * Appelé quand le monstre est gentil et qu'il attaque
     * @return Nombre de dégâts à infliger
     */
    protected int onHelpfulAttack() { return -2; }

    /**
     * Appelé quand lorsque le monstre est créé afin de déterminer s'il est gentil ou non
     * @return Le monstre est-il gentil ?
     */
    protected boolean initialHelpfulRoll() { return FightActivity.Instance.RNG.nextBoolean(); }
    // endregion
}
