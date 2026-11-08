import java.util.List;
import java.util.Scanner;

public class EvenementTremblementDeTerre extends EvenementAbstrait{
    public EvenementTremblementDeTerre(){
        super("Tremblement De Terre", "res/evenements/evenement_Majeur/3*/*.txt");
    }

    public void appliquerEffets(Roi r, Ressource res, Scanner sc){
        sc = new Scanner(System.in);
        if (sc.nextInt() <= 0 || sc.nextInt() > 4){
            System.out.println("Choix incorrecte. Veuillez choisir parmi les options disponibles.");
        } else {
            int choix = sc.nextInt();
            List<String> listEffets = parcoursFichier(res,"res/evenements/evenement_Majeur/3*/*.csv");
            String[] line = listEffets.get(choix).split(",",1);
            res.addBonheur(Double.parseDouble(line[0]));
            res.addOr(Double.parseDouble(line[1]));
            res.addNourriture(Double.parseDouble(line[2]));
            res.addPopulation(Double.parseDouble(line[3]));
            res.addArmee(Double.parseDouble(line[4]));
            res.addPrestige(Double.parseDouble(line[5]));
        }
        sc.close();
    }
} 