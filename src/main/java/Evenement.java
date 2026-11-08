import java.util.Scanner;

public interface Evenement {
    String getTitre();
    boolean estDeclenchable(EventData event);
    void executer(Roi roi, Ressource res, Scanner sc);

    // Variante avec la saison en cours : par defaut on ignore la saison
    default void executer(Roi roi, Ressource res, Scanner sc, Saison saison){
        executer(roi, res, sc);
    }
}
