package daos;

import java.util.ArrayList;
import java.util.List;


import modele.Retrait;

public class RetraitDao {
    private static RetraitDao instance = null;
    private List<Retrait> retraits = new ArrayList<>();

    public static RetraitDao getInstance(String[] dates, int[] montants) {
        if (instance == null)
            instance = new RetraitDao(dates, montants);
        return instance;
    }


    private RetraitDao(String[] dates, int[] montants) {
        Retrait r;

        for (int i = 0; i < montants.length; i++) {
            r = new Retrait((i + 1) + "", dates[i], montants[i] + "");
            retraits.add(r);
        }
    }

    public List<Retrait> getRetraits() {
        return retraits;
    }


    public Retrait getRetrait(String id) {
        Retrait r = null;
        for (Retrait ret : retraits)
            if (ret.getId().equals(id))
                r = ret;
        return r;
    }
}
