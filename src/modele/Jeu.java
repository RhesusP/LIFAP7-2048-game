package modele;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Observable;
import java.util.Random;
import java.util.HashMap;
import java.awt.Point;
import java.util.Scanner;

public class Jeu extends Observable {
    private static Case[][] tabCases; //contient les Cases

    private static HashMap<Case, Point> hashMap;                //associe les Cases et leurs coordonnées
    private static int score;
    private static int bestScore;
    private static boolean isWinner;                            //est true si la Case 2048 a été atteinte.
    private static final Random rnd = new Random(4);

    /**
     * @brief Constructeur de la classe Jeu
     * Constructeur par défaut initialisant un tableau de Cases 2D aux bonnes dimensions ainsi qu'une hashmap.
     * Le score est initialisé à 0 et le meilleur score est récupéré dans le fichier "./src/.score". Si ce
     * dernier est vide ou n'existe pas, le meilleur score est de 0.
     * @param size La longueur des côtés de la grille.
     */
    public Jeu(int size) {
        tabCases = new Case[size][size];
        hashMap = new HashMap<Case, Point>();
        score = 0;
        isWinner = false;
        //Récupère le meilleur score dans le fichier adéquat.
        try {
            File fichier = new File("./src/.score");
            Scanner reader = new Scanner(fichier);
            bestScore = Integer.parseInt(reader.nextLine());
            reader.close();
        } catch (FileNotFoundException e) {
            System.out.println("No data found");
            bestScore = 0;
            e.printStackTrace();
        }
        init_jeu();
    }

    /**
     * @brief Contructeur "speedrun" de la classe Jeu.
     * Ce constructeur est appelé lorsque l'option "--speedrun" est renseigné au
     * lancement du programme. Cette option permet d'arriver plus rapidement à la
     * Case 2048 et donc de gagner plus rapidement.
     * @param size La longueur des côtés de la grille.
     * @param option Le mode de jeu lancé.
     */
    public Jeu(int size, String option){
        this(size);
        hashMap.forEach((key, value) -> {key.setValeur(1024); key.majCouleur();});
    }

    /**
     * @brief Accesseur à la taille du tableau de Cases.
     * @return La taille du tableau.
     */
    public int getSize() {
        return tabCases.length;
    }

    /**
     * @brief Accesseur à une case du tableau de Cases.
     * @param i La ligne de la Case (i >= 0).
     * @param j La colonne de la Case (j >= 0).
     * @return La Case correspondante aux coordonnées données.
     */
    public static Case getCase(int i, int j) {
        return tabCases[i][j];
    }

    /**
     * @brief Mutateur du tableau de Cases.
     * @param i La ligne de la Case à modifier (i >= 0).
     * @param j La colonne de la Case à modifier (j >= 0).
     * @param newCase La Case à insérer dans le tableau.
     */
    public static void setCase(int i, int j, Case newCase) {
        tabCases[i][j] = newCase;
    }

    /**
     * @brief Accesseur au score du Jeu.
     * @return Le score actuel.
     */
    public static int getScore() {
        return score;
    }

    /**
     * @brief Mutateur au score du Jeu.
     * @param newScore Le nouveau score.
     */
    public static void setScore(int newScore) {
        score = newScore;
    }

    /**
     * @brief Accesseur au meilleur score du Jeu.
     * @return Le meilleur score.
     */
    public static int getBestScore() {
        return bestScore;
    }

