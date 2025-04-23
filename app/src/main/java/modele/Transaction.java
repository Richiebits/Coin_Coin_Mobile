package modele;

public class Transaction {
    private int id;
    private int montant;
    private String date;
    private String type;

    public Transaction(int id, int montant, String date, String type) {
        this.id = id;
        this.montant = montant;
        this.date = date;
        this.type = type;
    }

    // Getters
    public int getId() {
        return id;
    }

    public int getMontant() {
        return montant;
    }

    public String getDate() {
        return date;
    }

    public String getType() {
        return type;
    }

    // Setters
    public void setId(int id) {
        this.id = id;
    }

    public void setMontant(int montant) {
        this.montant = montant;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "id=" + id +
                ", montant=" + montant +
                ", date='" + date + '\'' +
                ", type='" + type + '\'' +
                '}';
    }
}
