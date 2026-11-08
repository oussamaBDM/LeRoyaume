import java.util.ArrayList;
import java.util.List;

public class EventData {
    public static final int DUREE_SAISON = 25; // nombre de jours avant un changement de saison

    private int tourActuel; // 0 à 99
    private int jourDansSaison; //0 à 25;
    private Roi roi;// Dans ta classe Jeu (ou Main)

    private boolean estTerminee; // Status de la partie
    private Saison saisonCurrente; // HIVER, PRINTEMPS, AUTOMNE, ETE 
    private List<String> memoireEvenements;
    
    
    public EventData (Roi roi){
        this(roi, 0, Saison.getRandom(), 0);
    }

    // Reprise d'une sauvegarde : on repart au bon tour, dans la bonne saison
    public EventData (Roi roi, int tourActuel, Saison saison, int jourDansSaison){
        this.tourActuel = tourActuel;
        this.jourDansSaison = jourDansSaison;
        this.saisonCurrente = saison;
        this.roi = roi;
        this.estTerminee = false;
        this.memoireEvenements = new ArrayList<>();
    }
    
    public void avanceTour(){
        this.tourActuel++;
        this.jourDansSaison++;

        if (this.jourDansSaison >= DUREE_SAISON){
            this.jourDansSaison = 0;
            this.saisonCurrente = Saison.getRandom();
            System.out.println("La saison change ! Nous sommes maintenant en " + this.saisonCurrente
                + " (" + this.saisonCurrente.effets() + ")");
        }
    }
    //   Gestion de la memoire
    public void ajouteEvenementVu(String titre){
        if (!this.memoireEvenements.contains(titre)) this.memoireEvenements.add(titre);
    }
    public boolean evenementDejaVu(String titre){
        return this.memoireEvenements.contains(titre);
    }
    
   
   //Fin de la partie
   public void finPartie(){
    if (!this.roi.estVivant()) this.estTerminee = true;
   }
    
    
   //Getter
    public Roi getRoi(){
        return this.roi;
    }
    public int getJourDansSaison() {
        return jourDansSaison;
    }
    public List<String> getMemoireEvenements() {
        return memoireEvenements;
    }
    public Saison getSaisonCurrente() {
        return saisonCurrente;
    }
    public int getTourActuel() {
        return tourActuel;
    }
    public Ressource getRessources(){
        return roi.getRessources();
    }
    public boolean getEstTerminee(){
        return this.estTerminee;
    }
   
  // Setter de la fin de partie manuellement 
    
    public void terminerPartie(){
        this.estTerminee = true;
    }
    
    
}
