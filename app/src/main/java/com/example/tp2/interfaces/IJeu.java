/* AUTHEUR: SAMUEL GAUTHIER
 * EXPLICATION:
 *      Interface déterminant ce que les classes voulant
 *      intéragir avec le jeu doivent comportées
 */

package com.example.tp2.interfaces;

import java.util.Scanner;

public interface IJeu {
    int attaque(Scanner reader);
    String obtenirÉtat();
}