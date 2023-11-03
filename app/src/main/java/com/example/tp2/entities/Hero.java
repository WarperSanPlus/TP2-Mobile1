// (YYYY/MM/DD) [AUTHOR] : MODIFICATION
// (2023/10/27) [SAMUEL GAUTHIER] : Added javadoc for the class Hero
//

package com.example.tp2.entities;

import androidx.annotation.NonNull;

import com.example.tp2.fragments.HeroStatFragment;

/**
 * Déifinition d'un héro
*/
public class Hero extends Entity {
    // region Nom
    private String name;

    public String getName() { return name; }

    public void setName(String name) { this.name = name; }
    // endregion

    // region Ammo Fragment
    private HeroStatFragment AmmoStatFragment;
    public void setAmmoStatFragment(HeroStatFragment ammoStatFragment) { AmmoStatFragment = ammoStatFragment; }
    // endregion

    public Hero(String nom, int munitions, int santé) {
        super(munitions, santé);
        setName(nom);
    }

    public double obtenirChanceAttaquer() {
        return 0.5;
    }

    public boolean canPlay() { return getAmmoLeft() > 0; }

    // region Override
    @Override
    public String obtenirÉtat() {
        return this.toString();
    }

    @NonNull
    @Override
    public String toString() {
        return getName() + " a " + getAmmoLeft() + " munitions et une santé de " + getHealth();
    }

    @Override
    public void takeDamage(int ammount) {
        super.takeDamage(ammount);

        HeroStatFragment hhf = (HeroStatFragment) getDisplayFragment();

        if (hhf == null)
            return;

        hhf.updateStatLabel(getHealth() + "");
    }

    @Override
    public void removeAmmo(int amount) {
        super.removeAmmo(amount);

        if (AmmoStatFragment == null)
            return;

        AmmoStatFragment.updateStatLabel(getAmmoLeft() + "");
    }

    // endregion
}