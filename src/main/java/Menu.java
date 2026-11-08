import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Menu{

    public static List<String[]> lireChoix(String chemin){ // Lit un csv d'evenement, une case par colonne, l'en-tete est ignoree
        List<String[]> choix = new ArrayList<String[]>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(chemin));
            String ligne = br.readLine(); // en-tete : response_text,happiness_change etc
            ligne = br.readLine();
            while(ligne != null){
                if(!ligne.trim().isEmpty()){
                    choix.add(ligne.split(","));
                }
                ligne = br.readLine();
            }
            br.close();
        }catch(IOException e){
            System.out.println("Erreur de lecture de fichier : " + e.getMessage());
        }
        return choix;
    }

    public static void afficherChoix(List<String[]> choix){ // Affiche les reponses possibles, numerotees a partir de 1
        afficherChoix(choix, null);
    }

    public static void afficherChoix(List<String[]> choix, String optionSupplementaire){ // idem, avec une entree de jeu en plus (sauvegarde...)
        System.out.println();
        for(int i = 0; i < choix.size(); i++){
            System.out.println("  [ " + (i + 1) + " ] " + choix.get(i)[0]);
        }
        if(optionSupplementaire != null){
            System.out.println("  [ " + (choix.size() + 1) + " ] " + optionSupplementaire);
        }
        System.out.println();
    }

    public static int lireEntier(Scanner sc, int min, int max){ // Redemande tant que la reponse n'est pas un nombre entre min et max
        int valeur = min;
        boolean valide = false;
        while(!valide){
            System.out.print("Votre choix (" + min + "-" + (max-1) + ") : ");
            String reponse = sc.nextLine().trim();
            try{
                valeur = Integer.parseInt(reponse);
                if(valeur < min || valeur > max){
                    System.out.println("Choix incorrect, veuillez choisir parmi les options disponibles.");
                }else{
                    valide = true;
                }
            }catch(NumberFormatException e){
                System.out.println("Ce n'est pas un nombre mon Roi, reessayez.");
            }
        }
        return valeur;
    }

    public static int demanderChoix(Scanner sc, List<String[]> choix){ // Affiche les choix et renvoie l'indice choisi (0 = premier), -1 si la liste est vide
        if(choix.isEmpty()){
            System.out.println("Aucun choix disponible.");
            return -1;
        }
        afficherChoix(choix);
        return lireEntier(sc, 1, choix.size()) - 1;
    }

    public static final String OPTION_SAUVEGARDE = "Sauvegarder et quitter";

    public static int demanderChoixOuSauvegarde(Scanner sc, List<String[]> choix){
        if(choix.isEmpty()){
            System.out.println("Aucun choix disponible.");
            return -1;
        }
        afficherChoix(choix, OPTION_SAUVEGARDE);
        return lireEntier(sc, 1, choix.size() + 1) - 1;
    }

    public static String[] demanderChoixCsv(Scanner sc, String chemin){ // Raccourci : lit le csv, affiche les choix et renvoie la ligne choisie
        List<String[]> choix = lireChoix(chemin);
        int indice = demanderChoix(sc, choix);
        if(indice < 0){
            return null;
        }
        return choix.get(indice);
    }

    public static void appliquerLigne(Ressource res, String[] ligne){ //applique les 6 colonnes de la ligne choisie aux jauges, sans effet de saison
        appliquerLigne(res, ligne, Saison.PRINTEMPS); // printemps = tous les multiplicateurs a 1
    }

    public static void appliquerLigne(Ressource res, String[] ligne, Saison saison){ //idem, mais les gains sont modules par la saison en cours
        res.addBonheur(saison.appliquerBonheur(Double.parseDouble(ligne[1])));
        res.addOr(saison.appliquerOr(Double.parseDouble(ligne[2])));
        res.addNourriture(saison.appliquerNourriture(Double.parseDouble(ligne[3])));
        res.addPopulation(saison.appliquerPopulation(Double.parseDouble(ligne[4])));
        res.addArmee(saison.appliquerArmee(Double.parseDouble(ligne[5])));
        res.addPrestige(saison.appliquerPrestige(Double.parseDouble(ligne[6])));
    }

}
