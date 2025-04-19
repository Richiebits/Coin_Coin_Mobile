package modele;

public class Projet {

    int id,butEpargne,clientId;
    String nom;


    public Projet (int id,String nom,int butEpargne, int clientId){
        this.id = id;
        this.nom = nom;
        this.butEpargne = butEpargne;
        this.clientId = clientId;
    }

    public int getId(){return this.id;}

    public String getNom(){return this.nom;}

    public int getButEpargne(){return this.butEpargne;}

    public int getClientId(){return this.clientId;}

    public void setId(int id) {
        this.id = id;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public void setButEpargne(int butEpargne) {
        this.butEpargne = butEpargne;
    }

    public void setClientId(int clientId) {
        this.clientId = clientId;
    }


}
