package modele;

public class Depot {
    private int id, montant, depot_recurrence;
    private String nom;

    public Depot(int id, int montant, int depot_recurrence, String nom) {
        this.id = id;
        this.montant = montant;
        this.depot_recurrence = depot_recurrence;
        this.nom = nom;
    }

    public int getId() {
        return id;
    }

    public int getMontant() {
        return montant;
    }

    public int getDepot_recurrence() {
        return depot_recurrence;
    }

    public String getNom() {
        return nom;
    }
    @Override
    public String toString() {
        return "Depot{id=" + id + ", name='" + nom + "', ...}";
    }
}
