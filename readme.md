# Projet 2048
### LIFAP7 : Algorithmique et Programmation Orientée Objet

## Rendu graphique

<p align="center" width="100%">
<img src='./img/solo.png' height="350" width="300"/>
</p>
<p align="center" width="100%">
<img src='./img/duo.png' height="350"/>
</p>


## Fonctionnalités implémentées

✓ Mouvement et fusion des cases  
✓ Amélioration du rendu graphique (couleurs, menu, affichage des scores, ...)  
✓ Enregistrement du meilleur score  
✓ Possibilité de réinitialiser une partie  
✓ Version deux joueurs

## Précisions sur le code 
Le mouvement des Cases est réalisé grâce aux fonctions `void action(Direction direction)` de la classe `Jeu` et `boolean deplacer()` de la classe `Case`.

La fonction `boolean deplacer()` permet de déplacer une tuile au maximum **vers le haut** et de la fusionner avec son voisin si ces dernières ont les mêmes valeurs. 

La fonction `void action(Direction direction)` appelle la fonction `deplacer()` sur toutes les cases (sauf celles de la premieres lignes, car elles ne peuvent pas plus être plus ramenées vers le haut). Pour palier au fait que la fonction `deplacer()` ne permette de bouger les cases seulement vers le haut, on effectue une rotation du tableau `tabCases` avant et après le mouvement des Cases.