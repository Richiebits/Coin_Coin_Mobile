package com.example.coin_coin_mobile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Calendar;

public class PageAjouterProjet extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextNomProjet,editTextMontantCible,editTextDateCible;

    private ImageButton btnRetour;

    private RadioGroup radioGroupTempsOuBudget;

    private String[] frequence;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_page_ajouter_projet);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });


        editTextNomProjet = findViewById(R.id.editNomProjet);
        editTextDateCible = findViewById(R.id.editDate);
        editTextDateCible.setOnClickListener(this);
        editTextMontantCible = findViewById(R.id.editMontant);
        btnRetour = findViewById(R.id.btnRetour);
        btnRetour.setOnClickListener(this);
        radioGroupTempsOuBudget = findViewById(R.id.radioGroupMontant);
        radioGroupTempsOuBudget.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonTemps) {
                    editTextDateCible.setEnabled(true);
                    editTextDateCible.setAlpha(1.0f);

                } else if (checkedId == R.id.radioButtonBudget) {
                    editTextDateCible.setEnabled(false);
                    editTextDateCible.setAlpha(0.5f);
                }
            }
        });


        Spinner spinnerDepot = findViewById(R.id.spnFrequenceDepot);
        Resources resources = getResources();
        frequence = resources.getStringArray(R.array.frequence);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, // Contexte de l'activité
                android.R.layout.simple_spinner_item,
                frequence
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
        spinnerDepot.setAdapter(adapter);











    }

    @Override
    public void onClick(View v) {
        if (v==editTextDateCible){
            Calendar calendar = Calendar.getInstance();
            int annee = calendar.get(Calendar.YEAR);
            int mois = calendar.get(Calendar.MONTH);
            int jour = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    PageAjouterProjet.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(android.widget.DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

                            String formattedDate = selectedDay + "/" + (selectedMonth + 1) + "/" + selectedYear;
                            editTextDateCible.setText(formattedDate);
                        }
                    },
                    annee, mois, jour
            );
            datePickerDialog.getDatePicker().setMinDate(calendar.getTimeInMillis());
            datePickerDialog.show();
        }

        if (v==btnRetour){
            Intent intent = new Intent(PageAjouterProjet.this,Projet.class);
            activityResultLauncher.launch(intent);

        }
    }
}