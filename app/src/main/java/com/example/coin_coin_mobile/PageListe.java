package com.example.coin_coin_mobile;

import android.os.Bundle;
import android.widget.ListView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.List;

import adapteur.DepenseAdapter;
import daos.DepenseDao;
import modele.Depense;

public class PageListe extends AppCompatActivity {
    private ListView lv;

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
        String[] dates = getResources().getStringArray(R.array.dates);
        int[] montants = getResources().getIntArray(R.array.montants);

        DepenseDao dao = DepenseDao.getInstance(dates, montants);
        List<Depense> listeDesPersonnes = dao.getDepenses();
        DepenseAdapter adapter;
        adapter = new DepenseAdapter(this, R.layout.depense_layout, listeDesPersonnes);
        lv = (ListView) findViewById(R.id.lvPersonnes);
        lv.setAdapter(adapter);

    }
}