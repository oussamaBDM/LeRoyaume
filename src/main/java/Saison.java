public enum Saison {
	PRINTEMPS(1., 1., 1., 1., 1., 1.),
	ETE(1.5, 1.5, 1.5, 1.5, 1., 1.),
	AUTOMNE(0.6, 1.5, 1.5, 1., 1., 1.),
	HIVER(0.4, 0.6, 1., 1., 1., 1.); 
	
	private double mulBonheur;
	private double mulOr;
	private double mulNourriture;
	private double mulPopulation;
	private double mulArmee;
	private double mulPrestige;

	private Saison(double mB, double mO, double mN, double mPo, double mA, double mPr) {
		this.mulBonheur = mB;
		this.mulOr = mO;
		this.mulNourriture = mN;
		this.mulPopulation = mPo;
		this.mulArmee = mA;
		this.mulPrestige = mPr;
	}

	public double getMulBonheur() { return mulBonheur; }
	public double getMulOr() { return mulOr; }
	public double getMulNourriture() { return mulNourriture; }
	public double getMulPopulation() { return mulPopulation; }
	public double getMulArmee() { return mulArmee; }
	public double getMulPrestige() { return mulPrestige; }
	
	public static Saison getRandom() {
        Saison[] saisons = values();  // Récupère toutes les valeurs de l'énumération
        int index = (int) (Math.random() * saisons.length); // 0, 1, 2 ou 3
        return saisons[index];
    }

	// Applique le multiplicateur de la saison a un changement de jauge.
	// Seuls les gains sont modules : en hiver on gagne moins, mais une saison
	// ne doit jamais transformer une perte en cadeau (x0.4 sur un -10 donnerait -4).
	private static double moduler(double valeur, double multiplicateur){
		if(valeur <= 0){
			return valeur;
		}
		return valeur * multiplicateur;
	}

	public double appliquerBonheur(double valeur){ return moduler(valeur, this.mulBonheur); }
	public double appliquerOr(double valeur){ return moduler(valeur, this.mulOr); }
	public double appliquerNourriture(double valeur){ return moduler(valeur, this.mulNourriture); }
	public double appliquerPopulation(double valeur){ return moduler(valeur, this.mulPopulation); }
	public double appliquerArmee(double valeur){ return moduler(valeur, this.mulArmee); }
	public double appliquerPrestige(double valeur){ return moduler(valeur, this.mulPrestige); }

	private static String decrireEffet(String nom, double multiplicateur){
		if(multiplicateur == 1.){
			return "";
		}
		return nom + " x" + multiplicateur + "  ";
	}

	// Resume lisible des multiplicateurs de la saison, pour l'afficher au joueur
	public String effets(){
		String resume = decrireEffet("Bonheur", this.mulBonheur)
			+ decrireEffet("Or", this.mulOr)
			+ decrireEffet("Nourriture", this.mulNourriture)
			+ decrireEffet("Population", this.mulPopulation)
			+ decrireEffet("Armee", this.mulArmee)
			+ decrireEffet("Prestige", this.mulPrestige);
		if(resume.isEmpty()){
			return "gains normaux";
		}
		return "gains : " + resume.trim();
	}

}
