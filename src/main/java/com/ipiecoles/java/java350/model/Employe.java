package com.ipiecoles.java.java350.model;

import javax.persistence.*;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Objects;
import java.util.stream.Stream;

@Entity
public class Employe {


    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    private String nom;

    private String prenom;

    private String matricule;

    private LocalDate dateEmbauche;

    private Double salaire = Entreprise.SALAIRE_BASE;

    private Integer performance = Entreprise.PERFORMANCE_BASE;

    private Double tempsPartiel = 1.0;

    public Employe() {
    }

    public Employe(String nom, String prenom, String matricule, LocalDate dateEmbauche, Double salaire, Integer performance, Double tempsPartiel) {
        this.nom = nom;
        this.prenom = prenom;
        this.matricule = matricule;
        this.dateEmbauche = dateEmbauche;
        this.salaire = salaire;
        this.performance = performance;
        this.tempsPartiel = tempsPartiel;
    }

    /**
     * Méthode calculant le nombre d'années d'ancienneté à partir de la date d'embauche
     * @return
     */
    public Integer getNombreAnneeAnciennete() {
        if(dateEmbauche == null || dateEmbauche.isAfter(LocalDate.now())){
            return 0;
        }
        return LocalDate.now().getYear() - dateEmbauche.getYear();
    }

    public Integer getNbConges() {
        return Entreprise.NB_CONGES_BASE + this.getNombreAnneeAnciennete();
    }

    public Integer getNbRtt(){
        return getNbRtt(LocalDate.now());
    }

    /**
     * Fonction calculant le nombre de jours RTT à partir de la date
     * @param date,
     * @return le nombre de jours RTT en Integer
     */
    public Integer getNbRtt(LocalDate date){
        //Le nombre de jours total, varie si on est une année bisextille
        int nbJoursTotal = date.isLeapYear() ? 366 : 365;
        //nombre de semaines par an * 2 jours de weekend
        int nbJoursWeekend = 104;
        double nbJoursRTT;
        DayOfWeek firstDayOfYear = LocalDate.of(date.getYear(),1,1).getDayOfWeek();

        //nbJoursWeekend ne compte pas la 1ère semaine de l'année,
        //Si le 1er jour de l'année est un jour de week-end, on doit le compter. Si l'année est bisextille et le 1er jour de l'année est jeudi ou vendredi, on
        switch (firstDayOfYear){
            case THURSDAY:
                if(date.isLeapYear()) {
                    nbJoursWeekend++;
                }
                break;

            case FRIDAY:
                if(date.isLeapYear()) {
                    nbJoursWeekend += 2;
                }
                else {
                    nbJoursWeekend++;
                }
                break;

            case SATURDAY:
                nbJoursWeekend++;
                break;
        }

        //On parcourt les jours fériés, pour récupérer tout les jours qui ne sont pas samedi ou dimanche
        Stream<LocalDate> stream = Entreprise.joursFeries(date).stream().filter(localDate ->
                localDate.getDayOfWeek().getValue() <= DayOfWeek.FRIDAY.getValue());
        int nbJoursFeriesHorsWeekend = (int) stream.count();

        nbJoursRTT = (nbJoursTotal - Entreprise.NB_JOURS_MAX_FORFAIT - nbJoursWeekend - Entreprise.NB_CONGES_BASE - nbJoursFeriesHorsWeekend) * tempsPartiel;
        int resultat = (int) Math.ceil(nbJoursRTT);
        return resultat;
    }


    /**
     * Fonction calculant le nombre de jours RTT à partir de l'année
     * @param date
     * @return le nombre de jours RTT en Integer
     */
    public Integer getNbRtt2(LocalDate date){

        //Le nombre de jours total, varie si on est une année bisextille
        int nbJoursTotal = date.isLeapYear() ? 366 : 365;
        //nombre de semaines par an * 2 jours de weekend
        int nbJoursWeekend = 104;
        double nbJoursRTT;
        DayOfWeek firstDayOfYear = LocalDate.of(date.getYear(),1,1).getDayOfWeek();

        //nbJoursWeekend ne compte pas la 1ère semaine de l'année,
        //Si le 1er jour de l'année est un jour de week-end, on doit le compter. Si l'année est bisextille et le 1er jour de l'année est jeudi ou vendredi, on
        switch (firstDayOfYear){
            case THURSDAY:
                if(date.isLeapYear()) {
                    nbJoursWeekend++;
                }
                break;

            case FRIDAY:
                if(date.isLeapYear()) {
                    nbJoursWeekend += 2;
                }
                else {
                    nbJoursWeekend++;
                }
                break;

            case SATURDAY:
                nbJoursWeekend++;
                break;
        }

        //On parcourt les jours fériés, pour récupérer tout les jours qui ne sont pas samedi ou dimanche
        Stream<LocalDate> stream = Entreprise.joursFeries(date).stream().filter(localDate ->
                localDate.getDayOfWeek().getValue() <= DayOfWeek.FRIDAY.getValue());
        int nbJoursFeriesHorsWeekend = (int) stream.count();

        nbJoursRTT = (nbJoursTotal - Entreprise.NB_JOURS_MAX_FORFAIT - nbJoursWeekend - Entreprise.NB_CONGES_BASE - nbJoursFeriesHorsWeekend) * tempsPartiel;
        int resultat = (int) Math.ceil(nbJoursRTT);
        return resultat;
    }

