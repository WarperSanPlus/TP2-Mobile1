// (YYYY/MM/DD) [AUTHOR] : MODIFICATION
//

package com.example.tp2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.util.Log;

import com.example.tp2.entities.DarknessMonster;
import com.example.tp2.entities.Monstre;
import com.example.tp2.fragments.EnemyFragment;

import java.util.Random;

public class FightActivity extends AppCompatActivity {
    public static Random RNG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fight);

        RNG = new Random();

        // Load monsters
        // Add each monster to the layout

        DarknessMonster c = new DarknessMonster("A", "BOB", 1, 3);
        DarknessMonster d = new DarknessMonster("B", "MONSTRE 2", 2, 2);
        DarknessMonster e = new DarknessMonster("C", "MONSTRE 3", 3, 1);
        DarknessMonster f = new DarknessMonster("D", "MONSTRE 4", 3, 1);

        addMonster(c);
        addMonster(d);
        addMonster(e);
        addMonster(f);
    }

    private void addMonster(Monstre monstre) {
        ajouterFragment(R.id.fightActivity_monsterLayout, EnemyFragment.newInstance(monstre));

        Log.d("A", monstre.getName());
    }

    private void ajouterFragment(int containerViewId, Fragment frag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(containerViewId, frag).commit();
    }
}