package com.example.coin_coin_mobile;

import android.annotation.SuppressLint;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class PageGraphique extends AppCompatActivity implements View.OnClickListener {
    private LinearLayout carteLayout;
    private HorizontalScrollView horizontalScrollView;
    private GestureDetector gestureDetector;
    private boolean isSwiping = false;
    private static final int SWIPE_THRESHOLD_VELOCITY = 500;
    private static final int SWIPE_MIN_DISTANCE = 50;
    private static final int SWIPE_MAX_DISTANCE = 400;

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

    @Override
    public void onClick(View v) {
        //Clique sur les cartes
    }
}
