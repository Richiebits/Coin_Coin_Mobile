package com.example.coin_coin_mobile;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import org.json.JSONException;

import java.util.Calendar;

public class PageAjouterProjet extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextNomProjet,editTextMontantCible,editDepot,editRetrait;

    private TextView textViewDateCible;

    private ImageButton btnRetour;

    private RadioGroup radioGroupTempsOuBudget;

    private RadioButton radioBtnTemps,radioBtnBudget;

    private Spinner   spinnerDepot,spinnerRetrait;

    private Button btnCreer;

    private String id;

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
        textViewDateCible = findViewById(R.id.txtViewDate);
        textViewDateCible.setOnClickListener(this);
        textViewDateCible.setText("");
        editTextMontantCible = findViewById(R.id.editMontant);
        editDepot = findViewById(R.id.editDepot);
        editRetrait = findViewById(R.id.editRetrait);

        btnRetour = findViewById(R.id.btnRetour);
        btnRetour.setOnClickListener(this);
        btnCreer = findViewById(R.id.btnCreerProjet);
        btnCreer.setOnClickListener(this);

        radioBtnTemps = findViewById(R.id.radioButtonTemps);
        radioBtnTemps.setOnClickListener(this);
        radioBtnBudget = findViewById(R.id.radioButtonBudget);
        radioBtnBudget.setOnClickListener(this);
        radioGroupTempsOuBudget = findViewById(R.id.radioGroupMontant);
        radioGroupTempsOuBudget.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioButtonTemps) {
                    textViewDateCible.setAlpha(1.0f);
                    textViewDateCible.setEnabled(true);
                    editDepot.setEnabled(false);
                    editDepot.setAlpha(0.5f);
                    editRetrait.setEnabled(false);
                    editRetrait.setAlpha(0.5f);
                    spinnerDepot.setEnabled(false);
                    spinnerDepot.setAlpha(0.5f);
                    spinnerRetrait.setEnabled(false);
                    spinnerRetrait.setAlpha(0.5f);
                    radioBtnTemps.setSelected(true);
                    radioBtnBudget.setSelected(false);


                } else if (checkedId == R.id.radioButtonBudget) {
                    textViewDateCible.setAlpha(0.5f);
                    textViewDateCible.setEnabled(false);
                    editDepot.setEnabled(true);
                    editDepot.setAlpha(1.0f);
                    editRetrait.setEnabled(true);
                    editRetrait.setAlpha(1.0f);
                    spinnerDepot.setEnabled(true);
                    spinnerDepot.setAlpha(1.0f);
                    spinnerRetrait.setEnabled(true);
                    spinnerRetrait.setAlpha(1.0f);
                    radioBtnBudget.setSelected(true);
                    radioBtnTemps.setSelected(false);

                }
            }
        });


        spinnerDepot = findViewById(R.id.spnFrequenceDepot);
        spinnerRetrait = findViewById(R.id.spnFrequenceRetrait);
        Resources resources = getResources();
        frequence = resources.getStringArray(R.array.frequence);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this, // Contexte de l'activité
                R.layout.spinner_item_text,
                frequence
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerDepot.setAdapter(adapter);
        spinnerRetrait.setAdapter(adapter);
    }

    @Override
    public void onClick(View v) {
        if (v==textViewDateCible){
            Calendar calendar = Calendar.getInstance();
            int annee = calendar.get(Calendar.YEAR);
            int mois = calendar.get(Calendar.MONTH);
            int jour = calendar.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog datePickerDialog = new DatePickerDialog(
                    PageAjouterProjet.this,
                    new DatePickerDialog.OnDateSetListener() {
                        @Override
                        public void onDateSet(android.widget.DatePicker view, int selectedYear, int selectedMonth, int selectedDay) {

                            String formattedDate = selectedYear + "-" + (selectedMonth + 1) + "-" + selectedDay;
                            textViewDateCible.setText(formattedDate);
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
        if(v==btnCreer){

            int selectedRadioButtonId = radioGroupTempsOuBudget.getCheckedRadioButtonId();
            if (selectedRadioButtonId==-1){
                Toast.makeText(this, "Vous devez choisir un mode de calcul pour votre projet (temps ou budget)", Toast.LENGTH_SHORT).show();
            } else {
                String nomProjet = editTextNomProjet.getText().toString().trim();
                String valeurMontantBudget = editTextMontantCible.getText().toString().trim();

                if (radioBtnBudget.isSelected()){
                    String montantDepot =editDepot.getText().toString();
                    String montantRetrait = editRetrait.getText().toString();
                    if ((montantDepot.isEmpty()&&montantRetrait.isEmpty())||nomProjet.isEmpty()||valeurMontantBudget.isEmpty()){
                        Toast.makeText(this,"Veuillez remplir les champs nécessaires",Toast.LENGTH_SHORT).show();
                    } else if (valeurMontantBudget.equals("0")){
                        Toast.makeText(this,"La valeur de votre budget ne peut pas être nulle",Toast.LENGTH_SHORT).show();

                    }  else {
                        Toast.makeText(this,getRecurrence(spinnerDepot) + " " + getRecurrence(spinnerRetrait),Toast.LENGTH_SHORT).show();
                    }

                } if(radioBtnTemps.isSelected()) {
                    String dateCible = textViewDateCible.getText().toString().trim();
                    if (nomProjet.isEmpty()||valeurMontantBudget.equals("0")||valeurMontantBudget.isEmpty()||dateCible.isEmpty()){
                        Toast.makeText(this,"Veuillez remplir les champs nécessaires",Toast.LENGTH_LONG).show();

                    } else {
                        Intent intent = getIntent();
                        this.id =intent.getStringExtra("id");
                        Toast.makeText(this,"Ve " + id,Toast.LENGTH_LONG).show();
                    }
                }

            }

        }
    }

    private static int getRecurrence(Spinner spn){
        String recurrence = spn.getSelectedItem().toString();
        int recurrenceIntValue=0;
        switch (recurrence){
            case "Mensuel":
                recurrenceIntValue = 30;
                break;
            case "Hebdomadaire":
                recurrenceIntValue = 7;
                break;
            case "Bi-hebdomadaire":
                recurrenceIntValue =14;
                break;
            case "Annuellement":
                recurrenceIntValue = 365;
                break;

        }
        return recurrenceIntValue;
    }


}