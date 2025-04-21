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
import modele.Projet;


public class DepotDao {
    private static DepotDao instance = null;
    private List<Depot> depots = new ArrayList<>();
    private String token;
    private String projetId;

    public static DepotDao getInstance(String projetId, String token) {
            instance = new DepotDao(projetId, token);
        return instance;
    }
    private DepotDao(String projetId, String token) {
        this.projetId = projetId;
        this.token = token;
    }

    public List<Depot> getDepots() {
        return this.depots;
    }

    public void getDepotsAsync( String token, OnDataFetchedListener listener) {
        //Avoir le ID du budget
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
                    //Fetch tous les depots
                    Map<String, String> headers2 = new HashMap<>();
                    headers2.put("Authorization", "Bearer " + token);
                    FetchApi.fetchData("revenu/budget/" + budgetId, "GET", null, headers2, new OnDataFetchedListener() {

                        @Override
                        public void onSuccess(String data) throws JSONException {
                            if (!data.equalsIgnoreCase("false")){
                                JSONArray depotsArray = new JSONArray(data);

                                for (int i =0; i<depotsArray.length();i++){
                                    JSONObject jsonData = depotsArray.getJSONObject(i);
                                    int id = jsonData.getInt("id_depot");
                                    String nom = jsonData.getString("nom");
                                    int montant = jsonData.getInt("montant");
                                    int depotRecurrence = jsonData.getInt("depot_recurrence");
                                    Depot depot = new Depot(id,montant,depotRecurrence, nom);
                                    depots.add(depot);
                                }
                            }

                            listener.onSuccess(data);
                        }

                        @Override
                        public void onError(String error) {
                            Log.e("ERROR", "Fetch depots error: " + error);
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