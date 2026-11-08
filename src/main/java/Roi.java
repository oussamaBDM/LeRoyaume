import java.util.Scanner;

public class Roi {
    private String nom;
    private Ressource ressources;
    private Difficulte difficulte = Difficulte.DIFFICILE; // par defaut : une seule jauge vide suffit

    public Roi(String nom, Ressource rs){
        this.ressources = rs;
        this.nom = validerNom(nom);
    }

    private String validerNom(String nom){
        if(nom == null || nom.isEmpty() || nom.length() > 20){
            throw new IllegalArgumentException("Le nom doit contenir entre 1 et 20 caractères");
        }
        return nom;
    }


    public static Roi creerRoi(Scanner sc, Ressource rs){
        Affichage.afficherLentement("Roi, donnez moi votre nom !", Affichage.DELAI_NARRATION);
        String nom = sc.nextLine();
        while(nom.isEmpty() || nom.length() > 20){
            System.out.println("Votre nom ne convient pas mon Roi, entrez un nom entre 1 et 20 caractères :");
            nom = sc.nextLine();
        }
        return new Roi(nom, rs);
    }

    public String getNom(){
        return this.nom;
    }

    public Difficulte getDifficulte(){
        return this.difficulte;
    }

    public void setDifficulte(Difficulte difficulte){
        this.difficulte = difficulte;
    }

    // Les 6 jauges tuent, sans exception : une jauge vide est une facon de perdre.
    public static final int NB_JAUGES = 6;

    public int jaugesVides(){
        int vides = 0;
        if(this.ressources.getBonheur() == 0) vides++;
        if(this.ressources.getOr() == 0) vides++;
        if(this.ressources.getNourriture() == 0) vides++;
        if(this.ressources.getPopulation() == 0) vides++;
        if(this.ressources.getArmee() == 0) vides++;
        if(this.ressources.getPrestige() == 0) vides++;
        return vides;
    }

    public String toString(){
        return this.nom+" :\n"+this.ressources.toString();
    }


    public Ressource getRessources(){
        return this.ressources;
    }

    // La phrase qui clot le regne, ou null si le roi tient encore debout.
    // C'est Affichage qui l'affiche : une methode qui repond oui ou non n'a pas a parler.
    public String causeDeLaMort(){
        if(jaugesVides() < this.difficulte.getJaugesVidesPourMourir()){
            return null; // pas encore assez de jauges a sec pour cette difficulte
        }
        if(jaugesVides() == NB_JAUGES){
            return "Votre règne s'achève ici, vous êtes le pire Roi ayant existé.";
        }else if(this.ressources.getBonheur() == 0){
            return "Votre règne s'achève ici, le peuple s'est révolté et vous a chassé du trône.";
        }else if(this.ressources.getOr() == 0){
            return "Votre règne s'achève ici, vous mourrez dans la honte et la pauvreté.";
        }else if(this.ressources.getNourriture() == 0){
            return "Votre règne s'achève ici, vous mourrez dans la honte et le ventre vide.";
        }else if(this.ressources.getPopulation() == 0){
            return "Votre règne s'achève ici, vous mourrez dans la honte et sous les pieds de votre peuple.";
        }else if(this.ressources.getArmee() == 0){
            return "Votre règne s'achève ici, sans armée vos ennemis ont franchi vos murs.";
        }else if(this.ressources.getPrestige() == 0){
            return "Votre règne s'achève ici, plus personne dans le royaume ne vous reconnaît pour roi.";
        }
        return null;
    }

    public boolean estVivant(){
        return causeDeLaMort() == null;
    }

    public String toSauvegarde(){
        return this.nom + "|" + this.ressources.toSauvegarde();
    }

    public static Roi depuisSauvegarde(String ligne){
        String[] p = ligne.split("\\|", 2);
        return new Roi(p[0], Ressource.depuisSauvegarde(p[1]));
    }

    public static void main(String [] args){
        Scanner sc = new Scanner(System.in);
        //Ressource ressources = new Ressource(4,10,5,9,8,6);
        Ressource ressources2 = new Ressource(4,0,5,9,8,6);
        //Ressource ressources3 = new Ressource(15,0,0,0,8,6);

        
        Roi roi = Roi.creerRoi(sc, ressources2);
        Affichage.afficherLentement("\nLongue vie au Roi "+roi.nom+"\n", Affichage.DELAI_NARRATION);
        Affichage.afficherLentement("\nBienvenue dans votre Royaume !\n", Affichage.DELAI_NARRATION);
        System.out.println(roi.toString()+"\n");
        sc.close();
        System.out.println(roi.causeDeLaMort());

    }

}
