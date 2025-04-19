package com.example.coin_coin_mobile;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import adapteur.ProjetAdapter;
import daos.ProjetDao;
import modele.Projet;

public class PageProjet extends AppCompatActivity implements View.OnClickListener,AdapterView.OnItemClickListener {
    private ImageButton btnRetour;
    private Button btnAjouter;
    private ActivityResultLauncher<Intent> aRL;
    private ListView listView;

    ProjetAdapter adapterProjet;

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
        listView = (ListView) findViewById(R.id.lvProjets);
        listView.setOnItemClickListener(this);
        ProjetDao projetDao = ProjetDao.getInstance(id,token);
        projetDao.getProjetsAsync(id,token,new OnDataFetchedListener() {
            @Override
            public void onSuccess(String data) {
                List<Projet> projets = projetDao.getProjets();
                adapterProjet = new ProjetAdapter(PageProjet.this, R.layout.projet_layout, projets);
                listView.setAdapter(adapterProjet);
            }

            @Override
            public void onError(String error) {
                Log.d("ERREUR", error);
            }
        });



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
    protected void onResume() {
        super.onResume();

        Intent intent = getIntent();
        this.id = intent.getStringExtra("USER_ID");
        this.token = intent.getStringExtra("TOKEN");

        ProjetDao projetDao = ProjetDao.getInstance(id, token);
        projetDao.getProjetsAsync(id, token, new OnDataFetchedListener() {
            @Override
            public void onSuccess(String data) {
                List<Projet> projets = projetDao.getProjets();
                adapterProjet = new ProjetAdapter(PageProjet.this, R.layout.projet_layout, projets);
                listView.setAdapter(adapterProjet);
            }

            @Override
            public void onError(String error) {
                Log.d("ERREUR", error);
            }
        });
    }

    @Override
    public void onClick(View v) {
        if(v==btnRetour) {
            Intent intent = new Intent(PageProjet.this, PageAccueil.class);
            intent.putExtra("USER_ID",id);
            intent.putExtra("TOKEN",token);
            aRL.launch(intent);
        }

        if(v==btnAjouter) {
            Intent intent = new Intent(PageProjet.this,PageAjouterProjet.class);
            intent.putExtra("USER_ID",id);
            intent.putExtra("TOKEN",token);
            aRL.launch(intent);

        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long idLong) {
        Intent intent = new Intent(this, PageGraphique.class);
        Projet projet = (Projet) parent.getAdapter().getItem(position);
        intent.putExtra("USER_iD",id);
        intent.putExtra("TOKEN",token);
        intent.putExtra("PROJET_ID",projet.getId());
        intent.putExtra("PROJET_NOM",projet.getNom());
        intent.putExtra("BUT_EPARGNE",projet.getButEpargne());
        aRL.launch(intent);
    }


}