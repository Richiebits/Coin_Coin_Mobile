package com.example.coin_coin_mobile;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;
import java.util.Objects;

import adapteur.DepotAdapter;
import adapteur.RetraitAdapter;
import daos.DepotDao;
import daos.RetraitDao;
import modele.Depot;
import modele.Retrait;

public class PageListe extends AppCompatActivity implements View.OnClickListener {
    private ListView lv;
    private ImageButton btnRetour;
    private ActivityResultLauncher<Intent> aRL;

    private String id,token;

    private TextView titre;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_page_liste);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnRetour = (ImageButton) findViewById(R.id.btnRetour);
        btnRetour.setOnClickListener(this);
        titre=(TextView) findViewById(R.id.txtTitre);

        Intent intent = getIntent();
        this.id = intent.getStringExtra("USER_ID");
        this.token = intent.getStringExtra("TOKEN");
        String type = intent.getStringExtra("TYPE");

        String[] dates = getResources().getStringArray(R.array.dates);
        int[] montants = getResources().getIntArray(R.array.montants);

        switch(Objects.requireNonNull(type)){
            case "Depot":
                titre.setText(R.string.depots);

                DepotDao dao = DepotDao.getInstance(dates, montants);
                List<Depot> listeDesDepots = dao.getDepots();
                DepotAdapter adapterDe;
                adapterDe = new DepotAdapter(this, R.layout.depense_layout, listeDesDepots);
                lv = (ListView) findViewById(R.id.lvPersonnes);
                lv.setAdapter(adapterDe);
                break;

            case "Retrait" :
                titre.setText(R.string.retraits);
                RetraitDao daoRe = RetraitDao.getInstance(dates, montants);
                List<Retrait> listeDesRetraits = daoRe.getRetraits();
                RetraitAdapter adapterRe;
                adapterRe = new RetraitAdapter(this, R.layout.depense_layout, listeDesRetraits);
                lv = (ListView) findViewById(R.id.lvPersonnes);
                lv.setAdapter(adapterRe);
                break;
        }





    }

    @Override
    public void onClick(View v) {
    if(v == btnRetour){
        Intent intent = new Intent(PageListe.this, PageGraphique.class);
        intent.putExtra("USER_ID",id);
        intent.putExtra("TOKEN",token);
        startActivity(intent);
    }
    }
}