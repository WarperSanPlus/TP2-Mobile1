/* AUTHEUR: SAMUEL GAUTHIER
 * EXPLICATION:
 *      Classe décrivant ce que définit un personnage.
 *      Un personnage implémente l'interface IJeu pour pouvoir
 *      intéragir dans le jeu. Il comporte un nom, un nombre de munitions
 *      et une santé.
 */
package com.example.tp2.entities;

import com.example.tp2.interfaces.IJeu;

public abstract class Personnage implements IJeu {
    private String name;
    private int ammoLeft;
    private int health;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAmmoLeft() {
        return ammoLeft;
    }

    public void setAmmoLeft(int ammoLeft) {
        this.ammoLeft = ammoLeft;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean EstVivant() {
        return getHealth() > 0;
    }

    public Personnage(String name, int ammoLeft, int health) {
        setName(name);
        setAmmoLeft(ammoLeft);
        setHealth(health);
    }
}
