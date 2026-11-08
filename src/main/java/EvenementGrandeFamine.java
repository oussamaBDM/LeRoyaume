import java.util.List;
import java.util.Scanner;


public class EvenementGrandeFamine extends EvenementAbstrait{
    public EvenementGrandeFamine(){
        super("GrandeFamine", "res/evenements/evenement_Majeur/1*/*.txt");
    }
    public void appliquerEffets(Roi r, Ressource res, Scanner sc){
        sc = new Scanner(System.in);

        if (sc.nextInt() <= 0 || sc.nextInt() > 4) System.out.println("Choix incorrecte. Veuillez choisir parmi les options disponibles.");
        else {
        int choix = sc.nextInt();
        List<String> listEffets = parcoursFichier(res,"res/evenements/evenement_Majeur/1*/*.csv");
        String[] line = listEffets.get(choix).split(",",1);
        res.addBonheur(Double.parseDouble(line[1]));
        res.addOr(Double.parseDouble(line[1]));
        res.addNourriture(Double.parseDouble(line[1]));
        res.addPopulation(Double.parseDouble(line[1]));
        res.addArmee(Double.parseDouble(line[1]));
        res.addPrestige(Double.parseDouble(line[1]));
        sc.close();
        }
    }
}
