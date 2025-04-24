package modele;

public class Transaction {
    private int id;
    private int montant,recurrence;
    private String date;
    private String type;

    public Transaction(int id, int montant, String date, String type, int recurrence) {
        this.id = id;
        this.montant = montant;
        this.date = date;
        this.type = type;
        this.recurrence = recurrence;
    }

    // Getters
    public int getId() {
        return id;
    }
    public int getRecurrence() {
        return recurrence;
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
    public void setRecurrence(int recurrence) {
        this.recurrence = recurrence;
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
                ", recurrence='" + recurrence + '\'' +
                '}';
    }
}
