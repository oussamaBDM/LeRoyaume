import java.util.Scanner;

// Combien de jauges doivent etre a zero avant que le regne s'arrete.
// Les 6 jauges comptent : chacune est une facon de perdre.
public enum Difficulte {
    FACILE("Facile", 3),
    MOYEN("Moyen", 2),
    DIFFICILE("Difficile", 1);

    private final String nom;
    private final int jaugesVidesPourMourir;

    private Difficulte(String nom, int jaugesVidesPourMourir){
        this.nom = nom;
        this.jaugesVidesPourMourir = jaugesVidesPourMourir;
    }

    public String getNom(){
        return this.nom;
    }

    public int getJaugesVidesPourMourir(){
        return this.jaugesVidesPourMourir;
    }

    public static Difficulte demander(Scanner sc){
        System.out.println();
        System.out.println("Quelle difficulte pour votre regne ?");
        System.out.println();
        System.out.println("  [ 1 ] Facile     : il faut 3 jauges a zero pour tomber");
        System.out.println("  [ 2 ] Moyen      : 2 jauges vides suffisent");
        System.out.println("  [ 3 ] Difficile  : une seule jauge vide et tout s'arrete");
        System.out.println();
        System.out.println("  (les 6 jauges comptent : bonheur, or, nourriture, population, armee, prestige)");
        System.out.println();
        return values()[Menu.lireEntier(sc, 1, 3) - 1];
    }
}
