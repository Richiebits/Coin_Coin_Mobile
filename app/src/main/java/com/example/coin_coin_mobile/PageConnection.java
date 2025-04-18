package com.example.coin_coin_mobile;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.graphics.drawable.VectorDrawable;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import org.json.JSONException;
import org.json.JSONObject;


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
        ScalableDrawable scalableDrawable = new ScalableDrawable(imageLogo);
        scalableDrawable.setScaleFactor(1.4f);
        layerDrawable.setDrawable(1, scalableDrawable);
        imageLogo.setAlpha(26);
        scalableDrawable.setInsets(-350, -280, 0, 0);
        topConstraint.setBackground(layerDrawable);
        topConstraint.invalidate();

        ConstraintLayout bottomConstraint = findViewById(R.id.constraintLayoutBottom);
        Drawable drawableBot = ContextCompat.getDrawable(this, R.drawable.layer_list_conn_footer);
        LayerDrawable layerDrawableBot = (LayerDrawable) drawableBot;
        assert layerDrawableBot != null;
        Drawable imageLogoBot = layerDrawableBot.getDrawable(1);
        ScalableDrawable scalableDrawableBot = new ScalableDrawable(imageLogoBot);
        scalableDrawableBot.setScaleFactor(2.0f);
        layerDrawableBot.setDrawable(1, scalableDrawableBot);
        imageLogoBot.setAlpha(26);
        scalableDrawableBot.setInsets(-300, -730, 0, 0);
        bottomConstraint.setBackground(layerDrawableBot);
        bottomConstraint.invalidate();

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
            String email = editTextCourriel.getText().toString();
            String password = editTextPassword.getText().toString();
            String jsonBody = "{\"email\":\"" + email + "\", \"mot_de_passe\":\"" + password + "\"}";


            FetchApi.fetchData("client/connexion",
                            "POST", jsonBody, new OnDataFetchedListener() {
                @Override
                public void onSuccess(String data) throws JSONException {
                    if (!data.equalsIgnoreCase("false")){

                        JSONObject JsonData = new JSONObject(data);
                        String id = JsonData.getString("id");
                        Intent intentConnexion = new Intent(PageConnection.this,PageAccueil.class);
                        intentConnexion.putExtra("USER_ID", id );
                        activityResultLauncher.launch(intentConnexion);
                    } else {
                        editTextPassword.setText("");
                        Toast.makeText(PageConnection.this, "Mot de passe incorrect!",
                                Toast.LENGTH_SHORT).show();

                    }

                }
                @Override
                public void onError(String error) {
                    Log.e("ERROR", "Fetch error: " + error);
                }
            });

        }
        if (v==txtViewCreation){
            Intent intentCreation = new Intent(PageConnection.this,PageCreationCompte.class);
            intentCreation.putExtra("keyCreate","create");
            activityResultLauncher.launch(intentCreation);
        }

    }
}