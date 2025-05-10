# Amiibo-Quiiz-Game

##  Présentation

Amiibo Quiz est une application Android ludique développée dans le cadre du cours *Application Mobile et Ergonomie* (Licence 3 Informatique). Elle permet de tester ses connaissances sur les figurines Amiibo de l’univers Nintendo à travers un quiz interactif et ergonomique.

##  Équipe

- Groupe : 03  
- Réalisé par : **Arioui Mohamed Achraf Ouassim**  
- Responsable pédagogique : **Mohammed Morchid**  
- Date : 10/04/2025  

##  Technologies et outils

- **Android Studio** : Environnement de développement
- **Kotlin** : Langage principal
- **Retrofit** : Requêtes API REST
- **Glide** : Chargement d’images
- **Realm (legacy)** : Base de données locale
- **MediaPlayer** : Gestion des sons
- **StarUML** : Modélisation UML
- **XML / ConstraintLayout** : Interfaces responsives

##  Fonctionnalités principales

- Quiz basé sur les Amiibos (image + 3 choix)
- Navigation entre activités (MainActivity, GameActivity…)
- Affichage et gestion dynamique du score
- Détection des gestes (swipe gauche/droite)
- Intégration d’une API REST pour récupérer les données
- Stockage local avec Realm
- Gestion de la musique de fond
- Transitions et animations pour une meilleure UX

##  Ergonomie et utilisabilité

- Interface responsive et épurée
- Boutons bien espacés et intuitifs
- Score visible et dynamique
- Navigation fluide et gestes utilisateurs
- Feedback immédiat (visuel + sonore)

##  Modélisation UML

- **Diagrammes de classes** : Entités comme `Amiibo`, `GameActivity`, `ApiService`…
- **Diagrammes de séquence** : Swipe, sélection de réponse, lancement du quiz…
- **Cas d’utilisation** : Gestion du score, musique, navigation utilisateur…

##  Difficultés rencontrées

- Changement dynamique de couleur dans le menu
- Gestion de la musique en arrière-plan

##  Améliorations apportées

- Changement de couleur des boutons en fonction de la réponse
- Pause/reprise automatique de la musique selon le cycle de vie de l’activité

##  Conclusion

L’application est complète, stable et propose une vraie logique de quiz. Elle peut être étendue ou publiée sur le Google Play Store avec quelques ajustements graphiques ou fonctionnels.

---

> Pour lancer le projet :  
> Ouvrir avec Android Studio → Lancer sur un appareil Android ou émulateur.