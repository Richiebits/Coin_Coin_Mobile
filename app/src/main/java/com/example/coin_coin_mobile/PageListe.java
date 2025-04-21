package com.example.coin_coin_mobile;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import adapteur.DepotAdapter;
import adapteur.ProjetAdapter;
import adapteur.RetraitAdapter;
import daos.DepotDao;
import daos.RetraitDao;
import modele.Depot;
import modele.Projet;
import modele.Retrait;

public class PageListe extends AppCompatActivity implements View.OnClickListener {
    private ListView lv;
    private ImageButton btnRetour;
    private ActivityResultLauncher<Intent> aRL;
    private String id, token, type;
    private int projetId, budgetId;
    private TextView titre;
    private Button btnAjouterTransaction;

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
        titre = (TextView) findViewById(R.id.txtTitre);
        btnAjouterTransaction = (Button) findViewById(R.id.btnAjouterTransaction);
        btnAjouterTransaction.setOnClickListener(this);

        Intent intent = getIntent();
        this.id = intent.getStringExtra("USER_ID");
        this.token = intent.getStringExtra("TOKEN");
        this.projetId = intent.getIntExtra("PROJET_ID", -1);
        this.type = intent.getStringExtra("TYPE");

        loadData();

    }

    @Override
    public void onClick(View v) {
        if (v == btnRetour) {
            Intent intent = new Intent(PageListe.this, PageGraphique.class);
            intent.putExtra("USER_ID", id);
            intent.putExtra("TOKEN", token);
            intent.putExtra("PROJET_ID", projetId);
            startActivity(intent);
        }
        if (v == btnAjouterTransaction) {
            LayoutInflater inflater = LayoutInflater.from(this);
            View dialogView = inflater.inflate(R.layout.ajouter_transaction, null);

            EditText editTextNom = dialogView.findViewById(R.id.editTextNom);
            EditText editTextMontant = dialogView.findViewById(R.id.editTextMontant);
            Spinner spinner = dialogView.findViewById(R.id.spinner);

            // spinner
            String[] choix = {"Instantané", "Hebdomadaire", "Bi-hebdomadaire", "Mensuel", "Annuel"};
            ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, choix);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);

            //Avoir le ID du budget
            Map<String, String> headers2 = new HashMap<>();
            headers2.put("Authorization", "Bearer " + token);
            FetchApi.fetchData("budget/projet/" + projetId, "GET", null, headers2, new OnDataFetchedListener() {
                @Override
                public void onSuccess(String data) throws JSONException {
                    if (data != null && !data.isEmpty() && !data.equalsIgnoreCase("false")) {
                        JSONArray BudgetArray = new JSONArray(data);
                        JSONObject jsonData = BudgetArray.getJSONObject(0);
                        budgetId = jsonData.getInt("id");
                    } else {
                        Log.e("ERROR", "Empty or invalid data received: " + data);
                    }
                }

                @Override
                public void onError(String error) {
                    Log.e("ERROR", "Fetch Budget error: " + error);
                }
            });


            switch (Objects.requireNonNull(type)) {
                case "Depot":
                    AlertDialog dialog = new AlertDialog.Builder(this)
                            .setTitle("Ajouter un Dépot")
                            .setView(dialogView)
                            .setPositiveButton("AJOUTER", null)
                            .setNegativeButton("ANNULER", null)
                            .create();
                    dialog.setOnShowListener(d -> {
                        Button boutonAjouter = dialog.getButton(AlertDialog.BUTTON_POSITIVE);
                        boutonAjouter.setOnClickListener(w -> {
                            String nom = editTextNom.getText().toString().trim();
                            String montantStr = editTextMontant.getText().toString().trim();
                            String choixSelectionne = spinner.getSelectedItem().toString();
                            int recurrence = recurenceStringToInt(choixSelectionne);
                            if (nom.isEmpty()) {
                                editTextNom.setError("Le nom est requis");
                                return;
                            }
                            if (nom.length() > 100) {
                                editTextNom.setError("Le nom doit avoir 100 caractères maximum");
                                return;
                            }
                            double montant;
                            try {
                                montant = Double.parseDouble(montantStr);
                                if (montant <= 0) {
                                    editTextMontant.setError("Le montant doit être un nombre positif");
                                    return;
                                }
                            } catch (NumberFormatException e) {
                                editTextMontant.setError("Entrez un montant valide (ex : 25.50)");
                                return;
                            }
                            Map<String, String> headers = new HashMap<>();
                            headers.put("Authorization", "Bearer " + token);
                            String jsonBodyTransaction = "{"
                                    + "\"nomDepot\":\"" + nom + "\","
                                    + "\"montantDepot\":" + Double.parseDouble(montantStr) + ","
                                    + "\"depot_recurrence\":" + recurrence + ","
                                    + "\"id\":" + budgetId
                                    + "}";

                            FetchApi.fetchData("revenu", "POST", jsonBodyTransaction, headers, new OnDataFetchedListener() {
                                @Override
                                public void onSuccess(String data) throws JSONException {
                                    Toast.makeText(PageListe.this, "Dépot Ajouté!", Toast.LENGTH_SHORT).show();
                                    loadData();
                                }

                                @Override
                                public void onError(String error) {
                                    Log.e("ERROR", "POST error: " + error);
                                }
                            });
                            dialog.dismiss();
                        });
                    });
                    dialog.show();
                    break;
                case "Retrait":
                    AlertDialog dialog2 = new AlertDialog.Builder(this)
                            .setTitle("Ajouter un Retrait")
                            .setView(dialogView)
                            .setPositiveButton("AJOUTER", null)
                            .setNegativeButton("ANNULER", null)
                            .create();
                    dialog2.setOnShowListener(d -> {
                        Button boutonAjouter = dialog2.getButton(AlertDialog.BUTTON_POSITIVE);
                        boutonAjouter.setOnClickListener(w -> {
                            String nom = editTextNom.getText().toString().trim();
                            String montantStr = editTextMontant.getText().toString().trim();
                            String choixSelectionne = spinner.getSelectedItem().toString();
                            int recurrence = recurenceStringToInt(choixSelectionne);
                            if (nom.isEmpty()) {
                                editTextNom.setError("Le nom est requis");
                                return;
                            }
                            if (nom.length() > 100) {
                                editTextNom.setError("Le nom doit avoir 100 caractères maximum");
                                return;
                            }
                            double montant;
                            try {
                                montant = Double.parseDouble(montantStr);
                                if (montant <= 0) {
                                    editTextMontant.setError("Le montant doit être un nombre positif");
                                    return;
                                }
                            } catch (NumberFormatException e) {
                                editTextMontant.setError("Entrez un montant valide (ex : 25.50)");
                                return;
                            }
                            Map<String, String> headers = new HashMap<>();
                            headers.put("Authorization", "Bearer " + token);
                            String jsonBodyTransaction = "{"
                                    + "\"nomRetrait\":\"" + nom + "\","
                                    + "\"montantRetrait\":" + Double.parseDouble(montantStr) + ","
                                    + "\"retrait_recurrence\":" + recurrence + ","
                                    + "\"id\":" + budgetId
                                    + "}";

                            FetchApi.fetchData("depense", "POST", jsonBodyTransaction, headers, new OnDataFetchedListener() {
                                @Override
                                public void onSuccess(String data) throws JSONException {
                                    Toast.makeText(PageListe.this, "Retrait Ajouté!", Toast.LENGTH_SHORT).show();
                                    loadData();
                                }

                                @Override
                                public void onError(String error) {
                                    Log.e("ERROR", "POST error: " + error);
                                }
                            });
                            dialog2.dismiss();
                        });
                    });
                    dialog2.show();
                    break;
            }
        }
    }

    public int recurenceStringToInt(String recurrence) {
        switch (recurrence) {
            case "Instantané":
                return 0;
            case "Mensuel":
                return 30;
            case "Hebdomadaire":
                return 7;
            case "Bi-hebdomadaire":
                return 14;
            case "Annuel":
                return 365;

        }
        return 0;
    }
    private void loadData() {
        titre = findViewById(R.id.txtTitre);
        btnRetour = findViewById(R.id.btnRetour);
        btnAjouterTransaction = findViewById(R.id.btnAjouterTransaction);
        btnRetour.setOnClickListener(this);
        btnAjouterTransaction.setOnClickListener(this);

        switch (Objects.requireNonNull(type)) {
            case "Depot":
                titre.setText(R.string.depots);
                DepotDao dao = DepotDao.getInstance(String.valueOf(projetId), token);
                dao.getDepotsAsync(token, new OnDataFetchedListener() {
                    @Override
                    public void onSuccess(String data) {
                        List<Depot> listeDesDepots = dao.getDepots();
                        DepotAdapter adapterDe = new DepotAdapter(PageListe.this, R.layout.depense_layout, listeDesDepots);
                        ListView lv = findViewById(R.id.lvPersonnes);
                        lv.setAdapter(adapterDe);
                    }

                    @Override
                    public void onError(String error) {
                        Log.d("ERREUR", error);
                    }
                });
                break;
            case "Retrait":
                titre.setText(R.string.retraits);
                RetraitDao daoRe = RetraitDao.getInstance(String.valueOf(projetId), token);
                daoRe.getRetraitsAsync(token, new OnDataFetchedListener() {
                    @Override
                    public void onSuccess(String data) {
                        List<Retrait> listeDesRetraits = daoRe.getRetraits();
                        RetraitAdapter adapterRe = new RetraitAdapter(PageListe.this, R.layout.depense_layout, listeDesRetraits);
                        ListView lv = findViewById(R.id.lvPersonnes);
                        lv.setAdapter(adapterRe);
                    }

                    @Override
                    public void onError(String error) {
                        Log.d("ERREUR", error);
                    }
                });
                break;
        }
    }

}