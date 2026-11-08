import java.util.Scanner;

public class EvenementIntronisation extends EvenementAbstrait{
    // le super contructor 
    public EvenementIntronisation(){
        super("Intronisation", "res/intro.txt");
    }
        //les affectage methodes voir initialisation de jeu

    public void appliquerEffets(Roi roi, Ressource res, Scanner sc){

            res.setBonheur(25);
            res.setNourriture(50);
            res.setArmee(25);
            res.setOr(25);
            res.setPopulation(50);
            res.setPrestige(50);
            System.out.println("Le peuple a acclamé votre coronnement ! Hurray!!!");
        }
    }
