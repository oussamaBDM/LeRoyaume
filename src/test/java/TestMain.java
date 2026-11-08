import java.io.File;


public class TestMain {

    static int reussis = 0;
    static int total = 0;

    public static void main(String[] args){
        System.out.println("Tests de Main :");
        testExtraireNumero();
        testEvenementsTriesParNumero();
        testChaqueEvenementAUnTxtEtUnCsv();

        System.out.println();
        System.out.println(reussis + " / " + total + " tests reussis");
    }

    static void testExtraireNumero(){
        verifier("42_Chat_Disparu donne 42",
            Main.extraireNumero("res/evenements/evenement_Mineur/42_Chat_Disparu/42_Chat_Disparu") == 42);
        verifier("1_La_Grande_Famine donne 1",
            Main.extraireNumero("res/evenements/evenement_Majeur/1_La_Grande_Famine/1_La_Grande_Famine") == 1);
    }

    // Test 2 : les evenements sortent tries du plus petit numero au plus grand,
    static void testEvenementsTriesParNumero(){
        String[] evenements = Main.scanEvenements("res/evenements");
        boolean trie = true;
        for(int i = 1; i < evenements.length; i++){
            if(Main.extraireNumero(evenements[i - 1]) > Main.extraireNumero(evenements[i])){
                trie = false;
            }
        }
        verifier("les " + evenements.length + " evenements sont tries par numero croissant", trie);
    }

    // Test 3 : la boucle de jeu lit chemin+".txt" puis chemin+".csv",
    static void testChaqueEvenementAUnTxtEtUnCsv(){
        String[] evenements = Main.scanEvenements("res/evenements");
        String manquant = null;
        for(String chemin : evenements){
            if(!new File(chemin + ".txt").exists() || !new File(chemin + ".csv").exists()){
                manquant = chemin;
            }
        }
        verifier("chaque evenement a son .txt et son .csv"
            + (manquant == null ? "" : " -> il manque un fichier pour " + manquant), manquant == null);
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
