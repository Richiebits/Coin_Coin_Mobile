package com.example.coin_coin_mobile;

import android.annotation.SuppressLint;
import android.app.DownloadManager;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewStub;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import okhttp3.Request;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class PageAccueil extends AppCompatActivity implements View.OnClickListener{


    private Button btnAccueilProjets,btnCompteView;
    private TextView txtViewAccueil;
    private ActivityResultLauncher<Intent> activityResultLauncher;
    private String id,token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_page_accueil);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        Intent intent = getIntent();
        this.id = intent.getStringExtra("USER_ID");
        this.token = intent.getStringExtra("TOKEN");
        txtViewAccueil = findViewById(R.id.tvAccueil);
        btnAccueilProjets = findViewById(R.id.btnProjet);
        btnAccueilProjets.setOnClickListener(this);
        btnCompteView = findViewById(R.id.btnCompte);
        btnCompteView.setOnClickListener(this);


        String route = "client/" + id;
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        FetchApi.fetchData(route, "GET", null,headers, new OnDataFetchedListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onSuccess(String data) throws JSONException {
                JSONObject JsonData = new JSONObject(data);

                String nom = JsonData.getString("nom");
                String prenom = JsonData.getString("prenom");
                txtViewAccueil.setText("Bon Retour, " + prenom + " " + nom + "!");

            }

            @Override
            public void onError(String error) {

            }
        });



        activityResultLauncher =registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>(){
                    @Override
                    public void onActivityResult(ActivityResult result) {

                    }
                });

    }
    @Override
    public void onClick(View v) {
        if (v==btnAccueilProjets){
            Intent intent = new Intent (PageAccueil.this,Projet.class);
            Toast.makeText(this,id,Toast.LENGTH_LONG).show();
            intent.putExtra("USER_ID",this.id);
            intent.putExtra("TOKEN",token);
            activityResultLauncher.launch(intent);
        }
        if (v==btnCompteView){
            Intent intent = new Intent (PageAccueil.this,Compte.class);
            intent.putExtra("USER_ID", this.id);
            intent.putExtra("TOKEN",token);
            activityResultLauncher.launch(intent);
        }
    }

}