package daos;

import java.util.ArrayList;
import java.util.List;
import modele.Depense;


public class DepenseDao {
    private static DepenseDao instance = null;
    private List<Depense> depenses = new ArrayList<>();

    public static DepenseDao getInstance(String[] dates, int[] montants) {
        if (instance == null)
            instance = new DepenseDao(dates, montants);
        return instance;
    }


    private DepenseDao(String[] dates, int[] montants) {
        Depense d;
//        String[] dates = {
//                "2023-01-05", "2023-01-12", "2023-01-18", "2023-01-25", "2023-02-03",
//                "2023-02-10", "2023-02-17", "2023-02-24", "2023-03-05", "2023-03-12",
//                "2023-03-19", "2023-03-26", "2023-04-02", "2023-04-09", "2023-04-16",
//                "2023-04-23", "2023-04-30", "2023-05-07", "2023-05-14", "2023-05-21",
//                "2023-05-28", "2023-06-04", "2023-06-11", "2023-06-18", "2023-06-25",
//                "2023-07-02", "2023-07-09", "2023-07-16", "2023-07-23", "2023-07-30",
//                "2023-08-06", "2023-08-13", "2023-08-20", "2023-08-27", "2023-09-03",
//                "2023-09-10", "2023-09-17", "2023-09-24", "2023-10-01", "2023-10-08",
//                "2023-10-15", "2023-10-22", "2023-10-29", "2023-11-05", "2023-11-12",
//                "2023-11-19", "2023-11-26", "2023-12-03", "2023-12-10", "2023-12-17"
//        };
//
//        // Tableau de montants entre 10 et 9999
//        int[] montants = {
//                150, 899, 1245, 356, 4789, 562, 87, 9123, 3456, 210,
//                789, 4561, 982, 567, 1234, 876, 5432, 109, 7654, 3210,
//                98, 4321, 654, 7890, 2345, 678, 9012, 345, 6789, 123,
//                4567, 890, 2345, 678, 9012, 3456, 789, 1234, 5678, 901,
//                234, 5678, 9012, 345, 6789, 1234, 567, 8901, 2345, 678
//        };

        for (int i = 0; i < montants.length; i++) {
            d = new Depense((i + 1) + "", dates[i], montants[i] + "");
            depenses.add(d);
        }
    }

    /**
     * Méthode qui retourne la Liste complète des personnes
     * @return List<Personne> - Liste des personnes enregistrées
     */
    public List<Depense> getDepenses() {
        return depenses;
    }

    /**
     * Méthode qui recherche et retourne une depense par son ID
     * @param id - Identifiant de la depense recherchée
     * @return Depense - l'objet correspondant à l'ID, ou null si non trouvé
     */
    public Depense getDepense(String id) {
        Depense d = null;
        for (Depense dep : depenses)
            if (dep.getId().equals(id))
                d = dep;
        return d;
    }
}