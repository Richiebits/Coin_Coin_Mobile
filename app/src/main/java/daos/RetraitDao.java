package daos;

import android.util.Log;

import com.example.coin_coin_mobile.FetchApi;
import com.example.coin_coin_mobile.OnDataFetchedListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import modele.Depot;
import modele.Retrait;

public class RetraitDao {
    private static RetraitDao instance = null;
    private List<Retrait> retraits = new ArrayList<>();
    private String token, projetId;

    public static RetraitDao getInstance(String projetId, String token) {
            instance = new RetraitDao(projetId, token);
        return instance;
    }
    private RetraitDao(String projetId, String token) {
        this.projetId = projetId;
        this.token = token;
    }

    public List<Retrait> getRetraits() {
        return retraits;
    }

    public void getRetraitsAsync(String token, OnDataFetchedListener listener) {
        //Avoir le ID du retrait
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        FetchApi.fetchData("budget/projet/" + projetId, "GET", null, headers, new OnDataFetchedListener() {
            @Override
            public void onSuccess(String data) throws JSONException {
                if (data != null && !data.isEmpty() && !data.equalsIgnoreCase("false")) {

                    JSONArray BudgetArray = new JSONArray(data);
                    JSONObject jsonData = BudgetArray.getJSONObject(0);
                    int budgetId = jsonData.getInt("id");
                    Log.d("depotPrint","BUDGET ID:" + budgetId);
                    //Fetch tous les retraits
                    Map<String, String> headers2 = new HashMap<>();
                    headers2.put("Authorization", "Bearer " + token);
                    FetchApi.fetchData("depense/budget/" + budgetId, "GET", null, headers2, new OnDataFetchedListener() {

                        @Override
                        public void onSuccess(String data) throws JSONException {
                            if (!data.equalsIgnoreCase("false")){
                                JSONArray retraitsArray = new JSONArray(data);

                                for (int i =0; i<retraitsArray.length();i++){
                                    JSONObject jsonData = retraitsArray.getJSONObject(i);
                                    int id = jsonData.getInt("id_retrait");
                                    String nom = jsonData.getString("nom");
                                    int montant = jsonData.getInt("montant");
                                    int retraitRecurrence = jsonData.getInt("retrait_recurrence");
                                    Retrait retrait= new Retrait(id,montant,retraitRecurrence, nom);
                                    retraits.add(retrait);
                                }
                            }

                            listener.onSuccess(data);
                        }

                        @Override
                        public void onError(String error) {
                            Log.e("ERROR", "Fetch retraits error: " + error);
                        }
                    });


                } else {
                    Log.e("ERROR", "Empty or invalid data received: " + data);
                    listener.onError("Aucune donnée reçue");
                }
                listener.onSuccess(data);
            }

            @Override
            public void onError(String error) {
                Log.e("ERROR", "Fetch Budget error: " + error);
            }
        });

    }

}
