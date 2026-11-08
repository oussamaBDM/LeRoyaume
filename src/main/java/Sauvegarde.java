import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Sauvegarde{
    private static final String CHEMIN_DEFAUT = "src/save/sauvegarde.txt";
    private static final String DOSSIER = "src/save/";
    private static final String EXTENSION = ".txt";

    private static Path cheminPour(String nom){
        return Path.of(DOSSIER + nom + EXTENSION);
    }

    public static boolean sauvegardeExiste(String nom){
        return Files.exists(cheminPour(nom));
    }

    // Format : ligne 1 = le jour, ligne 2 = le roi et ses jauges, ligne 3 = saison, jour dans la saison, difficulte
    public static void sauvegarder(String nom, Roi roi, int jour, EventData etat){
        String contenu = jour + "\n" + roi.toSauvegarde() + "\n"
            + etat.getSaisonCurrente().name() + ";" + etat.getJourDansSaison()
            + ";" + roi.getDifficulte().name();
        try{
            Files.createDirectories(Path.of(DOSSIER)); // le dossier n'existe pas au premier lancement
            Files.writeString(cheminPour(nom), contenu);
            System.out.println("Partie sauvegardée sous \""+nom+"\" !");
        }catch(IOException e){
            System.out.println("Erreur lors de la sauvegarde : " + e.getMessage());
        }
    }  

    public static List<String> listerSauvegardes(){
        List<String> noms = new ArrayList<>();
        Path dossier = Path.of(DOSSIER);
        if(!Files.exists(dossier)){
            return noms;
        }
        try(DirectoryStream<Path> fichiers = Files.newDirectoryStream(dossier, "*"+EXTENSION)){
            for(Path fichier : fichiers){
                String nomFichier = fichier.getFileName().toString();
                noms.add(nomFichier.substring(0, nomFichier.length() - EXTENSION.length()));
            }
        }catch(IOException e){
            System.out.println("Erreur lors de la lecture des sauvegardes : " + e.getMessage());
        }
        return noms;
    }


    public static EtatCharge charger(String nom){
        if(!sauvegardeExiste(nom)){
            System.out.println("Aucune sauvegarde nommée \""+nom+"\" trouvée.");
            return null;
        }
        try{
            List<String> lignes = Files.readAllLines(cheminPour(nom));
            int jour = Integer.parseInt(lignes.get(0));
            Roi roi = Roi.depuisSauvegarde(lignes.get(1));
            Saison saison = Saison.getRandom();
            int jourDansSaison = 0;
            if(lignes.size() > 2){
                String[] p = lignes.get(2).split(";");
                saison = Saison.valueOf(p[0]);
                jourDansSaison = Integer.parseInt(p[1]);
                if(p.length > 2) roi.setDifficulte(Difficulte.valueOf(p[2]));
            }
            return new EtatCharge(roi, jour, saison, jourDansSaison);
        }catch(IOException | IllegalArgumentException | IndexOutOfBoundsException e){
            System.out.println("Erreur lors du chargement : " + e.getMessage());
            return null;
        }
    }

    public static class EtatCharge { //jour = nbTour
        public final Roi roi;
        public final int jour;
        public final Saison saison;
        public final int jourDansSaison;

        public EtatCharge(Roi roi, int jour, Saison saison, int jourDansSaison){
            this.roi = roi;
            this.jour = jour;
            this.saison = saison;
            this.jourDansSaison = jourDansSaison;
        }
    }
} 
