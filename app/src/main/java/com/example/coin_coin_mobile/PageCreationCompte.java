package com.example.coin_coin_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PageCreationCompte extends AppCompatActivity implements View.OnClickListener {

    private EditText editNom, editPrenom , editCourriel, editTel, editMDP, editMDPConfirm;

    private Button btnConfirmer;

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

        String nom = editNom.getText().toString().trim();
        String prenom = editNom.getText().toString().trim();
        String courriel = editCourriel.getText().toString().trim();
        String numTel = editTel.getText().toString().trim();
        String motDePasse = editMDP.getText().toString().trim();
        String motDePasseConfirm = editMDPConfirm.getText().toString().trim();

        activityResultLauncher.launch(intent);
    }
}