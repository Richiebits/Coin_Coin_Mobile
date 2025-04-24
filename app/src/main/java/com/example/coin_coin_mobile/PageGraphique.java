package com.example.coin_coin_mobile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Button;
import android.widget.HorizontalScrollView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.chrono.ChronoLocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;

import adapteur.DepotAdapter;
import modele.Depot;
import modele.Transaction;

public class PageGraphique extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout carteLayout;
    private HorizontalScrollView horizontalScrollView;
    private GestureDetector gestureDetector;
    private boolean isSwiping = false;
    private ImageButton btnRetour;
    private static final int SWIPE_THRESHOLD_VELOCITY = 300;
    private static final int SWIPE_MIN_DISTANCE = 50;
    private static final int SWIPE_MAX_DISTANCE = 400;
    private static int montantObjectif = 0;
    private String id, token, projetNom;
    private int projetId;

    private CardView btnDepot, btnRetrait, btnDate, btnConfig,btnSupp;
    private ActivityResultLauncher<Intent> aRL;
    private TextView txtTitre;
    ArrayList<Transaction> transactionList = new ArrayList<>();
    private static String dateFin = "";
    private static String dateDebut;
    private boolean noFinDate = false;
    private static boolean flagImpossible = false;
    private static boolean flagIreal = false;



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
        txtTitre = (TextView) findViewById(R.id.txtTitre);
        btnSupp = (CardView)findViewById(R.id.btnSupprimer);
        btnSupp.setOnClickListener(this);
        Intent intent = getIntent();
        this.id = intent.getStringExtra("USER_ID");
        this.token = intent.getStringExtra("TOKEN");
        this.projetId = intent.getIntExtra("PROJET_ID", -1);
        this.projetNom = intent.getStringExtra("PROJET_NOM");
        txtTitre.setText(projetNom);
        Log.d("DEBUGG", token);
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
        //fecthing data
        fetchTransactionAsync(new OnDataFetchedListener() {
            @Override
            public void onSuccess(String data) {
                fetchDateDebutFin(new OnDataFetchedListener() {
                    @Override
                    public void onSuccess(String data) {
                        fetchMontantObjectifAsync(new OnDataFetchedListener() {
                            @Override
                            public void onSuccess(String data) throws JSONException {
                                if (dateFin.isEmpty() || dateFin.equals("null")) {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                        if (!LocalDate.now().toString().equals(dateDebut)) {
                                            dateFin = LocalDate.now().toString();
                                            noFinDate = true;
                                        } else {
                                            dateFin = LocalDate.now().plusDays(1).toString();
                                            noFinDate = true;
                                        }
                                    }
                                }
                                if (noFinDate) {
                                    trouverDateFin(transactionList);
                                }
                                transactionList = processTransactions(transactionList);
                                for (Transaction transaction : transactionList) {
                                    Log.d("GRAPHDEBUG", "trans :" + String.valueOf(transaction));
                                }
                                chargerGraphique();
                                Log.d("ERREUR", "dates :" + dateDebut + "      " + dateFin);
                            }

                            @Override
                            public void onError(String error) {
                                Log.d("ERREUR", "erreur fetch projet pour le graphique :" + error);
                            }
                        });
                    }

                    @Override
                    public void onError(String error) {
                        Log.d("ERREUR", "erreur fetch transaction pour le graphique :" + error);
                    }
                });
            }

            @Override
            public void onError(String error) {
                Log.d("ERREUR", "erreur fetch transaction pour le graphique :" + error);
            }
        });

    }

    @Override
    public void onClick(View v) {
        if (v == btnRetour) {
            Intent intent = new Intent(PageGraphique.this, PageProjet.class);
            intent.putExtra("USER_ID", id);
            intent.putExtra("TOKEN", token);
            startActivity(intent);
        }
        if (v == btnDepot) {
            Intent intent = new Intent(PageGraphique.this, PageListe.class);
            intent.putExtra("USER_ID", id);
            intent.putExtra("TOKEN", token);
            intent.putExtra("TYPE", "Depot");
            intent.putExtra("PROJET_ID", projetId);
            intent.putExtra("PROJET_NOM", projetNom);
            startActivity(intent);
        }
        if (v == btnRetrait) {
            Intent intent = new Intent(PageGraphique.this, PageListe.class);
            intent.putExtra("USER_ID", id);
            intent.putExtra("TOKEN", token);
            intent.putExtra("TYPE", "Retrait");
            intent.putExtra("PROJET_ID", projetId);
            intent.putExtra("PROJET_NOM", projetNom);
            startActivity(intent);
        }
        if (v== btnSupp){
            AlertDialog dialogSupp = new AlertDialog.Builder(this)
                    .setTitle("Supprimer le projet?")
                    .setPositiveButton("SUPPRIMER", null)
                    .setNegativeButton("ANNULER", null)
                    .create();
            dialogSupp.setOnShowListener(dialogInterface -> {
                Button positiveButton = dialogSupp.getButton(AlertDialog.BUTTON_POSITIVE);
                positiveButton.setOnClickListener(view -> {
                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", "Bearer " + token);
                    FetchApi.fetchData("projet/delete/" + projetId +"/" + id, "DELETE", null, headers, new OnDataFetchedListener() {

                        @Override
                        public void onSuccess(String data) throws JSONException {
                            dialogSupp.dismiss();
                            Toast.makeText(PageGraphique.this,"Projet " + projetNom + " supprimé!",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(PageGraphique.this,PageProjet.class);
                            intent.putExtra("USER_ID",id);
                            intent.putExtra("TOKEN",token);
                            startActivity(intent);


                        }

                        @Override
                        public void onError(String error) {
                            Log.e("ERROR", "Fetch depots error: " + error);
                        }
                    });


                });


            });
            dialogSupp.show();



        }
    }

    @SuppressLint("ClickableViewAccessibility")
    public void chargerScrollHorizontal() {
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

    public void chargerGraphique() {
        LineChart lineChart = findViewById(R.id.lineChart);

        ArrayList<String> xDates = new ArrayList<>();
        SimpleDateFormat sdfFull = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
        Date startDate, endDate;

        Calendar todayCalendar = Calendar.getInstance();
        todayCalendar.set(Calendar.HOUR_OF_DAY, 0);
        todayCalendar.set(Calendar.MINUTE, 0);
        todayCalendar.set(Calendar.SECOND, 0);
        todayCalendar.set(Calendar.MILLISECOND, 0);
        Date today = todayCalendar.getTime();

        try {
            startDate = sdfFull.parse(dateDebut);
            endDate = sdfFull.parse(dateFin);
        } catch (ParseException e) {
            e.printStackTrace();
            return;
        }

        Map<String, Float> groupedTransactions = new TreeMap<>();
        for (Transaction t : transactionList) {
            try {
                Date tDate = sdfFull.parse(t.getDate());
                if (!tDate.before(startDate) && !tDate.after(endDate)) {
                    String dateStr = sdfFull.format(tDate);
                    groupedTransactions.put(dateStr, groupedTransactions.getOrDefault(dateStr, 0f) + t.getMontant());
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }

        float cumulativeBalance = 0;
        ArrayList<Entry> pastEntries = new ArrayList<>();
        ArrayList<Entry> futureEntries = new ArrayList<>();
        float maxValue = 0;
        for (int i = 0; i < daysBetween(startDate, endDate); i++) {
            Date currentDate = addDays(startDate, i);
            String currentDateStr = sdfFull.format(currentDate);
            xDates.add(new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(currentDate));

            if (groupedTransactions.containsKey(currentDateStr)) {
                cumulativeBalance += groupedTransactions.get(currentDateStr);
            }
            if (cumulativeBalance > maxValue) {
                maxValue = cumulativeBalance;
            }

            Entry entry = new Entry(i, cumulativeBalance);

            if (currentDate.after(today)) {
                futureEntries.add(entry);
            } else {
                pastEntries.add(entry);
                Log.d("GraphData", "x: " + i + ", y: " + cumulativeBalance);
            }
        }

        // Past data jaune
        LineDataSet pastDataSet = new LineDataSet(pastEntries, "Solde");
        pastDataSet.setColor(Color.rgb(244, 206, 20));
        pastDataSet.setValueTextColor(Color.BLACK);
        pastDataSet.setLineWidth(2f);
        pastDataSet.setDrawCircles(false);
        pastDataSet.setDrawFilled(true);
        GradientDrawable drawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.argb(120, 244, 206, 20), Color.argb(10, 244, 206, 20)}
        );
        pastDataSet.setFillDrawable(drawable);
        pastDataSet.setDrawValues(true);
        pastDataSet.setValueFormatter(new ValueFormatter() {
            private float lastValue = Float.MIN_VALUE;

            @Override
            public String getFormattedValue(float value) {
                if (value == lastValue) return "";
                lastValue = value;
                return "$" + (int) value;
            }
        });

        // les donnees future en gris
        LineDataSet futureDataSet = new LineDataSet(futureEntries, "Prévision");
        futureDataSet.setColor(Color.GRAY);
        futureDataSet.setLineWidth(2f);
        futureDataSet.enableDashedLine(8f, 10f, 0f);
        futureDataSet.setDrawCircles(false);
        futureDataSet.setDrawValues(false);

        LineData lineData = new LineData(pastDataSet, futureDataSet);
        lineChart.setData(lineData);

        // Customize X Axis
        XAxis xAxis = lineChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setGranularity(1f);
        xAxis.setLabelRotationAngle(45);
        xAxis.setTextSize(10f);
        xAxis.setDrawGridLines(false);
        xAxis.setAxisMinimum(0);
        xAxis.setAxisMaximum(xDates.size() - 1);
        xAxis.setLabelCount(xDates.size() / 5, false);
        xAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getAxisLabel(float value, AxisBase axis) {
                int i = (int) value;
                return (i >= 0 && i < xDates.size() && i % 2 == 0) ? xDates.get(i) : "";
            }
        });

        // Customize Y Axis
        YAxis leftAxis = lineChart.getAxisLeft();
        leftAxis.setTextSize(14f);
        leftAxis.setAxisMinimum(0f);
        leftAxis.setAxisMaximum(Math.max(montantObjectif, maxValue));
        leftAxis.setDrawGridLines(false);
        leftAxis.setGranularity(100f);
        leftAxis.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value) {
                return "$" + (int) value;
            }
        });

        lineChart.getAxisRight().setEnabled(false);
        lineChart.getDescription().setEnabled(false);

        // Ligne objectif
        LimitLine objectifLine = new LimitLine(montantObjectif, "Objectif");
        objectifLine.setLineColor(Color.argb(128, 0, 128, 0));
        objectifLine.setLineWidth(2f);
        objectifLine.enableDashedLine(10f, 5f, 0f);
        objectifLine.setTextColor(Color.argb(128, 0, 128, 0));
        objectifLine.setTextSize(12f);
        objectifLine.setLabelPosition(LimitLine.LimitLabelPosition.LEFT_BOTTOM);
        leftAxis.addLimitLine(objectifLine);

       //ligne aujourdhui
        int todayIndex = xDates.indexOf(new SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(today));
        if (todayIndex != -1) {
            LimitLine todayLine = new LimitLine(todayIndex, "Aujourd'hui");
            todayLine.setLineColor(Color.argb(100, 255, 0, 0));
            todayLine.setLineWidth(2f);
            todayLine.enableDashedLine(10f, 5f, 0f);
            todayLine.setLabelPosition(LimitLine.LimitLabelPosition.RIGHT_TOP);
            todayLine.setTextColor(Color.argb(100, 255, 0, 0));
            xAxis.addLimitLine(todayLine);
        }
        //  "OBJECTIF INATTEIGNABLE"
        if(flagImpossible){
            Description description = new Description();
            description.setText("OBJECTIF IMPOSSIBLE À ATTEINDRE   \n");
            description.setTextColor(Color.RED);
            description.setTextSize(16f);
            lineChart.setDescription(description);
        }else if(flagIreal){
            Description description = new Description();
            description.setText("Le soleil va mourir avant d'avoir atteint l'objectif.");
            description.setTextColor(Color.RED);
            description.setTextSize(14f);
            lineChart.setDescription(description);

        }

        lineChart.invalidate();
        float scaleFactorX = 1.1f;
        lineChart.setScaleX(scaleFactorX);
    }

    private int daysBetween(Date startDate, Date endDate) {
        long diff = endDate.getTime() - startDate.getTime();
        return (int) (diff / (1000 * 60 * 60 * 24));
    }

    private Date addDays(Date date, int days) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.add(Calendar.DATE, days);
        return cal.getTime();
    }

    private void fetchTransactionAsync(OnDataFetchedListener listener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        FetchApi.fetchData("historique/projet/" + projetId, "GET", null, headers, new OnDataFetchedListener() {

            @Override
            public void onSuccess(String data) throws JSONException {
                if (!data.equalsIgnoreCase("false")) {
                    JSONArray depotsArray = new JSONArray(data);
                    for (int i = 0; i < depotsArray.length(); i++) {
                        JSONObject jsonData = depotsArray.getJSONObject(i);
                        int id = jsonData.getInt("id");
                        String type = jsonData.getString("type");
                        int montant = jsonData.getInt("montant");
                        String date = jsonData.getString("date_histo");
                        int recurrence = jsonData.getInt("recurrence");
                        if (type.equalsIgnoreCase("retrait") && montant > 0) {
                            montant = -montant;
                        }

                        Transaction transaction = new Transaction(id, montant, date, type, recurrence);
                        transactionList.add(transaction);
                        Collections.sort(transactionList, Comparator.comparing(Transaction::getDate));
                    }
                } else {
                    Log.e("ERROR", "Empty or invalid data received: " + data);
                    listener.onError("Aucune donnée reçue");
                }
                listener.onSuccess(data);

            }

            @Override
            public void onError(String error) {
                Log.e("ERROR", "Fetch depots error: " + error);
            }
        });


    }

    private void fetchDateDebutFin(OnDataFetchedListener listener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        FetchApi.fetchData("budget/projet/" + projetId, "GET", null, headers, new OnDataFetchedListener() {

            @Override
            public void onSuccess(String data) throws JSONException {
                if (!data.equalsIgnoreCase("false")) {
                    JSONArray depotsArray = new JSONArray(data);
                    for (int i = 0; i < depotsArray.length(); i++) {
                        JSONObject jsonData = depotsArray.getJSONObject(i);
                        dateDebut = jsonData.getString("date_debut");
                        dateFin = jsonData.getString("date_fin");
                    }
                } else {
                    Log.e("ERROR", "Empty or invalid data received: " + data);
                    listener.onError("Aucune donnée reçue");
                }
                listener.onSuccess(data);

            }

            @Override
            public void onError(String error) {
                Log.e("ERROR", "Fetch depots error: " + error);
            }
        });

    }

    private void fetchMontantObjectifAsync(OnDataFetchedListener listener) {
        Map<String, String> headers = new HashMap<>();
        headers.put("Authorization", "Bearer " + token);
        FetchApi.fetchData("projet/" + projetId, "GET", null, headers, new OnDataFetchedListener() {

            @Override
            public void onSuccess(String data) throws JSONException {
                if (!data.equalsIgnoreCase("false")) {
                    JSONArray depotsArray = new JSONArray(data);
                    for (int i = 0; i < depotsArray.length(); i++) {
                        JSONObject jsonData = depotsArray.getJSONObject(i);
                        montantObjectif = jsonData.getInt("but_epargne");
                    }
                } else {
                    Log.e("ERROR", "Empty or invalid data received: " + data);
                    listener.onError("Aucune donnée reçue");
                }
                listener.onSuccess(data);
            }

            @Override
            public void onError(String error) {
                Log.e("ERROR", "Fetch depots error: " + error);
            }
        });
    }

    private static ArrayList<Transaction> processTransactions(ArrayList<Transaction> transactionList) {
        ArrayList<Transaction> tempTransactionList = new ArrayList<>();
        DateTimeFormatter formatter = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            LocalDate endDate = LocalDate.parse(dateFin, formatter);

            for (Transaction t : transactionList) {
                int recurrence = t.getRecurrence();
                LocalDate currentDate = LocalDate.parse(t.getDate(), formatter);
                //en mettre une par default
                tempTransactionList.add(new Transaction(t.getId(), t.getMontant(), currentDate.format(formatter), t.getType(), 0));
                //Ajouter tant que cest avant ajourdhui
                while (recurrence > 0) {
                    currentDate = currentDate.plusDays(recurrence);
                    if (currentDate.isAfter(endDate)) {
                        break;
                    }
                    tempTransactionList.add(new Transaction(t.getId(), t.getMontant(), currentDate.format(formatter), t.getType(), 0));
                }
            }
        }

        return tempTransactionList;
    }

    private static void trouverDateFin(ArrayList<Transaction> transactionList) {
        int montantInstant = 0;
        double montantParJour = 0;
        for (Transaction tran : transactionList) {
            switch (tran.getRecurrence()) {
                case 0:
                    montantInstant += tran.getMontant();
                    break;
                case 7:
                    montantParJour += ((double) tran.getMontant() / 7);
                    break;
                case 14:
                    montantParJour += ((double) tran.getMontant() / 14);
                    break;
                case 30:
                    montantParJour += ((double) tran.getMontant() / 30);
                    break;
                case 365:
                    montantParJour += ((double) tran.getMontant() / 365);
                    break;
            }

        }
        if (montantObjectif > montantInstant && montantParJour <= 0) {
            flagImpossible = true;
        } else {
            flagImpossible = false;
            double nombreJour = (montantObjectif - montantInstant) / montantParJour;

            if (nombreJour > 10000.0) {
                flagIreal = true;
            }else{
                flagIreal = false;
            }

            DateTimeFormatter formatter = null;
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                LocalDate today = LocalDate.now();
                LocalDate endDate = today.plusDays((int)nombreJour);
                dateFin = endDate.format(formatter);
            }
        }

    }

}
