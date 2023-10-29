package com.example.tp2.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.example.tp2.AudioManager;
import com.example.tp2.Logger;
import com.example.tp2.R;
import com.example.tp2.entities.Hero;
import com.example.tp2.entities.MonstreEntity;
import com.example.tp2.fragments.AmmoFragment;
import com.example.tp2.fragments.EnemyFragment;
import com.example.tp2.fragments.HeroHealthFragment;

import java.util.ArrayList;
import java.util.Random;

public class FightActivity extends AppCompatActivity {
    public static FightActivity Instance;
    public static Random RNG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fight);

        Instance = this;

        loadBundles(getIntent().getExtras());
    }

    private void loadBundles(Bundle bundle) {
        loadAmmos(bundle);
        loadMonstres(bundle);
        loadBackground(bundle);
        loadHero(bundle);
    }

    private void ajouterFragment(int containerViewId, Fragment frag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(containerViewId, frag).commit();
    }

    // region Monstres
    private ArrayList<MonstreEntity> MonstresVivants = new ArrayList<>();
    private ArrayList<View> monstersViewRows = new ArrayList<>();
    public static final String MONSTRES_ARRAY_NAME = "monstresArray";

    private void loadMonstres(Bundle bundle){
        MonstresVivants = bundle == null ?
                new ArrayList<>() :
                (ArrayList<MonstreEntity>) bundle.getSerializable(MONSTRES_ARRAY_NAME);

        if (MonstresVivants == null) {
            Logger.log("Aucun monstre n'a été chargés.");
            return;
        }

        for (MonstreEntity mons : MonstresVivants) {
            addMonster(mons);
        }
    }

    private void addMonster(MonstreEntity monstre) {
        // Créer le fragment pour le monstre
        EnemyFragment enemyFragment = EnemyFragment.newInstance(monstre);
        monstre.setMonstreFragment(enemyFragment);

        ajouterFragment(R.id.fightActivity_rowPlaceholder, enemyFragment);

        Logger.log("Monstre '" + monstre + "' ajouté !");
    }

    private void killMonster(int index) {
        int lastIndex = MonstresVivants.size() - 1;

        MonstreEntity monstreMort = MonstresVivants.get(index);

        // Mettre le monstre touché à la dernière position
        MonstresVivants.set(index, MonstresVivants.get(lastIndex));
        MonstresVivants.set(lastIndex, monstreMort);

        // Créer la transaction pour supprimer des monstres
        FragmentTransaction removeFragmentTransaction = getSupportFragmentManager().beginTransaction();

        removeFragmentTransaction.remove(monstreMort.getMonstreFragment());

        removeFragmentTransaction.commit();

        // Supprimer le monstre
        MonstresVivants.remove(MonstresVivants.size() - 1);
    }

    // TODO : Row management
    // Add view row
    // Remove view row
    // Move monsters in view rows

    // endregion

    // region Ammo
    public static final String AMMO_OPTIONS_NAME = "ammoOptions";

    private AmmoFragment[] ammoFragments;
    private void loadAmmos(Bundle bundle) {
        byte[] options = bundle.getByteArray(AMMO_OPTIONS_NAME);

        ammoFragments = new AmmoFragment[options.length];

        for (int i = 0; i < options.length; i++) {
            AmmoFragment newFrag = AmmoFragment.newInstance(options[i]);

            ajouterFragment(R.id.fightActivity_ammoLayout, newFrag);
            ammoFragments[i] = newFrag;
        }
    }

    private void setEnableAmmos(boolean isEnabled) {
        int maxAmount = HeroCharacter.getAmmoLeft();

        for (AmmoFragment frag : ammoFragments) {
            frag.setEnableFragment(isEnabled && frag.getAmmoAmount() <= maxAmount);
        }
    }

    public void useAmmo(byte amount) {
        if (MonstresVivants.size() == 0)
            return;

        setEnableAmmos(false);

        int ammoUsed = 0;

        for (int i = 0; i < amount; i++) {
            if (HeroCharacter.obtenirChanceAttaquer() < RNG.nextDouble())
                continue;

            // Nombre aléatoire [0; MonstresVivatns.Length - successCount]
            int rdmIndex = RNG.nextInt(MonstresVivants.size());

            MonstreEntity monstreTouche = MonstresVivants.get(rdmIndex);

            monstreTouche.takeDamage(1);

            Logger.log("Le monstre '" + monstreTouche + "' a été touché !");

            ammoUsed++;

            // Si le monstre n'est pas mort, continue
            if (monstreTouche.isAlive())
                continue;

            killMonster(rdmIndex);

            if (MonstresVivants.size() == 0)
                break;
        }

        // TODO : Quit battle once no monster alive

        if (ammoUsed == 0)
            AudioManager.playUrlAudio("https://sounds.pond5.com/weapons-bullet-large-whoosh-miss-sound-effect-152700983_nw_prev.m4a", true, null);
        else
        {
            HeroCharacter.setAmmoLeft(HeroCharacter.getAmmoLeft() - ammoUsed);
        }

        // TODO : Make the monsters play

        setEnableAmmos(true);
    }
    // endregion

    // region Background
    public static final String BACKGROUND_NAME = "bgGameId";

    private void loadBackground(Bundle bundle) {
        ImageView bg = findViewById(R.id.fightActivity_backgroundImg);
        bg.setImageResource(bundle.getInt(BACKGROUND_NAME, R.drawable.bg_game_forest));
    }
    // endregion

    // region Hero stats
    public static final String HERO_OBJECT_NAME = "heroCharacter";

    private Hero HeroCharacter;
    private HeroHealthFragment heroHealthFragment;

    private void loadHero(Bundle bundle) {
        HeroCharacter = (Hero) bundle.getSerializable(HERO_OBJECT_NAME);

        heroHealthFragment = HeroHealthFragment.newInstance(HeroCharacter.getHealth() + "");

        ajouterFragment(R.id.fightActivity_playerStatsLayout, heroHealthFragment);
    }

    private void damageHero(int amount) {
        HeroCharacter.takeDamage(amount);

        heroHealthFragment.updateHealthLabel(HeroCharacter.getHealth() + "");
    }
    // endregion
}