import modele.Jeu;
import vue_controleur.Console2048;
import vue_controleur.Swing2048;

import java.util.Objects;

public class Main {

    public static void main(String[] args) {
        //mainConsole();
        if (args.length == 0) {
            mainSwing();
        }
        else if (Objects.equals(args[0], "--speedrun")) {
            mainSwing(args[0]);
        }
    }

    public static void mainConsole() {
        Jeu jeu = new Jeu(4);
        Console2048 vue = new Console2048(jeu);
        jeu.addObserver(vue);

        vue.start();
    }

    public static void mainSwing() {
        Jeu jeu1 = new Jeu(4);
        Jeu jeu2 = new Jeu(4); //Jeu second joueur
        Swing2048 vue = new Swing2048(jeu1, jeu2);
        jeu1.addObserver(vue);
        vue.setTitle("Projet 2048");
        vue.setVisible(true);
    }

    public static void mainSwing(String option) {
        Jeu jeu = new Jeu(4, option);
        Jeu jeu2 = new Jeu(4, option); //Jeu second joueur
        Swing2048 vue = new Swing2048(jeu, jeu2);
        jeu.addObserver(vue);
        vue.setTitle("Projet 2048");
        vue.setVisible(true);
    }
}