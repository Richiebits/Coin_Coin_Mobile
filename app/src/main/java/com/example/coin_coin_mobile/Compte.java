package com.example.coin_coin_mobile;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Objects;

public class Compte extends AppCompatActivity implements View.OnClickListener{
    private EditText editNom, editPrenom, editEmail, editMDP, editNouvMDP, editMDPModif;
    private Button btnModifier, btnEnvoyerModif, btnEnvoyerMDP, btnConfNouvMdp;
    private TextView txtConf,txtMessageConfMdp;

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

        editNom=(EditText) findViewById(R.id.editNom);
        editPrenom=(EditText) findViewById(R.id.editPrenom);
        editEmail=(EditText) findViewById(R.id.editEmail);
        btnModifier=(Button) findViewById(R.id.btnMofication);
        btnModifier.setOnClickListener(this);
        btnEnvoyerModif=(Button) findViewById(R.id.btnEnvoyerModif);
        btnEnvoyerModif.setOnClickListener(this);
        txtConf = (TextView) findViewById(R.id.txtMessageConf);
        btnEnvoyerMDP = (Button) findViewById(R.id.btnEnvoyerMDP);
        btnEnvoyerMDP.setOnClickListener(this);
        editMDP=(EditText) findViewById(R.id.editMDP);
        editNouvMDP=(EditText) findViewById(R.id.editNouvMDP);
        btnConfNouvMdp = (Button) findViewById(R.id.btnConfNouvMdp);
        btnConfNouvMdp.setOnClickListener(this);
        txtMessageConfMdp = (TextView) findViewById(R.id.txtMessageConfMdp);
        editMDPModif = (EditText) findViewById(R.id.editMDPModif);

    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onClick(View v) {
        if(v == btnModifier){
            editNom.setEnabled(true);
            editPrenom.setEnabled(true);
            editEmail.setEnabled(true);
            editMDPModif.setEnabled(true);
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
            //----
            //Envoyer les infos a la data base ici
            txtConf.setText("La data Base n'est pas connectee");
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
            txtMessageConfMdp.setText("La data Base n'est pas connectee");
            //----
        }
    }
}