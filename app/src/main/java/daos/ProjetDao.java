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
import modele.Projet;

public class ProjetDao {

    private static ProjetDao instance = null;
    private List<Projet> projets = new ArrayList<>();

    private String userId;
    private String token;

    public static ProjetDao getInstance(String userId,String token){
        instance = new ProjetDao(userId,token);
        return instance;
    }


    private ProjetDao(String userId,String token){
        this.userId = userId;
        this.token = token;
    }

    public  void getProjetsAsync(String userId, String token, OnDataFetchedListener listener) {

        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        FetchApi.fetchData("projet/client/" + userId,
                "GET", null,headers, new OnDataFetchedListener() {
                    @Override
                    public void onSuccess(String data) throws JSONException {
                        if (!data.equalsIgnoreCase("false")){
                            JSONArray projetsArray = new JSONArray(data);

                            for (int i =0; i<projetsArray.length();i++){
                                JSONObject jsonData = projetsArray.getJSONObject(i);
                                int id = jsonData.getInt("id");
                                String nom = jsonData.getString("nom");
                                int butEpargne = jsonData.getInt("but_epargne");
                                int clientId = jsonData.getInt("client_id");
                                Projet projet = new Projet(id,nom,butEpargne,clientId);
                                projets.add(projet);
                            }
                        }
                        listener.onSuccess(data);
                    }
                    @Override
                    public void onError(String error) {

                        Log.e("ERROR", "Fetch error: " + error);
                    }
                });









    }



    public List<Projet> getProjets(){return this.projets;}



}
