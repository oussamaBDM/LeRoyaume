import java.io.File;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class Main {
	final String INTRO_PATH = "res/intro.txt";
	final String TITLE_PATH = "res/title.txt";

	final String KING_PATH = "res/ascii/king.txt";
	final String KNIGHT_PATH = "res/ascii/knight.txt";
	final String PEASENT_PATH = "res/ascii/peasent.txt";
	final String WIZARD_PATH = "res/ascii/wizard.txt";
	final String SKULL_PATH = "res/ascii/skull.txt";

	final String EVENEMENTS_MIN_DOSSIER = "res/evenements/evenement_Mineur";
	final String EVENEMENTS_MAJ_DOSSIER = "res/evenements/evenement_Majeur";

	// un dossier par evenement, le .txt pour le lore et le .csv pour les choix
	final String[] EVENEMENTS_MIN = scanEvenements(EVENEMENTS_MIN_DOSSIER);
	final String[] EVENEMENTS_MAJ = scanEvenements(EVENEMENTS_MAJ_DOSSIER);

	boolean running = true;
	int jour = 0;
	Roi roi;
	String nomSauvegardeActuelle;

	// parcourt recursivement le dossier et recupere le chemin (sans extension)
	static String[] scanEvenements(String dossier) {
		List<String> result = new ArrayList<>();
		remplirEvenements(new File(dossier), result);
		result.sort(Comparator.comparingInt(Main::extraireNumero));
		return result.toArray(new String[0]);
	}

	static void remplirEvenements(File dossier, List<String> result) {
		for (File f : dossier.listFiles()) {
			if (f.isDirectory()) {
				remplirEvenements(f, result);
			} else if (f.getName().endsWith(".txt")) {
				result.add(f.getPath().replace(".txt", "").replace("\\", "/"));
			}
		}
	}

	static int extraireNumero(String chemin) {
		String nom = chemin.substring(chemin.lastIndexOf("/") + 1);
		return Integer.parseInt(nom.split("_")[0]);
	}

	public void initialisation(Scanner sc) {
		Ressource ressources = new Ressource(100, 100, 100, 100, 100, 100);
		Difficulte difficulte = Difficulte.demander(sc);
		roi = Roi.creerRoi(sc, ressources);
		roi.setDifficulte(difficulte);
		System.out.println();
		Affichage.catFichier(TITLE_PATH);
		Affichage.afficherLentement("Bienvenue dans votre Royaume !", Affichage.DELAI_NARRATION);
		Affichage.afficherLentement("Longue vie au Roi " + roi.getNom(), Affichage.DELAI_NARRATION);
		
	}

	public void jeu(Scanner sc) {
		jeu(sc, new EventData(roi)); // nouvelle partie : saison tiree au hasard
	}

	public void jeu(Scanner sc, EventData etat) {
		//intro jeu, sautee quand on reprend une sauvegarde
		if (jour == 0) {
			Affichage.catFichier(INTRO_PATH);
			Affichage.attendreEntree(sc);
			Affichage.clearScreen();
			Affichage.banniere("Votre regne commence en " + etat.getSaisonCurrente()
				+ " (" + etat.getSaisonCurrente().effets() + ")");
			Affichage.catFichier(KING_PATH, KNIGHT_PATH, 10); // pop juste avant les jauges, sinon il scrolle et on ne le voit plus
			Affichage.afficherJauges(roi, null); // debut de partie : rien a comparer
		}
		Random rand = new Random();
		int eventNumber;


		ArrayList<String> event = new ArrayList<>();
		event.add(KNIGHT_PATH);
		event.add(WIZARD_PATH);
		event.add(PEASENT_PATH);


		//test event.add(v

		//boucle jeu
		while (running && jour < 100) {
			Collections.shuffle(event);
			String chemin;
			
			
			if (jour != 49 && jour != 99) { 
				eventNumber = rand.nextInt(EVENEMENTS_MIN.length);
				chemin = EVENEMENTS_MIN[eventNumber];
			} else {
				eventNumber = rand.nextInt(EVENEMENTS_MAJ.length);
				chemin = EVENEMENTS_MAJ[eventNumber];
			}
			
			Saison saison = etat.getSaisonCurrente();
			System.out.println();
			System.out.println(Affichage.gras("--- Saison : " + saison + " (jour " + (etat.getJourDansSaison() + 1)
				+ "/" + EventData.DUREE_SAISON + ") | " + saison.effets() + " ---"));
			

			
			Affichage.catFichierProgressif(chemin + ".txt");
			List<String[]> choix = Menu.lireChoix(chemin + ".csv");
			int indice = Menu.demanderChoixOuSauvegarde(sc, choix);

			if (indice == choix.size()) { // derniere entree du menu : sauvegarder et quitter
				Sauvegarde.sauvegarder(roi.getNom(),roi, jour, etat);
				running = false;
			} else {
				if (indice >= 0) { // csv vide ou illisible : la journee passe sans effet
					String[] ligne = choix.get(indice);
					Ressource avant = roi.getRessources().copie(); // pour afficher ce que le choix a change
					int videsAvant = roi.jaugesVides();
					Menu.appliquerLigne(roi.getRessources(), ligne, saison);
					Affichage.clearScreen();
					Affichage.afficherLentement("\nVous avez choisi : " + ligne[0], Affichage.DELAI_NARRATION);
					if (jour == 49 || jour == 99) Affichage.catFichier(KING_PATH,SKULL_PATH,10);
					else Affichage.catFichier(KING_PATH,event.get(0),10); // pop juste avant les jauges, sinon il scrolle et on ne le voit plus
					Affichage.afficherJauges(roi, avant);
					// if (roi.jaugesVides() > videsAvant) {
					// 	Son.jouer(Son.ALERTE); // une jauge vient de tomber a zero
					// }
				}
				jour++;
				etat.avanceTour(); // avance le jour et change de saison tous les DUREE_SAISON jours
				if (jour >= 100 || !roi.estVivant()) {
					running = false;
				}
			}
			System.out.println("================================================================================");
		}

		// epilogue : rien du tout si le joueur a simplement sauvegarde et quitte
		if (!roi.estVivant()) {
			Affichage.afficherFin(roi.causeDeLaMort(), roi.getNom(), jour);
		} else if (jour >= 100) {
			Affichage.afficherFinVictoire(roi.getNom(), jour);
		}
	}

	// Boucle "evenements objets" : encore en chantier (les EvenementXxx ne lisent
	// // pas encore leurs csv correctement), donc main() utilise jeu() pour l'instant.
	// public void lancerJeu() {
	// 	Scanner scanner = new Scanner(System.in);
	// 	Ressource ressources = new Ressource(50, 50, 50, 50, 50, 50);
	// 	Roi roi = Roi.creerRoi(scanner, ressources);
	// 	EventData event = new EventData(roi);

	// 	List<Evenement> tousLesEvenements = new ArrayList<>();
	// 	tousLesEvenements.add(new EvenementIntronisation());   // fixe
	// 	// tousLesEvenements.add(new EvenementMarchand());        // aléatoire
	// 	// tousLesEvenements.add(new EvenementRevolte());         // préférentiel
	// 	// tousLesEvenements.add(new EvenementEpidemie());        // aléatoire
	// 	// ... ajoute tous tes événements ici

	// 	while (!event.getEstTerminee() && event.getTourActuel() < 100) {
	// 		System.out.println("\n========== TOUR " + event.getTourActuel() + " ==========");
	// 		System.out.println("Saison: " + event.getSaisonCurrente() + " (Jour " + event.getJourDansSaison()
	// 			+ "/" + EventData.DUREE_SAISON + ") | " + event.getSaisonCurrente().effets());
	// 		System.out.println(ressources);

	// 		// 1. Sélectionner les événements disponibles
	// 		List<Evenement> disponibles = new ArrayList<>();
	// 		for (Evenement e : tousLesEvenements) {
	// 			if (e.estDeclenchable(event)) {
	// 				disponibles.add(e);
	// 			}
	// 		}

	// 		// 2. S'il y en a, en choisir un au hasard
	// 		if (!disponibles.isEmpty()) {
	// 			Evenement choisi = disponibles.get((int)(Math.random() * disponibles.size()));
	// 			choisi.executer(roi, roi.getRessources(), scanner, event.getSaisonCurrente());
	// 		}

	// 		// 3. Fin du tour : appliquer la consommation, etc.
	// 		// (On verra ça plus tard)
	// 		// appliquerFinDeTour(event);

	// 		// 4. Vérifier les conditions de game over (jauge vide = le roi tombe)
	// 		event.finPartie();

	// 		// 5. Avancer dans le temps (incremente le tour, change la saison tous les 25 jours)
	// 		event.avanceTour();
	// 	}

	// 	// Victoire ou défaite
	// 	if (event.getTourActuel() >= 100 && !event.getEstTerminee()) {
	// 		System.out.println("VICTOIRE ! Vous avez régné 100 tours !");
	// 	}
	// 	scanner.close();
	// }

	// Affiche les sauvegardes disponibles et demande au joueur laquelle charger.
	// Retourne null si aucune sauvegarde n'existe ou si le joueur annule.
	private static Sauvegarde.EtatCharge demanderChargement(Scanner sc, Main app) {
		List<String> sauvegardes = Sauvegarde.listerSauvegardes();
		if (sauvegardes.isEmpty()) {
			System.out.println("Aucune sauvegarde disponible.");
			return null;
		}

		System.out.println("Sauvegardes disponibles :");
		for (String nom : sauvegardes) {
			System.out.println(" - " + nom);
		}
		System.out.println("Quel nom charger ? (laisser vide pour annuler)");
		String nomChoisi = sc.nextLine().trim();
		if (nomChoisi.isEmpty()) {
			return null;
		}

		Sauvegarde.EtatCharge etat = Sauvegarde.charger(nomChoisi);
		if (etat != null) {
			app.nomSauvegardeActuelle = nomChoisi; // pour re-sauvegarder sous le meme nom plus tard
		}
		return etat;
	}
	public static void main(String[] args) {

		Scanner sc = new Scanner(System.in);
		Main app = new Main();
		boolean continuer = true;

		while (continuer) {

			int choix = Affichage.afficherMenu(sc);
			if (choix == 1) {
				app.initialisation(sc);
				app.jeu(sc);
				continuer = false;

			} else if (choix == 2) {
				Affichage.catFichier("INFO-JEU-GENERAL");
			} else if (choix == 3) {
				Sauvegarde.EtatCharge etat = demanderChargement(sc, app);
				if (etat != null) { // sinon charger() a deja prevenu, on revient au menu
					app.roi = etat.roi;
					app.jour = etat.jour;
					System.out.println("Vous reprenez votre regne au jour " + app.jour
						+ ", en " + etat.saison + ".");
					app.jeu(sc, new EventData(etat.roi, etat.jour, etat.saison, etat.jourDansSaison));
					continuer = false;
				}
			} else if (choix == 5) {
				app.initialisation(sc); // cree le roi normalement (nom, difficulte)
				int jourVoulu = Menu.lireEntier(sc, 0, 99);
				app.jour = jourVoulu;
				EventData etat = new EventData(app.roi, jourVoulu, Saison.getRandom(), jourVoulu % EventData.DUREE_SAISON);
				app.jeu(sc, etat);
				continuer = false;
			} else {
				System.out.println("Vous n'etes pas digne d'etre Roi");
				continuer = false;
			}
		}
		sc.close();
	}
}