

class Ressource {
    
    //attributes

    private double Bonheur;
    private double Or;
    private double Nourriture;
    private double Population;
    private double Armee;
    private double Prestige;

    
    //constructor

    Ressource(double Bonheur, double Or, double Nourriture, double Population, double Armee, double Prestige) {
        this.Bonheur = Bonheur;
        this.Or = Or;
        this.Nourriture = Nourriture;
        this.Population = Population;
        this.Armee = Armee;
        this.Prestige = Prestige;
    }
    Ressource(){
        this(50, 50, 50, 50, 50, 50);
    }

    //methods

    // Photo des jauges a un instant T, pour pouvoir afficher les variations apres un choix
    public Ressource copie(){
        return new Ressource(this.Bonheur, this.Or, this.Nourriture, this.Population, this.Armee, this.Prestige);
    }

    @Override
    public String toString() {
        
        return 
        " Bonheur: " + Bonheur + "\n" +
        " Or: " + Or + "\n" +
        " Nourriture: " + Nourriture + "\n" +
        " Population: " + Population + "\n" +
        " Armee: " + Armee + "\n" +
        " Prestige " + Prestige;
    }    
    
        // Getters

    public double getBonheur(){
        return this.Bonheur;
    }
    
    public double getOr(){
        return this.Or;
    }
    
    public double getNourriture(){
        return this.Nourriture;
    }
    
    public double getPopulation(){
        return this.Population;
    }
    
    public double getArmee(){
        return this.Armee;
    }

    public double getPrestige() {
        return this.Prestige;
    }

    // Setters

    public void setBonheur(double Bonheur){
        this.Bonheur = Bonheur;
        if (getBonheur() > 100) setBonheur(100);
        else if (getBonheur() < 0) setBonheur(0);
    }
    
    public void setOr(double Or){
        this.Or = Or;
        if (getOr() > 100) setOr(100);
        else if (getOr() < 0) setOr(0);
    }
    
    public void setNourriture(double Nourriture){
        this.Nourriture = Nourriture;
        if (getNourriture() > 100) setNourriture(100);
        else if (getNourriture() < 0) setNourriture(0);
    }
    
    public void setPopulation(double Population) {
        this.Population = Population;
        if (getPopulation() > 100) setPopulation(100);
        else if (getPopulation() < 0) setPopulation(0);
    }
    
    public void setArmee(double Armee) {
        this.Armee = Armee;
        if (getArmee() > 100) setArmee(100);
        else if (getArmee() < 0) setArmee(0);
    }
    
    public void setPrestige(double Prestige) {
        this.Prestige = Prestige;
        if (getPrestige() > 100) setPrestige(100);
        else if (getPrestige() < 0) setPrestige(0);
    }


        // adder

    public void addBonheur(double Bonheur) {
        setBonheur(getBonheur() + Bonheur);
    }

    public void addOr(double Or) {
        setOr(getOr() + Or);
    }

    public void addNourriture(double Nourriture) {
        setNourriture(getNourriture() + Nourriture);
    }

    public void addPopulation(double Population) {
        setPopulation(getPopulation() + Population);
    }
    
    public void addArmee(double Armee) {
        setArmee(getArmee() + Armee);
    }

    public void addPrestige(double prestige) {
        setPrestige(getPrestige() + prestige);
    }

    public String toSauvegarde(){
        return this.Bonheur+";"+this.Or+";"+this.Nourriture+";"+this.Population+";"+this.Armee+";"+this.Prestige;
    }

    public static Ressource depuisSauvegarde(String ligne){
        String[] p = ligne.split(";");
        return new Ressource(
            Double.parseDouble(p[0]), Double.parseDouble(p[1]), Double.parseDouble(p[2]),
            Double.parseDouble(p[3]), Double.parseDouble(p[4]), Double.parseDouble(p[5])
        );
    }

}
