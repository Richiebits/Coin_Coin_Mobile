package com.example.coin_coin_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Projet extends AppCompatActivity implements View.OnClickListener {
    private ImageButton btnRetour;
    private Button btnAjouter;
    private ActivityResultLauncher<Intent> aRL;
    private CardView carte1Test;

    private String id,token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.projet);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnRetour = (ImageButton) findViewById(R.id.btnRetour);
        btnRetour.setOnClickListener(this);
        btnAjouter = findViewById(R.id.btnAjouterProjet);
        btnAjouter.setOnClickListener(this);
        Intent intent = getIntent();
        this.id = intent.getStringExtra("USER_ID");
        this.token = intent.getStringExtra("TOKEN");
        carte1Test = (CardView) findViewById(R.id.carte1Test);
        carte1Test.setOnClickListener(this);

        aRL = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(androidx.activity.result.ActivityResult result) {

                    }
                }
        );

    }

    @Override
    public void onClick(View v) {
        if(v==btnRetour) {
            Intent intent = new Intent(Projet.this, PageAccueil.class);
            aRL.launch(intent);
        }
        if(v==carte1Test){
            Intent intent = new Intent(Projet.this, PageGraphique.class);
            aRL.launch(intent);
        }
        if(v==btnAjouter) {
            Intent intent = new Intent(Projet.this,PageAjouterProjet.class);
            intent.putExtra("USER_ID",id);
            intent.putExtra("TOKEN",token);
            aRL.launch(intent);

        }
    }
}