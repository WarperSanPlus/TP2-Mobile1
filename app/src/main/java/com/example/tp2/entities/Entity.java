package com.example.tp2.entities;

import androidx.annotation.NonNull;

import com.example.tp2.interfaces.IJeu;

import java.io.Serializable;
import java.util.UUID;

public abstract class Entity implements IJeu, Serializable {
    // region UUID
    private String _UUID = UUID.randomUUID().toString();
    public String getUUID() { return _UUID; }

    // endregion
    // region Ammo Left
    private int ammoLeft;

    public int getAmmoLeft() {
        return ammoLeft;
    }

    public void setAmmoLeft(int ammoLeft) {
        this.ammoLeft = ammoLeft;
    }

    // endregion
    // region Health
    private int health;

    public int getHealth() {
        return health;
    }

    public void setHealth(int health) {
        this.health = health;
    }

    public boolean isAlive() { return getHealth() > 0; }

    public void takeDamage(int ammount) { setHealth(getHealth() - ammount); }
    // endregion

    public Entity(int ammoLeft, int health) {
        setAmmoLeft(ammoLeft);
        setHealth(health);
    }

    // region Override
    @NonNull
    @Override
    public String toString() {
        return super.toString() + "(" + _UUID + ")";
    }
    // endregion
}
