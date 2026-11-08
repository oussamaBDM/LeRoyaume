import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Affichage{

    public static final long DELAI_NARRATION = 4; // ms entre deux lettres, pour les phrases racontees uniquement
    public static final int LETTRES_PAR_PAQUET = 6;
    public static final long DELAI_PAQUET = 8;    // ms entre deux paquets
    public static final long DELAI_LIGNE = 70;    // ms entre deux lignes de jauges, pour qu'elles se remplissent une par une

    public static void pause(long delaiMs){
        try{
            Thread.sleep(delaiMs);
        }catch (InterruptedException e){
            Thread.currentThread().interrupt();
        }
    }

    public static void catFichier(String fichier){ // Affiche d'un coup
        System.out.println(lireFichier(fichier));
    }

    // Contenu du fichier sans ses LIGNES vides de debut et de fin
    private static String lireFichier(String fichier){
        try{
            List<String> lignes = new ArrayList<>(Files.readAllLines(Path.of(fichier)));
            while(!lignes.isEmpty() && lignes.get(0).isBlank()) lignes.remove(0);
            while(!lignes.isEmpty() && lignes.get(lignes.size() - 1).isBlank()) lignes.remove(lignes.size() - 1);
            return String.join("\n", lignes);
        }catch (IOException e){
            return "Erreur de lecture : " + e.getMessage();
        }
    }

public static void catFichier(String fichierGauche, String fichierDroit, int espacement){ // Affiche d'un coup 2 fichier txt avec un espacement
    try {
        List<String> gauche = Files.readAllLines(Path.of(fichierGauche));
        List<String> droit = Files.readAllLines(Path.of(fichierDroit));
    
    
    
        int maxLignes = Math.max(gauche.size(), droit.size());
        while (gauche.size() < maxLignes) gauche.add("");
        while (droit.size() < maxLignes) droit.add("");

        int largeurGauche = 0;
        for (String ligne : gauche) {
            if (ligne.length() > largeurGauche) largeurGauche = ligne.length();
        }

        String separateur = "  ";

        for (int i = 0; i < maxLignes; i++) {
            String ligneG = gauche.get(i);
            String ligneD = droit.get(i);
            System.out.printf("%-" + largeurGauche + "s%s%s%n", ligneG, separateur, ligneD);
        }
    } catch (IOException e) {
        System.out.println("Erreur de lecture des fichiers : " + e.getMessage());
    }
}


    public static void afficherLentement(String texte, long delaiMs){//Delai de au choix
        for(char c : texte.toCharArray()){
            System.out.print(c);
            System.out.flush();
            pause(delaiMs);
        }
        System.out.println();
    }
    
    public static void catFichier(String fichier, long delaiMs){ //Affiche le fichier a la vitesse souhaité
        afficherLentement(lireFichier(fichier), delaiMs);
    }

    // ================= Affichage des jauges =================

    public static final boolean COULEURS = true; // a passer a false si votre terminal affiche des [32m au lieu des couleurs

    private static final String RESET = "\u001B[0m";
    private static final String VERT = "\u001B[32m";
    private static final String ROUGE = "\u001B[31m";
    private static final String GRIS = "\u001B[90m";
    private static final String GRAS = "\u001B[1m";

    public static String gras(String texte){
        return couleur(GRAS) + texte + couleur(RESET);
    }

    // Laisse le joueur lire avant d'enchainer
    public static void attendreEntree(Scanner sc){
        System.out.println();
        System.out.println("        [ Entree pour continuer ]");
        if(sc.hasNextLine()) sc.nextLine();
    }

    // Un texte encadre par deux traits de sa largeur
    public static void banniere(String texte){
        String trait = "-".repeat(texte.length());
        System.out.println();
        System.out.println(trait);
        System.out.println(texte);
        System.out.println(trait);
    }

    private static final int LARGEUR_JAUGE = 20; // 20 caracteres pour 100 points : 1 caractere = 5 points

    // Affiche les 6 jauges du roi sous forme de barres.
    // avant = l'etat des jauges avant le choix, pour afficher les variations (null = pas de variation a montrer)
    public static void afficherJauges(Roi roi, Ressource avant){
        Ressource res = roi.getRessources();
        System.out.println();
        System.out.println(roi.getNom() + " :");
        ligneJauge("Bonheur", res.getBonheur(), avant == null ? res.getBonheur() : avant.getBonheur());
        ligneJauge("Or", res.getOr(), avant == null ? res.getOr() : avant.getOr());
        ligneJauge("Nourriture", res.getNourriture(), avant == null ? res.getNourriture() : avant.getNourriture());
        ligneJauge("Population", res.getPopulation(), avant == null ? res.getPopulation() : avant.getPopulation());
        ligneJauge("Armee", res.getArmee(), avant == null ? res.getArmee() : avant.getArmee());
        ligneJauge("Prestige", res.getPrestige(), avant == null ? res.getPrestige() : avant.getPrestige());
        pause(DELAI_LIGNE);
        System.out.println();
    }

    private static void ligneJauge(String nom, double valeur, double valeurAvant){
        pause(DELAI_LIGNE); // les jauges se remplissent une par une, pas d'un bloc
        System.out.println("  " + String.format("%-11s", nom) + " " + barre(valeur)
            + String.format("%6s", nombre(valeur)) + "  " + variation(valeur - valeurAvant));
    }

    // [########------------] c quand la partie pleine passe en rouge quand la jauge devient critique
    private static String barre(double valeur){
        int plein = (int) Math.round(valeur / 100. * LARGEUR_JAUGE);
        StringBuilder barre = new StringBuilder("[");
        barre.append(couleur(valeur <= 25 ? ROUGE : RESET));
        for(int i = 0; i < plein; i++) barre.append("#");
        barre.append(couleur(GRIS));
        for(int i = plein; i < LARGEUR_JAUGE; i++) barre.append("-");
        return barre.append(couleur(RESET)).append("]").toString();
    }

    // +15 en vert, -20 en rouge, rien du tout si la jauge n'a pas bouge
    private static String variation(double delta){
        if(Math.abs(delta) < 0.01) return "";
        if(delta > 0) return couleur(VERT) + "+" + nombre(delta) + couleur(RESET);
        return couleur(ROUGE) + "-" + nombre(-delta) + couleur(RESET);
    }

    // 50.0 s'ecrit 50, 52.5 reste 52.5
    private static String nombre(double valeur){
        if(valeur == Math.rint(valeur)) return String.valueOf((int) valeur);
        return String.valueOf(Math.round(valeur * 10) / 10.);
    }

    private static String couleur(String code){
        return COULEURS ? code : "";
    }

    // Affiche un texte progressivement, par petits paquets : ni instantane, ni lettre par lettre
    public static void afficherProgressif(String texte){
        int compteur = 0;
        for(char c : texte.toCharArray()){
            System.out.print(c);
            compteur++;
            if(compteur % LETTRES_PAR_PAQUET == 0){
                System.out.flush();
                pause(DELAI_PAQUET);
            }
        }
        System.out.flush();
        System.out.println();
    }

    public static void catFichierProgressif(String fichier){
        afficherProgressif(lireFichier(fichier));
    }

    // ================= Fin de partie =================

    public static final long DELAI_FIN = 42;      // ms par lettre : la derniere phrase tombe lentement
    private static final long SILENCE = 600;      // ms de blanc avant et apres la sentence
    private static final int LARGEUR_STELE = 25;

    // Fin de regne : un silence, la phrase qui tombe lettre par lettre, puis la stele
    public static void afficherFin(String phrase, String nom, int jours){
        pause(SILENCE);
        System.out.println();
        afficherLentement(phrase, DELAI_FIN);
        pause(SILENCE + 300);
        stele("R . I . P .", nom, "a regne " + jours + (jours > 1 ? " journees" : " journee"));
        pause(SILENCE);
        afficherLentement("Le royaume continuera sans vous.", DELAI_FIN);
        System.out.println();
    }

    // Le roi a traverse tous les evenements sans tomber
    public static void afficherFinVictoire(String nom, int jours){
        pause(SILENCE);
        System.out.println();
        afficherLentement("Vous avez tenu " + jours + " journees. Votre couronne ne vous va toujours pas, mais elle est a vous.", DELAI_FIN);
        pause(SILENCE);
        stele("L E   R O I", nom, jours + " journees");
        System.out.println();
    }

    private static void stele(String titre, String nom, String sousTitre){
        String bord = "+" + "-".repeat(LARGEUR_STELE) + "+";
        System.out.println();
        System.out.println("       " + bord);
        System.out.println("       |" + centrer("", LARGEUR_STELE) + "|");
        System.out.println("       |" + centrer(titre, LARGEUR_STELE) + "|");
        System.out.println("       |" + centrer("", LARGEUR_STELE) + "|");
        System.out.println("       |" + centrer(nom, LARGEUR_STELE) + "|");
        System.out.println("       |" + centrer(sousTitre, LARGEUR_STELE) + "|");
        System.out.println("       |" + centrer("", LARGEUR_STELE) + "|");
        System.out.println("       " + bord);
        System.out.println();
    }

    private static String centrer(String texte, int largeur){
        if(texte.length() >= largeur) return texte.substring(0, largeur);
        int gauche = (largeur - texte.length()) / 2;
        return " ".repeat(gauche) + texte + " ".repeat(largeur - texte.length() - gauche);
    }

    public static int afficherMenu(Scanner sc){ // Affiche l'accueil et renvoie le choix (1 a 4), c'est Main qui decide quoi en faire
        catFichier("res/Menu.txt"); // le menu est une interface, pas une narration : affichage instantane
        return Menu.lireEntier(sc, 1, 5);
    }

    public static void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

}



