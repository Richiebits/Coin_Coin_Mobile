package modele;

public class Depense {
    private String id, date, montant;

    public Depense(String id, String date, String montant){
        this.id = id;
        this.date = date;
        this.montant = montant;
    }

    public String getId() {
        return id;
    }

    public String getDate() {
        return date;
    }

    public String getMontant() {
        return montant;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setMontant(String montant) {
        this.montant = montant;
    }

}