    /**
     * Calcul de la prime annuelle selon la règle :
     * Pour les managers : Prime annuelle de base bonnifiée par l'indice prime manager
     * Pour les autres employés, la prime de base plus éventuellement la prime de performance calculée si l'employé
     * n'a pas la performance de base, en multipliant la prime de base par un l'indice de performance
     * (égal à la performance à laquelle on ajoute l'indice de prime de base)
     *
     * Pour tous les employés, une prime supplémentaire d'ancienneté est ajoutée en multipliant le nombre d'année
     * d'ancienneté avec la prime d'ancienneté. La prime est calculée au pro rata du temps de travail de l'employé
     *
     * @return la prime annuelle de l'employé en Euros et cents
     */
    //Matricule, performance, date d'embauche, temps partiel, prime
    public Double getPrimeAnnuelle(){
        //Calcule de la prime d'ancienneté
        Double primeAnciennete = Entreprise.PRIME_ANCIENNETE * this.getNombreAnneeAnciennete();
        Double prime;
        //Prime du manager (matricule commençant par M) : Prime annuelle de base multipliée par l'indice prime manager
        //plus la prime d'anciennté.
        if(matricule != null && matricule.startsWith("M")) {
            prime = Entreprise.primeAnnuelleBase() * Entreprise.INDICE_PRIME_MANAGER + primeAnciennete;
        }
        //Pour les autres employés en performance de base, uniquement la prime annuelle plus la prime d'ancienneté.
        else if (this.performance == null || Entreprise.PERFORMANCE_BASE.equals(this.performance)){
            prime = Entreprise.primeAnnuelleBase() + primeAnciennete;
        }
        //Pour les employés plus performance, on bonnifie la prime de base en multipliant par la performance de l'employé
        // et l'indice de prime de base.
        else {
            prime = Entreprise.primeAnnuelleBase() * (this.performance + Entreprise.INDICE_PRIME_BASE) + primeAnciennete;
        }
        //Au pro rata du temps partiel.
        return prime * this.tempsPartiel;
    }



    //Augmenter salaire
    public void augmenterSalaire(double pourcentage){
        if(this.salaire != null){
            this.salaire = Math.floor(this.salaire * pourcentage * 100) / 100;
        }
        else{
            throw new EntityNotFoundException();
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * @return the nom
     */
    public String getNom() {
        return nom;
    }

    /**
     * @param nom the nom to set
     */
    public Employe setNom(String nom) {
        this.nom = nom;
        return this;
    }

    /**
     * @return the prenom
     */
    public String getPrenom() {
        return prenom;
    }

    /**
     * @param prenom the prenom to set
     */
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    /**
     * @return the matricule
     */
    public String getMatricule() {
        return matricule;
    }

    /**
     * @param matricule the matricule to set
     */
    public void setMatricule(String matricule) {
        this.matricule = matricule;
    }

    /**
     * @return the dateEmbauche
     */
    public LocalDate getDateEmbauche() {
        return dateEmbauche;
    }

    /**
     * @param dateEmbauche the dateEmbauche to set
     */
    public void setDateEmbauche(LocalDate dateEmbauche) {
        this.dateEmbauche = dateEmbauche;
    }

    /**
     * @return the salaire
     */
    public Double getSalaire() {
        return salaire;
    }

    /**
     * @param salaire the salaire to set
     */
    public void setSalaire(Double salaire) {
        this.salaire = salaire;
    }

    public Integer getPerformance() {
        return performance;
    }

    public void setPerformance(Integer performance) {
        this.performance = performance;
    }

    public Double getTempsPartiel() {
        return tempsPartiel;
    }

    public void setTempsPartiel(Double tempsPartiel) {
        this.tempsPartiel = tempsPartiel;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Employe)) return false;
        Employe employe = (Employe) o;
        return Objects.equals(id, employe.id) &&
                Objects.equals(nom, employe.nom) &&
                Objects.equals(prenom, employe.prenom) &&
                Objects.equals(matricule, employe.matricule) &&
                Objects.equals(dateEmbauche, employe.dateEmbauche) &&
                Objects.equals(salaire, employe.salaire) &&
                Objects.equals(performance, employe.performance);
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Employe{");
        sb.append("id=").append(id);
        sb.append(", nom='").append(nom).append('\'');
        sb.append(", prenom='").append(prenom).append('\'');
        sb.append(", matricule='").append(matricule).append('\'');
        sb.append(", dateEmbauche=").append(dateEmbauche);
        sb.append(", salaire=").append(salaire);
        sb.append(", performance=").append(performance);
        sb.append(", tempsPartiel=").append(tempsPartiel);
        sb.append('}');
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, prenom, matricule, dateEmbauche, salaire, performance);
    }
}

