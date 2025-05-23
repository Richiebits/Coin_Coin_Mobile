package com.example.coin_coin_mobile;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

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
import java.util.Objects;

public class Compte extends AppCompatActivity implements View.OnClickListener{
    private EditText editNom, editPrenom, editEmail, editMDP, editNouvMDP, editMDPModif, editNumTel;
    private Button btnModifier, btnEnvoyerModif, btnEnvoyerMDP, btnConfNouvMdp;
    private ImageButton btnRetour;
    private TextView txtConf,txtMessageConfMdp;
    private String id, email,token;


    private ActivityResultLauncher<Intent> activityResultLauncher;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_compte);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                    }
                });

        editNom = findViewById(R.id.editNom);
        editPrenom = findViewById(R.id.editPrenom);
        editEmail = findViewById(R.id.editEmail);
        btnModifier = findViewById(R.id.btnMofication);
        btnModifier.setOnClickListener(this);
        btnEnvoyerModif = findViewById(R.id.btnEnvoyerModif);
        btnEnvoyerModif.setOnClickListener(this);
        txtConf =  findViewById(R.id.txtMessageConf);
        btnEnvoyerMDP = findViewById(R.id.btnEnvoyerMDP);
        btnEnvoyerMDP.setOnClickListener(this);
        editMDP = findViewById(R.id.editMDP);
        editNouvMDP = findViewById(R.id.editNouvMDP);
        btnConfNouvMdp = findViewById(R.id.btnConfNouvMdp);
        btnConfNouvMdp.setOnClickListener(this);
        txtMessageConfMdp = findViewById(R.id.txtMessageConfMdp);
        editMDPModif = findViewById(R.id.editMDPModif);
        btnRetour = findViewById(R.id.btnRetour);
        btnRetour.setOnClickListener(this);
        editNumTel = (EditText) findViewById(R.id.editNumTel);

        Intent intent = getIntent();
        this.id = intent.getStringExtra("USER_ID");
        this.token = intent.getStringExtra("TOKEN");
        String route = "client/" + id;
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        FetchApi.fetchData(route, "GET", null,headers, new OnDataFetchedListener() {
            @Override
            public void onSuccess(String data) throws JSONException {
                JSONObject JsonData = new JSONObject(data);
                editNom.setText(JsonData.getString("nom"));
                editPrenom.setText(JsonData.getString("prenom"));
                editEmail.setText(JsonData.getString("email"));
                email = JsonData.getString("email");
                System.out.println(email);
            }

            @Override
            public void onError(String error) {
                Log.e("ERROR", "GET error: " + error);
            }
        });

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {

        if(v==btnRetour){
            Intent intent = new Intent(Compte.this,PageAccueil.class);
            intent.putExtra("USER_ID",id);
            intent.putExtra("TOKEN",token);
            activityResultLauncher.launch(intent);
        }
        if(v == btnModifier){
            editNom.setEnabled(true);
            editPrenom.setEnabled(true);
            editEmail.setEnabled(true);
            editMDPModif.setEnabled(true);
            editNumTel.setEnabled(true);
            btnModifier.setEnabled(false);
            btnModifier.setVisibility(INVISIBLE);
            btnEnvoyerModif.setEnabled(true);
            btnEnvoyerModif.setVisibility(VISIBLE);
        }
        if(v == btnEnvoyerModif){
            btnEnvoyerModif.setEnabled(false);
            btnEnvoyerModif.setVisibility(INVISIBLE);
            btnModifier.setEnabled(true);
            btnModifier.setVisibility(VISIBLE);
            editMDPModif.setEnabled(false);
            editNom.setEnabled(false);
            editPrenom.setEnabled(false);
            editEmail.setEnabled(false);
            editNumTel.setEnabled(false);
            //----
            //Envoyer les infos a la data base ici
            String nomModif = editNom.getText().toString();
            String prenomModif = editPrenom.getText().toString();
            String emailmodif = editEmail.getText().toString();
            String telModif = "";
            String MDP = editMDPModif.getText().toString();
            String jsonBodyConn = "{\"email\":\"" + email + "\", \"mot_de_passe\":\"" + MDP + "\"}";
            String route = "client/" + id;
            Intent intent = getIntent();
            token = intent.getStringExtra("TOKEN");
            Map<String, String> headers = new HashMap<>();
            headers.put("Authorization", "Bearer " + token);
            FetchApi.fetchData("client/connexion", "POST", jsonBodyConn,headers, new OnDataFetchedListener() {
                @Override
                public void onSuccess(String data) throws JSONException {
                    if (!data.equalsIgnoreCase("false")){

                        String jsonBodyModif =   "{\"email\":\"" + emailmodif + "\"," +
                                " \"nom\":\"" + nomModif + "\"," +
                                " \"prenom\":\"" + prenomModif + "\"," +
                                " \"tel\":\"" + telModif + "\"," +
                                " \"mot_de_passe\":\"" + MDP + "\"}";
                        FetchApi.fetchData(route, "PUT", jsonBodyModif,headers, new OnDataFetchedListener() {
                            @Override
                            public void onSuccess(String data) throws JSONException {
                                Toast.makeText(Compte.this, "Le compte à été modifié!",
                                        Toast.LENGTH_SHORT).show();
                            }

                            @Override
                            public void onError(String error) {
                                Toast.makeText(Compte.this, "Error API",
                                        Toast.LENGTH_SHORT).show();
                                Log.e("ERROR", "Fetch error: " + error);
                            }
                        });
                    } else {
                        editMDPModif.setText("");
                        Toast.makeText(Compte.this, "Mot de passe incorrect!",
                                Toast.LENGTH_SHORT).show();
                    }

                }

                @Override
                public void onError(String error) {
                    Toast.makeText(Compte.this, "Error API",
                            Toast.LENGTH_SHORT).show();
                    Log.e("ERROR", "Fetch error: " + error);
                }
            });


            //----
        }
        if(v==btnConfNouvMdp){
            editMDP.setEnabled(true);
            editNouvMDP.setEnabled(true);
            btnConfNouvMdp.setEnabled(false);
            btnConfNouvMdp.setVisibility(INVISIBLE);
            btnEnvoyerMDP.setEnabled(true);
            btnEnvoyerMDP.setVisibility(VISIBLE);
        }
        if(v==btnEnvoyerMDP){
            editMDP.setEnabled(false);
            editNouvMDP.setEnabled(false);
            btnEnvoyerMDP.setEnabled(false);
            btnEnvoyerMDP.setVisibility(INVISIBLE);
            btnConfNouvMdp.setEnabled(true);
            btnConfNouvMdp.setVisibility(VISIBLE);
            //----
            //Envoyer les infos a la data base ici

            //----
        }
    }
}