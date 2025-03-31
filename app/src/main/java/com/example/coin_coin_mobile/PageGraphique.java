package com.example.coin_coin_mobile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Shader;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;

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

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class PageGraphique extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout carteLayout;
    private HorizontalScrollView horizontalScrollView;
    private GestureDetector gestureDetector;
    private boolean isSwiping = false;
    private ImageButton btnRetour;
    private static final int SWIPE_THRESHOLD_VELOCITY = 500;
    private static final int SWIPE_MIN_DISTANCE = 50;
    private static final int SWIPE_MAX_DISTANCE = 400;

    private CardView btnDepot, btnRetrait, btnDate, btnConfig;
    private ActivityResultLauncher<Intent> aRL;

    private float montantObjectif = 1000f;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_page_graphique);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        btnRetour = (ImageButton) findViewById(R.id.btnRetour);
        btnRetour.setOnClickListener(this);
        btnDepot = (CardView) findViewById(R.id.btnDepot);
        btnDepot.setOnClickListener(this);
        btnRetrait = (CardView) findViewById(R.id.btnRetrait);
        btnRetrait.setOnClickListener(this);
        aRL = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(androidx.activity.result.ActivityResult result) {

                    }
                }
        );
        chargerScrollHorizontal();
        chargerGraphique();




    }

    @Override
    public void onClick(View v) {
        if(v==btnRetour) {
            Intent intent = new Intent(PageGraphique.this, Projet.class);
            aRL.launch(intent);
        }
        if(v == btnDepot){
            Intent intent = new Intent(PageGraphique.this, PageListe.class);
            intent.putExtra("TYPE", "Depot");
            aRL.launch(intent);
        }
        if(v == btnRetrait){
            Intent intent = new Intent(PageGraphique.this, PageListe.class);
            intent.putExtra("TYPE", "Retrait");
            aRL.launch(intent);
        }
    }
    @SuppressLint("ClickableViewAccessibility")
    public void chargerScrollHorizontal(){
        carteLayout = findViewById(R.id.carteLayout);
        horizontalScrollView = findViewById(R.id.horizontalScrollView);
        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
        if (carteLayout != null && horizontalScrollView != null) {

            //Logique pour swipe une courte distance et changer de carte
            gestureDetector = new GestureDetector(this, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
                    if (e1 == null || e2 == null) return false;

                    float deltaX = e2.getX() - e1.getX();

                    if (Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY && Math.abs(deltaX) > SWIPE_MIN_DISTANCE && Math.abs(deltaX) < SWIPE_MAX_DISTANCE) {
                        isSwiping = true;
                        int nombreCarte = carteLayout.getChildCount();
                        int horizontalScrollX = horizontalScrollView.getScrollX();
                        int screenWidth = Resources.getSystem().getDisplayMetrics().widthPixels;
                        int minDifference = Integer.MAX_VALUE;
                        int targetScrollX = 0;
                        int currentIndex = -1;

                        for (int i = 0; i < nombreCarte; i++) {
                            View carte = carteLayout.getChildAt(i);
                            int carteCenter = (carte.getLeft() + carte.getRight()) / 2;
                            int difference = Math.abs(horizontalScrollX + (screenWidth / 2) - carteCenter);

                            if (difference < minDifference) {
                                minDifference = difference;
                                targetScrollX = carte.getLeft() - (screenWidth / 2) + (carte.getWidth() / 2);
                                currentIndex = i;
                            }
                        }

                        if (deltaX > 0 && currentIndex > 0) {
                            View prevCard = carteLayout.getChildAt(currentIndex - 1);
                            targetScrollX = prevCard.getLeft() - (screenWidth / 2) + (prevCard.getWidth() / 2);
                        } else if (deltaX < 0 && currentIndex < nombreCarte - 1) {
                            View nextCard = carteLayout.getChildAt(currentIndex + 1);
                            targetScrollX = nextCard.getLeft() - (screenWidth / 2) + (nextCard.getWidth() / 2);
                        }

                        horizontalScrollView.smoothScrollTo(targetScrollX, 0);
                        return true;
                    }
                    return false;
                }
            });


            horizontalScrollView.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    boolean flingDetected = gestureDetector.onTouchEvent(event);
                    if (!flingDetected) {
                        isSwiping = false;
                    }
                    // Logique pour centrer la carte la plus proche du centre de l'ecran s'il n'y a pas eu de swipe
                    if (event.getAction() == MotionEvent.ACTION_UP) {
                        v.post(new Runnable() {
                            @Override
                            public void run() {
                                if (!isSwiping && carteLayout.getChildCount() > 0) {
                                    int nombreCarte = carteLayout.getChildCount();
                                    int horizontalScrollX = horizontalScrollView.getScrollX();
                                    int milieuEcran = horizontalScrollX + (screenWidth / 2);
                                    int minDifference = Integer.MAX_VALUE;
                                    int scrollToX = 0;

                                    for (int i = 0; i < nombreCarte; i++) {
                                        View carte = carteLayout.getChildAt(i);
                                        int carteCenter = (carte.getLeft() + carte.getRight()) / 2;
                                        int difference = Math.abs(milieuEcran - carteCenter);
                                        if (difference < minDifference) {
                                            minDifference = difference;
                                            scrollToX = carte.getLeft() - (screenWidth / 2) + (carte.getWidth() / 2);
                                        }
                                    }

                                    horizontalScrollView.smoothScrollTo(scrollToX, 0);
                                }
                            }
                        });
                    }
                    return flingDetected;
                }
            });


        } else {
            Log.e("MainActivity", "Error: layoutCarte or horizontalScrollView is null.");
        }
    }
    public void chargerGraphique(){
        //Le graphique
        LineChart lineChart = (LineChart) findViewById(R.id.lineChart);

        //Tableau avec les donnes a mettre dans le tableau
        ArrayList<Entry> entries = new ArrayList<>();
        // Donnee random
        for (int i = 0; i < 50; i++) {
            float yValue = (float) (Math.random() * 1000);
            entries.add(new Entry(i, yValue));
        }
        //La ligne dans le graphique
        LineDataSet lineDataSet = new LineDataSet(entries, "Maths");
        lineDataSet.setColor(Color.rgb(244, 206, 20));
        lineDataSet.setValueTextColor(Color.BLACK);
        lineDataSet.setLineWidth(2f);
        lineDataSet.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        lineDataSet.setCubicIntensity(0.2f);
        lineDataSet.setCircleColor(Color.BLACK);
        lineDataSet.setCircleHoleColor(Color.rgb(244, 206, 20));
        //Sous la ligne
        lineDataSet.setCubicIntensity(0.2f);
        lineDataSet.setDrawFilled(true);
        GradientDrawable drawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.argb(75, 244, 206, 20), Color.argb(0, 244, 206, 20)}
        );
        lineDataSet.setFillDrawable(drawable);
        LineData lineData = new LineData(lineDataSet);

        //X
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(45);
        xAxis.setTextSize(12f);
        xAxis.setAxisMinimum(1);
        xAxis.setAxisMaximum(50); //Date objectif fin ======
        xAxis.setDrawGridLines(false);
        // Info sur l'axe x
        SimpleDateFormat sdf = new SimpleDateFormat("dd MMM");
        Calendar calendar = Calendar.getInstance();
        calendar.set(2025, Calendar.MARCH, 1); //Date du debut
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                Calendar tempCalendar = (Calendar) calendar.clone();
                tempCalendar.add(Calendar.DAY_OF_YEAR, (int) value);
                return sdf.format(tempCalendar.getTime());
            }
        });

        // Y
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setTextSize(14f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(montantObjectif); // Objectif ==========
        leftAxis.setDrawGridLines(true);
        leftAxis.setGranularity(100f);
        //Ajouter $
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "$" + (int) value;
            }
        });
        lineChart.getAxisRight().setEnabled(false);
        lineChart.getDescription().setEnabled(false);
        lineChart.setData(lineData);
        lineChart.invalidate();
    }

}
