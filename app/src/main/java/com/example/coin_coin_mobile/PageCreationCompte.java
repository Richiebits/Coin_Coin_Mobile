package com.example.coin_coin_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
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

import java.util.regex.Pattern;

public class PageCreationCompte extends AppCompatActivity implements View.OnClickListener {

    private EditText editNom, editPrenom , editCourriel, editTel, editMDP, editMDPConfirm;

    private Button btnConfirmer;

    private ImageButton btnRetour;

    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_page_creation_compte);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editNom = findViewById(R.id.editNom);
        editPrenom = findViewById(R.id.editPrenom);
        editCourriel = findViewById(R.id.editCourriel);
        editTel = findViewById(R.id.editTel);
        editMDP = findViewById(R.id.editMDP);
        editMDPConfirm = findViewById(R.id.editMDPConfirm);
        btnConfirmer = findViewById(R.id.btnConfirmer);
        btnConfirmer.setOnClickListener(this);
        btnRetour = findViewById(R.id.btnRetour);
        btnRetour.setOnClickListener(this);


        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                    }
                });


    }

    @Override
    public void onClick(View v) {

        Intent intent = new Intent(PageCreationCompte.this,PageConnection.class);

        if (v==btnConfirmer) {
            String nom = editNom.getText().toString().trim();
            String prenom = editPrenom.getText().toString().trim();
            String courriel = editCourriel.getText().toString().trim();
            String numTel = editTel.getText().toString().trim();
            String motDePasse = editMDP.getText().toString().trim();
            String motDePasseConfirm = editMDPConfirm.getText().toString().trim();
            if (!nom.isEmpty() && !prenom.isEmpty() && !courriel.isEmpty() && !motDePasse.isEmpty()){
                String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]{8,}$";
                if (motDePasse.equals(motDePasseConfirm) && Pattern.matches(regex, motDePasse)){
                    String jsonBody =   "{\"email\":\"" + courriel + "\"," +
                                        " \"nom\":\"" + nom + "\"," +
                                        " \"prenom\":\"" + prenom + "\"," +
                                        " \"tel\":\"" + numTel + "\"," +
                                        " \"mot_de_passe\":\"" + motDePasse + "\"}";

                    FetchApi.fetchData("client", "POST", jsonBody,null, new OnDataFetchedListener() {
                        @Override
                        public void onSuccess(String data) throws JSONException {
                            Toast.makeText(PageCreationCompte.this,
                                    "Compte créé avec succès", Toast.LENGTH_SHORT).show();
                            activityResultLauncher.launch(intent);
                        }

                        @Override
                        public void onError(String error) {
                            Log.e("ERROR", "POST error: " + error);
                        }
                    });
                } else {
                    editMDP.setError("Le mot de passe doit etre 8 charactères et doit contenir 1 nbr, 1 spc char, 1 caps");
                    Toast.makeText(PageCreationCompte.this,
                            "Le mot de passe doit etre 8 charactères et doit contenir 1 nbr, 1 spc char, 1 caps", Toast.LENGTH_SHORT).show();
                }
            } else {
                Toast.makeText(PageCreationCompte.this,
                            "Veuillez remplir tout les champs obligatoire!", Toast.LENGTH_SHORT).show();
            }
        }
        if (v==btnRetour){
            activityResultLauncher.launch(intent);
        }
    }
}