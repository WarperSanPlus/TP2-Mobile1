/* AUTHEUR: SAMUEL GAUTHIER
 * EXPLICATION:
 *      Interface déterminant ce que les classes voulant
 *      intéragir avec le jeu doivent comportées
 */

package com.example.tp2.interfaces;

public interface IJeu {
    String obtenirÉtat();

    // attaque() sera specifique pour MonstreEntity
}