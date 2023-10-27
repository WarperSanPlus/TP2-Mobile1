// (YYYY/MM/DD) [AUTHOR] : MODIFICATION
// (2023/10/27) [SAMUEL GAUTHIER] : Added javadoc for the class Hero
//

package com.example.tp2.entities;

import androidx.annotation.NonNull;

import com.example.tp2.FightActivity;

import java.util.Scanner;

/**
 * Déifinition d'un héro
*/
public class Hero extends Personnage {
    final static int STARTING_AMMO = 10;
    final static int MAX_AMMO_PER_TURN = 3;
    final static int STARTING_HEALTH = 10;

    private genre Genre;

    public genre getGenre() {
        return Genre;
    }

    public void setGenre(genre genre) {
        Genre = genre;
    }

    // https://www.w3schools.com/java/java_enums.asp
    enum genre {
        Homme,
        Femme,
        Non_Spécifié
    };

    public Hero(String nom, int munitions, int santé, genre genre) {
        super(nom, munitions, santé);
        setGenre(genre);
    }

    @Override
    public int attaque(Scanner reader) {
        // #region Demander le nombre de munitions à utiliser
        int nombreMunitions;
        int max = getAmmoLeft();

        if (max > MAX_AMMO_PER_TURN)
            max = MAX_AMMO_PER_TURN;

        String question = "Attaquez avec nombre de munitions ";
        for (int i = 1; i <= MAX_AMMO_PER_TURN; i++) {
            question += i + (i == MAX_AMMO_PER_TURN - 1 ? " ou " : (i == MAX_AMMO_PER_TURN ? "" : ", "));
        }
        question += "?";

        do {
            nombreMunitions = 1;//App.AskUserInt(reader, question);

            if ((nombreMunitions > 0 || max == 0) && nombreMunitions <= max)
                break;
            System.out.println(getName() + "veuillez attaquer avec moins de munitions 1 à " + MAX_AMMO_PER_TURN
                    + "; minutions restantes "
                    + getAmmoLeft());
        } while (true);

        // Retirer les munitions
        setAmmoLeft(getAmmoLeft() - nombreMunitions);
        // #endregion

        int munitionsRestants = nombreMunitions;
        for (int index = 0; index < nombreMunitions; ++index) {
            int monstresCountLeft = 1; //App.Monstres.size();

            // Si aucun monstre est vivant, quitter
            if (monstresCountLeft == 0)
                break;

            // Nombre aléatoire entre 0 et n * 2
            int rdmIndex = FightActivity.RNG.nextInt(monstresCountLeft * 2);

            // Si l'index est plus grand que le nombre de monstres restants
            if (rdmIndex >= monstresCountLeft)
                --munitionsRestants;
        }
        return munitionsRestants;
    }

    @Override
    public String obtenirÉtat() {
        return this.toString();
    }

    @NonNull
    @Override
    public String toString() {
        return getName() + " a " + getAmmoLeft() + " munitions et une santé de " + getHealth();
    }

    public static Hero CréerHero(Scanner reader) {
        // #region Nom héro
        System.out.print("Héros! Tapez votre nom: ");
        String nom = reader.nextLine();
        // #endregion

        // #region Sexe héro
        int genreIndex = -1;
        String question = new String();
        Hero.genre[] genres = Hero.genre.values();
        question += "Spécifiez votre genre (";
        for (int i = 0; i < genres.length; i++) {
            Hero.genre genre = genres[i];
            question += i + 1 + ": "; // Ajouter le nombre et le :
            question += genre.toString().replace('_', ' '); // Ajouter le nom de la valeur
            question += (i == genres.length - 1 ? "" : ", "); // Ajouter une virgule tant que le dernier élément n'est
            // pas atteint
        }
        question += ")";

        do {
            genreIndex = 1;//App.AskUserInt(reader, question);

            if (genreIndex > 0 && genreIndex < genres.length)
                break;

            System.out.println("L'index entré n'est pas valide");
        } while (true);
        // #endregion
        return new Hero(nom, STARTING_AMMO, STARTING_HEALTH, genres[genreIndex]);
    }
}