package com.example.coin_coin_mobile;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;


//Cette classe a été généré par ChatGPT pour régler un problème d'esthétique.
public class ScalableDrawable extends Drawable {

    private Drawable mDrawable;
    private Matrix mMatrix;
    private float mScaleFactor = 1.0f;  // Default scale factor

    private int mInsetLeft = 0, mInsetTop = 0, mInsetRight = 0, mInsetBottom = 0;

    public ScalableDrawable(Drawable drawable) {
        this.mDrawable = drawable;
        mMatrix = new Matrix();
    }

    // Set the scale factor to scale the image
    public void setScaleFactor(float scaleFactor) {
        mScaleFactor = scaleFactor;
        invalidateSelf();  // Trigger a redraw
    }

    // Set the insets for the drawable (e.g., padding or positioning adjustments)
    public void setInsets(int left, int top, int right, int bottom) {
        this.mInsetLeft = left;
        this.mInsetTop = top;
        this.mInsetRight = right;
        this.mInsetBottom = bottom;
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        // Get the intrinsic width and height of the drawable
        int width = mDrawable.getIntrinsicWidth();
        int height = mDrawable.getIntrinsicHeight();

        // Set the bounds for the drawable (ensure the drawable has a size to draw)
        mDrawable.setBounds(mInsetLeft, mInsetTop, width + mInsetLeft, height + mInsetTop);

        // Apply scaling using the matrix
        mMatrix.setScale(mScaleFactor, mScaleFactor);

        // Apply the matrix to the canvas (scale the image)
        canvas.save();
        canvas.concat(mMatrix);  // Apply the scaling transformation

        // Draw the original drawable onto the canvas with the scaling transformation
        mDrawable.draw(canvas);

        canvas.restore();
    }

    @Override
    public void setAlpha(int alpha) {
        mDrawable.setAlpha(alpha);
    }

    @Override
    public void setColorFilter(ColorFilter colorFilter) {
        mDrawable.setColorFilter(colorFilter);
    }

    @Override
    public int getOpacity() {
        return mDrawable.getOpacity();
    }
}