    /**
     * @brief Mutateur au meilleur score du Jeu.
     * Le meilleur score est également écrit en local dans le fichier "./src/.score".
     * @param newBestScore Le nouveau meilleur record.
     */
    public static void setBestScore(int newBestScore) {
        bestScore = newBestScore;
        try {
            FileWriter writer = new FileWriter("./src/.score");
            writer.write(String.valueOf(bestScore));
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * @brief Accesseur au booléen permettant de savoir si la partie est gagnante.
     * @return true si la Case 2048 a été atteinte, false sinon.
     */
    public boolean getIsWinner(){
        return isWinner;
    }

    /**
     * @brief Mutateur au booléen permettant de savoir si la partie est gagnante.
     * @param isWinner Le nouveau booléen.
     */
    public void setIsWinner(boolean isWinner){
        this.isWinner = isWinner;
    }

    /**
     * @brief Supprime un élément de la hashmap.
     * @param c La Case (clé de la hashmap) à supprimer.
     */
    public static void removeFromHashMap(Case c) {
        hashMap.remove(c);
    }

    /**
     * @brief Ajoute un couple Case / Point à la hashmap.
     * @param c La Case (clé) à ajouter.
     * @param p Le Point (valeur) à ajouter.
     */
    public static void addToHashMap(Case c, Point p){
        hashMap.put(c,p);
    }

    /**
     * @brief Accesseur aux coordonnées d'une Case située dans la hashmap.
     * @param c La Case (clé).
     * @return Point contenant les coordonnées de la Case.
     */
    public static Point getCoordinate(Case c){
        return hashMap.get(c);
    }

    /**
     * @brief Initialise le tableau de Cases à null.
     * Appelée à la création du Jeu, elle affecte à tous les éléments de tabCases
     * la valeur null puis appelle la méthode init_valeur afin de faire apparaitre deux
     * Cases à des coordonnées aléatoires.
     */
    public void init_jeu(){
        for(int i=0; i<tabCases.length; i++){
            for(int j=0; j<tabCases.length; j++){
                tabCases[i][j] = null;
            }
        }
        init_valeur(2);
    }

    /**
     * @brief Insère un ou plusieurs Cases à des emplacements aléatoires sur le jeu.
     * Insère un nombre passé en paramètre de Cases aléatoirement parmi les emplacements
     * disponibles. Les Cases insérées ont 90% de chance d'avoir la valeur 2 et 10% de chance
     * d'avoir la valeur 4.
     * @param nbVal Nombre de valeurs à insérer dans le jeu.
     */
    public void init_valeur(int nbVal){
        int x;
        int y;
        Random rand = new Random();
        int i=0;
        while(i < nbVal){
            do{
                x = rand.nextInt(4);
                y = rand.nextInt(4);
            } while(tabCases[x][y] != null);
            Point coordinates = new Point(x,y);
            int r = rand.nextInt(10);
            switch(r){
                default:
                    tabCases[x][y] = new Case(2);
                    break;
                case 9:
                    tabCases[x][y] = new Case(4);
                    break;
            }
            tabCases[x][y].majCouleur();
            if(tabCases[x][y] != null){
                hashMap.put(tabCases[x][y], coordinates);
            }
            i++;
        }
    }

    /**
     * @brief Reinitialise la partie.
     * Met le score à 0, nettoie la hashmap et relance une partie.
     */
    public void reset_jeu(){
        setScore(0);
        setIsWinner(false);
        hashMap.clear();
        init_jeu();
    }

    /**
     * @brief Prévient toutes les cases non nulles d'un déplacement à effectuer.
     * Effectue une rotation de tabCases selon la direction dans laquelle doivent bouger
     * les Cases et appelle la méthode permettant d'effectuer le mouvement de la classe Case
     * pour les cases non nulles. Le meilleur score est également mis à jour ici.
     * @param direction La direction dans laquelle bouger toutes les Cases.
     */
    public void action(Direction direction){
        //La fonction 'déplacer(direction)' de la classe Case ne permet de déplacer les Cases que vers
        //le haut. Une ou plusieurs rotations de 90° vers la droite sont donc faites sur tabCases afin
        //de pouvoir déplacer les Cases.
        switch (direction) {
            case droite -> rotateTab(3);
            case gauche -> rotateTab(1);
            case bas -> rotateTab(2);
        }
        for(int i = 1; i < tabCases.length; i++){
            for(int j = 0; j < tabCases.length; j++){
                if(tabCases[i][j] != null){
                    isWinner = tabCases[i][j].deplacer();
                }
            }
        }
        //On refait une ou plusieurs rotations sur la droite afin de ramener tabCases dans le sens initial.
        switch (direction) {
            case droite -> rotateTab(1);
            case gauche -> rotateTab(3);
            case bas -> rotateTab(2);
        }
        if(getScore() > getBestScore()){
            setBestScore(getScore());
        }
        init_valeur(1);
        setChanged();
        notifyObservers();
    }

    /**
     * @brief Effectue une rotation de 90° vers la droite à tabCases.
     * @param nb Le nombre de rotations à faire.
     */
    public void rotateTab(int nb){
        for(int k=0 ; k<nb ; k++){
            Case[][] copie = new Case [tabCases.length][tabCases.length];
            for(int i = 0; i < tabCases.length; i++){
                for(int j = 0; j < tabCases.length; j++){
                    copie[j][tabCases.length-1 - i] = tabCases[i][j];
                    hashMap.replace(tabCases[i][j], new Point(j,tabCases.length-1 - i));
                }
            }
            tabCases = copie;
        }
    }

    public void rnd() {
        new Thread() { // permet de libérer le processus graphique ou de la console
            public void run() {
                int r;
                for (int i = 0; i < tabCases.length; i++) {
                    for (int j = 0; j < tabCases.length; j++) {
                        Point coordonnées = new Point(i, j);        //coordonnées du point courant
                        r = rnd.nextInt(3);

                        switch (r) {
                            case 0:
                                tabCases[i][j] = null;
                                break;
                            case 1:
                                tabCases[i][j] = new Case(2);
                                break;
                            case 2:
                                tabCases[i][j] = new Case(4);
                                break;
                        }
                        if(tabCases[i][j] != null){
                            hashMap.put(tabCases[i][j], coordonnées);
                        }
                    }
                }
            }
        }.start();
        setChanged();
        notifyObservers();
    }
}