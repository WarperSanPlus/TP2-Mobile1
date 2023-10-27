// (YYYY/MM/DD) [AUTHOR] : MODIFICATION
// (2023/10/27) [SAMUEL GAUTHIER] : Added javadoc for the class Monstre
//

package com.example.tp2.entities;

import androidx.annotation.NonNull;

import com.example.tp2.FightActivity;

import java.util.Scanner;

/**
 * Définition d'un monstre
*/
public abstract class Monstre extends Personnage {
    public abstract int getPortraitID();
    public abstract int getFrameID();

    private String race;
    private boolean EstGentil;

    public String getRace() {
        return race;
    }

    public void setRace(String race) {
        this.race = race;
    }

    public boolean getEstGentil() {
        return EstGentil;
    }

    public void setEstGentil(boolean EstGentil) {
        this.EstGentil = EstGentil;
    }

    public Monstre(String race, String nom, int munitions, int santé) {
        super(nom, munitions, santé);
        setRace(race);
        setEstGentil(FightActivity.RNG.nextBoolean());
    }

    @Override
    public int attaque(Scanner reader) {
        //afficherÉtat();
        if (EstGentil) {
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

    @NonNull
    @Override
    public String toString() {
        return getName() + " " + (getEstGentil() ? "Gentil" : "Méchant") + " " + getRace()
                + " avec une santé de " + getHealth()
                + " et " + getAmmoLeft() + " munitions";
    }
}
