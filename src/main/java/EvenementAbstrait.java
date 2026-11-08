import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public abstract class EvenementAbstrait implements Evenement{
    private String titre;
    private String chemin;
    private Saison saison = Saison.PRINTEMPS; // saison en cours, printemps = multiplicateurs neutres

    // constructor
    public EvenementAbstrait(String titre, String chemin){
        this.titre = titre;
        this.chemin = chemin;
    }
    // methodes 
    public String getTitre(){
        return this.titre;
    }
    public void executer(Roi roi, Ressource res, Scanner sc){
        System.err.println(this.getTitre());
        Affichage.catFichier(chemin);
        appliquerEffets(roi, res, sc);
    }

    public void executer(Roi roi, Ressource res, Scanner sc, Saison saison){
        this.saison = saison;
        executer(roi, res, sc);
    }

    // A utiliser dans appliquerEffets : Menu.appliquerLigne(res, ligne, getSaison())
    protected Saison getSaison(){
        return this.saison;
    }

    protected abstract void appliquerEffets(Roi roi, Ressource ress, Scanner sc);

    protected List<String> parcoursFichier(Ressource ress, String chemin){
        List< String > valEvenement = new ArrayList<String>();
        try{
            BufferedReader br = new BufferedReader(new FileReader(chemin));
                String line = br.readLine();
                while(line!=null){
                    line = br.readLine();
                    valEvenement.add(line);
                } 
                br.close();
        }catch(IOException IOE){
                System.out.println("Erreur de lecture de fichier.");
        }
        return valEvenement;
    }

    public boolean estDeclenchable(EventData event){
        return event.getTourActuel() == 0;
    }
}
