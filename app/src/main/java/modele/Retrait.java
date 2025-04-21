package modele;

public class Retrait {
    private int id;
    private int montant;
    private int retrait_recurrence;
    private String nom;

    public Retrait(int id, int montant, int retrait_recurrence, String nom) {
        this.id = id;
        this.montant = montant;
        this.retrait_recurrence = retrait_recurrence;
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public int getMontant() {
        return montant;
    }

    public int getRetrait_recurrence() {
        return retrait_recurrence;
    }

    public String getNom() {
        return nom;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setMontant(int montant) {
        this.montant = montant;
    }

    public void setRetrait_recurrence(int retrait_recurrence) {
        this.retrait_recurrence = retrait_recurrence;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
