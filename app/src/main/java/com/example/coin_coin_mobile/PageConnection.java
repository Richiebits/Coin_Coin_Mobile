package com.example.coin_coin_mobile;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PageConnection extends AppCompatActivity implements View.OnClickListener{

    private Button btnConnexion;
    private EditText editTextCourriel;
    private EditText editTextPassword;
    private TextView txtViewCreation;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_page_connection);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        btnConnexion = findViewById(R.id.btnConnecter);
        btnConnexion.setOnClickListener(this);
        editTextCourriel = findViewById(R.id.editCourriel);
        editTextPassword = findViewById(R.id.editMotDePasse);
        txtViewCreation = findViewById(R.id.textViewCreation);
        txtViewCreation.setOnClickListener(this);
        ConstraintLayout topConstraint = findViewById(R.id.constraintTop);
        Drawable drawable = ContextCompat.getDrawable(this, R.drawable.layer_list_conn);
        LayerDrawable layerDrawable = (LayerDrawable) drawable;
        assert layerDrawable != null;
        Drawable imageLogo = layerDrawable.getDrawable(1);
        imageLogo.setAlpha(26);
        topConstraint.setBackground(drawable);


        activityResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {

                    }
                });


    }

    @Override
    public void onClick(View v) {
        if (v==btnConnexion){
            Intent intentConnexion = new Intent (PageConnection.this,PageAccueil.class);
            activityResultLauncher.launch(intentConnexion);
        }
        if (v==txtViewCreation){
            Intent intentCreation = new Intent (PageConnection.this,PageCreationCompte.class);
            intentCreation.putExtra("keyCreate","create");
            activityResultLauncher.launch(intentCreation);
        }

    }
}