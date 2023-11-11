package com.example.tp2.entities;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.tp2.Logger;
import com.example.tp2.R;
import com.example.tp2.activities.FightActivity;
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

    public void setAmmoLeft(int ammoLeft) { this.ammoLeft = ammoLeft; }

    public void removeAmmo(int amount) { setAmmoLeft(getAmmoLeft() - amount); }

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

    // region Display Fragment
    private Fragment DisplayFragment;

    public void setDisplayFragment(Fragment frag) { DisplayFragment = frag; }
    public Fragment getDisplayFragment() { return DisplayFragment; }

    // endregion

    // region Log
    public void logMessage(String message) {
        Logger.log(message);

        if (FightActivity.Instance != null)
            FightActivity.Instance.addMessage(message, this, R.drawable.levelup_particle_04);
    }
    // endregion

    public Entity(int ammoLeft, int health) {
        setAmmoLeft(ammoLeft);
        setHealth(health);
    }

    protected int randomBetween(int min, int max) {
        return FightActivity.RNG.nextInt(max) + min;
    }

    // region Override
    @NonNull
    @Override
    public String toString() {
        return super.toString() + "(" + _UUID + ")";
    }
    // endregion
}
