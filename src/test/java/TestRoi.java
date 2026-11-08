// Tests des regles de mort du roi selon la difficulte.
public class TestRoi {

    static int reussis = 0;
    static int total = 0;

    public static void main(String[] args){
        System.out.println("Tests de Roi :");
        testBonheurAZeroTue();
        testArmeeEtPrestigeTuentAussi();
        testDifficulteChangeLeSeuil();

        System.out.println();
        System.out.println(reussis + " / " + total + " tests reussis");
    }

    // Le bug signale : bonheur a 0 ne tuait pas, meme en difficile
    static void testBonheurAZeroTue(){
        Roi roi = new Roi("Arthur", new Ressource(0, 50, 50, 50, 50, 50));
        roi.setDifficulte(Difficulte.DIFFICILE);
        verifier("bonheur a 0 en difficile : le roi tombe", !roi.estVivant());
        verifier("et la cause parle de revolte",
            roi.causeDeLaMort() != null && roi.causeDeLaMort().contains("révolté"));
    }

    // Toutes les jauges tuent, armee et prestige compris
    static void testArmeeEtPrestigeTuentAussi(){
        Roi armee = new Roi("Arthur", new Ressource(50, 50, 50, 50, 0, 50));
        armee.setDifficulte(Difficulte.DIFFICILE);
        verifier("armee a 0 en difficile : le roi tombe", !armee.estVivant());

        Roi prestige = new Roi("Arthur", new Ressource(50, 50, 50, 50, 50, 0));
        prestige.setDifficulte(Difficulte.DIFFICILE);
        verifier("prestige a 0 en difficile : le roi tombe", !prestige.estVivant());
    }

    // Une seule jauge vide : mortel en difficile, pas en moyen ni en facile
    static void testDifficulteChangeLeSeuil(){
        Ressource uneSeuleVide = new Ressource(50, 0, 50, 50, 50, 50);
        verifier("1 jauge vide, facile    : vivant", vivant(uneSeuleVide, Difficulte.FACILE));
        verifier("1 jauge vide, moyen     : vivant", vivant(uneSeuleVide, Difficulte.MOYEN));
        verifier("1 jauge vide, difficile : mort", !vivant(uneSeuleVide, Difficulte.DIFFICILE));
    }

    static boolean vivant(Ressource ressources, Difficulte difficulte){
        Roi roi = new Roi("Arthur", ressources.copie());
        roi.setDifficulte(difficulte);
        return roi.estVivant();
    }

    static void verifier(String message, boolean condition){
        total++;
        if(condition){
            reussis++;
            System.out.println("  OK     " + message);
        }else{
            System.out.println("  ECHEC  " + message);
        }
    }
}
