package vue_controleur;

import modele.Case;
import modele.Direction;
import modele.Jeu;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.util.Objects;
import java.util.Observable;
import java.util.Observer;

import static modele.Direction.*;
import static vue_controleur.Couleur.*;

public class Swing2048 extends JFrame implements Observer {
    private static final int PIXEL_PER_SQUARE = 100;
    private JLabel[][] tabC;            //Tableau de cases : i, j -> case graphique
    private JLabel[][] tab;             //Tableau de Cases pour le 2nd joueur
    private JLabel score;
    private JLabel bestScore;
    private final Jeu jeu;
    private final Jeu jeu2;
    private int nbJoueur;               //1 par défaut

    /**
     * @brief Constructeur par défaut de la classe Swing 2048.
     * Affiche le menu de sélection du nombre de joueurs et appelle la fonction
     * adéquate au choix du joueur.
     * @param _jeu Le Jeu du premier joueur.
     * @param jeu1 Le Jeu du second joueur.
     */
    public Swing2048(Jeu _jeu, Jeu jeu1) {
        jeu = _jeu;
        jeu2 = jeu1;
        nbJoueur = 1;

        setSize(jeu.getSize() * PIXEL_PER_SQUARE, jeu.getSize() * PIXEL_PER_SQUARE+60);
        setResizable(false);
        JPanel frame =new JPanel(new GridLayout(2, 1));
        Font menuFont = new Font("Sans Serif", Font.PLAIN, 40);

        //Bouton jeu solo
        JButton soloButton =new JButton("1 JOUEUR");
        soloButton.setFont(menuFont);
        soloButton.setBorderPainted(false);
        soloButton.setOpaque(true);
        soloButton.setBackground(yellow2048);
        soloButton.setForeground(Color.white);
        soloButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                Solo();
                frame.setVisible(false);    //rend le menu invisible
            }
        });

        //Bouton jeu multijoueur
        JButton multiButton =new JButton("2 JOUEURS");
        multiButton.setFont(menuFont);
        multiButton.setBorderPainted(false);
        multiButton.setOpaque(true);
        multiButton.setBackground(red64);
        multiButton.setForeground(Color.white);
        multiButton.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                Duo();
                frame.setVisible(false);
            }
        });
        frame.add(soloButton);
        frame.add(multiButton);
        getContentPane().add(frame);
    }

    /**
     * @brief Initialisation d'une partie en mode solo.
     * Mise en place de l'interface graphique du jeu et initialisation du
     * tableau de Cases graphiques. L'interface permet d'afficher le score
     * de la partie actuelle, le meilleur score, un bouton permettant de
     * réinitialiser sa partie et la grille de Cases.
     */
    private void Solo() {
        setSize(jeu.getSize() * PIXEL_PER_SQUARE, jeu.getSize() * PIXEL_PER_SQUARE+60);
        tabC = new JLabel[jeu.getSize()][jeu.getSize()];
        Font scoreFont = new Font("SansSerif", Font.BOLD, 24);       //police pour les scores
        Font caseFont = new Font("Monospaced", Font.BOLD, 40);       //police pour les Cases de 2 à 512

        //Panel container score / meilleur score / bouton reset
        JPanel infosPane = new JPanel();
        infosPane.setLayout(new GridLayout(1,3));
        infosPane.setOpaque(true);

        //Panel affichage score partie actuelle
        JPanel scorePane = new JPanel(new GridLayout(2,1));
        scorePane.add(new JLabel("SCORE", SwingConstants.CENTER));
        score = new JLabel("", SwingConstants.CENTER);
        score.setFont(scoreFont);
        scorePane.add(score);

        //Panel affichage meilleur score
        JPanel bestScorePane = new JPanel(new GridLayout(2,1));
        bestScorePane.add(new JLabel("BEST", SwingConstants.CENTER));
        bestScore = new JLabel("", SwingConstants.CENTER);
        bestScore.setFont(scoreFont);
        bestScorePane.add(bestScore);

        //Bouton réinitialisation
        JPanel buttonPane = new JPanel();
        JButton resetBtn = new JButton();
        resetBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("./../assets/reset-icon.png"))));
        resetBtn.setPreferredSize(new Dimension(50, 50));
        resetBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                jeu.reset_jeu();
                rafraichir();
                ajouterEcouteurClavier();
                setFocusable(true);
                requestFocus();
            }
        });

        //Panel du bas contenant les cases du jeu.
        JPanel contentPane = new JPanel(new GridLayout(jeu.getSize(), jeu.getSize()));
        for (int i = 0; i < jeu.getSize(); i++) {
            for (int j = 0; j < jeu.getSize(); j++) {
                Border border = BorderFactory.createLineBorder(Couleur.background, 5);
                tabC[i][j] = new JLabel();
                tabC[i][j].setBorder(border);
                tabC[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                tabC[i][j].setFont(caseFont);
                contentPane.add(tabC[i][j]);
            }
        }

        buttonPane.add(resetBtn);
        infosPane.add(scorePane);
        infosPane.add(bestScorePane);
        infosPane.add(buttonPane);

        getContentPane().add(infosPane, BorderLayout.NORTH);
        getContentPane().add(contentPane);

        ajouterEcouteurClavier();
        setFocusable(true);
        requestFocus();
        rafraichir();
    }

    /**
     * @brief Initialisation d'une partie en mode 2 joueurs.
     * Mise en place de l'interface graphique du jeu et initialisation des
     * tableaux de Cases graphiques. L'interface permet d'afficher le score
     * de la partie actuelle, le meilleur score, un bouton permettant de
     * réinitialiser sa partie et la grille de Cases.
     */
    private void Duo(){
        setSize(jeu.getSize() * PIXEL_PER_SQUARE * 3, jeu.getSize() * PIXEL_PER_SQUARE+60);
        tabC = new JLabel[jeu.getSize()][jeu.getSize()];
        tab = new JLabel[jeu.getSize()][jeu.getSize()];
        Font scoreFont = new Font("SansSerif", Font.BOLD, 24);       //police pour les scores
        Font caseFont = new Font("Monospaced", Font.BOLD, 40);       //police pour les Cases de 2 à 512

        //Panel container score / meilleur score / bouton reset
        JPanel infosPane = new JPanel();
        infosPane.setLayout(new GridLayout(1,3));
        infosPane.setOpaque(true);

        //Panel affichage score partie actuelle
        JPanel scorePane = new JPanel(new GridLayout(2,1));
        scorePane.add(new JLabel("SCORE", SwingConstants.CENTER));
        score = new JLabel("", SwingConstants.CENTER);
        score.setFont(scoreFont);
        scorePane.add(score);

        //Panel affichage meilleur score
        JPanel bestScorePane = new JPanel(new GridLayout(2,1));
        bestScorePane.add(new JLabel("BEST", SwingConstants.CENTER));
        bestScore = new JLabel("", SwingConstants.CENTER);
        bestScore.setFont(scoreFont);
        bestScorePane.add(bestScore);

        //Bouton réinitialisation
        JPanel buttonPane = new JPanel();
        JButton resetBtn = new JButton();
        resetBtn.setIcon(new ImageIcon(Objects.requireNonNull(getClass().getResource("./../assets/reset-icon.png"))));
        resetBtn.setPreferredSize(new Dimension(50, 50));
        resetBtn.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent e) {
                jeu.reset_jeu();
                rafraichir();
                ajouterEcouteurClavier();
                setFocusable(true);
                requestFocus();
            }
        });

        //Panel du bas contenant les cases du jeu.
        JPanel contentPane = new JPanel(new GridLayout(jeu.getSize(), jeu.getSize()));
        for (int i = 0; i < jeu.getSize(); i++) {
            for (int j = 0; j < jeu.getSize(); j++) {
                Border border = BorderFactory.createLineBorder(Couleur.background, 5);
                tabC[i][j] = new JLabel();
                tabC[i][j].setBorder(border);
                tabC[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                tabC[i][j].setFont(caseFont);
                contentPane.add(tabC[i][j]);
            }
        }

        JPanel Jeu = new JPanel(new GridLayout());
        JPanel Joueur2 = new JPanel(new GridLayout(jeu2.getSize(), jeu2.getSize()));

        for (int i = 0; i < jeu2.getSize(); i++) {
            for (int j = 0; j < jeu2.getSize(); j++) {
                Border border = BorderFactory.createLineBorder(Couleur.background, 5);
                tab[i][j] = new JLabel();
                tab[i][j].setBorder(border);
                tab[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                tab[i][j].setFont(caseFont);
                Joueur2.add(tab[i][j]);
            }
        }
        nbJoueur++;

        buttonPane.add(resetBtn);
        infosPane.add(scorePane);
        infosPane.add(bestScorePane);
        infosPane.add(buttonPane);
        Jeu.add(contentPane, BorderLayout.WEST);
        Jeu.add(new JSeparator(JSeparator.VERTICAL));
        Jeu.add(Joueur2, BorderLayout.EAST);
        getContentPane().add(infosPane, BorderLayout.NORTH);
        getContentPane().add(Jeu);
        ajouterEcouteurClavier();
        setFocusable(true);
        requestFocus();
        rafraichir();
    }

    /**
     * @brief Affiche les données du modèle
     * Met à jour le score actuel, le meilleur score s'il est dépassé ainsi que la grille de Cases.
     */
    private void rafraichir()  {
        SwingUtilities.invokeLater(new Runnable() { // demande au processus graphique de réaliser le traitement
            @Override
            public void run() {
                score.setText(String.valueOf(Jeu.getScore()));
                bestScore.setText(String.valueOf(Jeu.getBestScore()));
                for (int i = 0; i < jeu.getSize(); i++) {
                    for (int j = 0; j < jeu.getSize(); j++) {
                        Case c = jeu.getCase(i, j);
                        if (c == null) {
                            tabC[i][j].setText("");
                            tabC[i][j].setOpaque(true);
                            tabC[i][j].setBackground(Couleur.void0);
                        } else {
                            tabC[i][j].setText(c.getValeur() + "");
                            tabC[i][j].setOpaque(true);
                            tabC[i][j].setBackground(c.getBackgroundCouleur());
                            tabC[i][j].setForeground(c.getForegroundColor());
                            if(c.getValeur() > 999){
                                tabC[i][j].setFont(new Font("Monospaced", Font.BOLD,32));          //police pour les Cases >= 1024
                            }
                        }

                        //Gere l'affichage du deuxième jeu.
                        if(nbJoueur == 2) {
                            Case c1 = jeu2.getCase(i, j);
                            if (c1 == null) {
                                tab[i][j].setText("");
                                tab[i][j].setOpaque(true);
                                tab[i][j].setBackground(Couleur.void0);
                            } else {
                                tab[i][j].setText(c1.getValeur() + "");
                                tab[i][j].setOpaque(true);
                                tab[i][j].setBackground(c1.getBackgroundCouleur());
                                tab[i][j].setForeground(c1.getForegroundColor());
                                if (c1.getValeur() > 999) {
                                    tab[i][j].setFont(new Font("Monospaced", Font.BOLD, 32));      //police pour les Cases >= 1024
                                }
                            }
                        }
                    }
                }
                if(jeu.getIsWinner() || jeu2.getIsWinner()){
                    displayWinner();
                }
            }
        });
    }

    /**
     * @brief Affiche une pop-up avertissant le joueur de sa victoire.
     */
    public void displayWinner(){
        setVisible(false);
        JFrame popupFrame = new JFrame();
        JLabel message = new JLabel("You reach 2048 ! \uD83C\uDF89\uD83C\uDF89\uD83C\uDF89");
        message.setFont(new Font("SansSerif", Font.LAYOUT_LEFT_TO_RIGHT, 15));
        Icon icon = new ImageIcon(Objects.requireNonNull(getClass().getResource("./../assets/winner-animation.gif")));
        JOptionPane.showMessageDialog(popupFrame, message, "You win !" ,JOptionPane.INFORMATION_MESSAGE, icon);
        System.exit(0);
    }

    /**
     * @brief Écoute les événements et déclenche des traitements sur le modèle.
     * Appelle action() de la classe Jeu avec la direction adéquate en paramètre.
     */
    private void ajouterEcouteurClavier() {
        addKeyListener(new KeyAdapter() { // new KeyAdapter() { ... } est une instance de classe anonyme, il s'agit d'un objet qui correspond au controleur dans MVC
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {  // on regarde quelle touche a été pressée
                    case KeyEvent.VK_LEFT -> jeu.action(gauche);
                    case KeyEvent.VK_RIGHT -> jeu.action(droite);
                    case KeyEvent.VK_DOWN -> jeu.action(bas);
                    case KeyEvent.VK_UP -> jeu.action(haut);
                }
            }
        });
    }

    @Override
    public void update(Observable o, Object arg) {
        rafraichir();
    }
}