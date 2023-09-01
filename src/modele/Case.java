package modele;
import java.awt.*;

import vue_controleur.Couleur;

import static vue_controleur.Couleur.dark;
import static vue_controleur.Couleur.light;

import vue_controleur.Couleur;

import java.awt.*;

public class Case {
    private int valeur;
    private Color couleur;                      //couleur de fond.
    private Color foregroundColor;              //couleur du texte.

    /**
     * @brief Constructeur de la classe Case.
     * @param _valeur La valeur de la Case.
     */
    public Case(int _valeur) {
        valeur = _valeur;
        couleur = Color.white;
    }

    /**
     * @brief Accesseur à la couleur de fond de la Case.
     * @return La couleur de fond.
     */
    public Color getBackgroundCouleur(){
        return this.couleur;
    }

    /**
     * @brief Mutateur à la couleur de fond de la Case.
     * @param couleur La nouvelle couleur de fond.
     */
    public void setCouleur(Color couleur){
        this.couleur = couleur;
    }

    /**
     * @brief Accesseur à la couleur du texte de la case.
     * @return La couleur du texte.
     */
    public Color getForegroundColor(){
        return this.foregroundColor;
    }

    /**
     * @brief Mutateur à la couleur du texte de la Case.
     * @param foregroundColor La nouvelle couleur du texte.
     */
    public void setForegroundColor(Color foregroundColor){
        this.foregroundColor = foregroundColor;
    }

    /**
     * @brief Assigne une nouvelle couleur de fond et de texte à la case
     * en fonction de sa valeur.
     * La couleur de texte est noire pour les Cases ayant une valeur de
     * 2 ou de 4 et blanche pour les autres (entre 8 et 2048).
     */
    public void majCouleur(){
        int value = this.valeur;
        switch (value) {
            case 2 -> {
                setCouleur(Couleur.grey2);
                setForegroundColor(dark);
            }
            case 4 -> {
                setCouleur(Couleur.grey4);
                setForegroundColor(dark);
            }
            case 8 -> {
                setCouleur(Couleur.orange8);
                setForegroundColor(light);
            }
            case 16 -> {
                setCouleur(Couleur.orange16);
                setForegroundColor(light);
            }
            case 32 -> {
                setCouleur(Couleur.red32);
                setForegroundColor(light);
            }
            case 128 -> {
                setCouleur(Couleur.red64);
                setForegroundColor(light);
            }
            case 256 -> {
                setCouleur(Couleur.yellow128);
                setForegroundColor(light);
            }
            case 512 -> {
                setCouleur(Couleur.yellow256);
                setForegroundColor(light);
            }
            case 1024 -> {
                setCouleur(Couleur.yellow1024);
                setForegroundColor(light);
            }
            case 2048 -> {
                setCouleur(Couleur.yellow2048);
                setForegroundColor(light);
            }
        }
    }

    /**
     * @brief Accesseur à la valeur d'une Case.
     * @return La valeur de la Case.
     */
    public int getValeur() {
        return valeur;
    }

    /**
     * @brief Mutateur à la valeur de la Case.
     * @param valeur La nouvelle valeur.
     */
    public void setValeur(int valeur) {
        this.valeur = valeur;
    }

    /**
     * @brief Déplace au maximum une Case vers le haut et la fusionne si besoin.
     * Permet de déplacer la Case (et non seulement sa valeur) dans le tableau de Cases.
     * La Case avance vers le haut tant que son voisin haut est nul ou qu'elle est au bord
     * du tableau. Si le voisin qu'elle rencontre a une valeur égale à la sienne, la Case
     * fusionne avec son voisin. La hashmap est également mise à jour en fonction des
     * mouvements effectués. Le score est également mis à jour en cas de fusion de deux cases.
     * @return Un booléen true si la Case 2048 a été atteinte (donc le joueur a gagné).
     */
    public boolean deplacer(){
        Boolean isWinner = false;
        Point p = Jeu.getCoordinate(this);
        int x = (int)p.getX();
        int y = (int)p.getY();
        Case neighbour;
        while(x>0){
            neighbour = Jeu.getCase(x-1, y);
            if(neighbour == null){
                Jeu.setCase(x-1, y, this);
                Jeu.setCase(x,y, null);
                Jeu.removeFromHashMap(this);
                Jeu.addToHashMap(this, new Point(x-1,y));
            }
            //Fusion entre les deux cases
            else if(neighbour.getValeur() == this.valeur){
                int newValeur = neighbour.getValeur() + this.valeur;
                Jeu.setScore(Jeu.getScore() + newValeur);
                Jeu.setCase(x-1, y, this);
                Jeu.setCase(x,y,null);
                this.setValeur(newValeur);
                this.majCouleur();
                Jeu.removeFromHashMap(this);
                Jeu.removeFromHashMap(neighbour);
                Jeu.addToHashMap(this, new Point(x-1,y));
                //Cas de la partie gagnée -→ case 2048 atteinte
                if(newValeur == 2048){
                    isWinner = true;
                }
                break;
            }
            x--;
        }
        return isWinner;
    }
}