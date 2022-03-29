package com.example.dbl_app_dev;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

class CardSwipeListener extends GestureDetector.SimpleOnGestureListener {
    static final int SWIPE_MIN_DISTANCE = 80;
    static final int SWIPE_THRESHOLD_VELOCITY = 100;
    private RatingHandler ratingHandler;

    public CardSwipeListener(RatingHandler ratingHandler) {
        this.ratingHandler = ratingHandler;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if(e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            Log.d("extra_debug", "Right to left fling detected");
            // Right to left = dislike
            ratingHandler.negativeRating();
            return true;
        }  else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            Log.d("extra_debug", "Left to right fling detected");
            // Left to right = like
            ratingHandler.positiveRating();
            return true;
        }

        if(e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
            // Bottom to top
            Log.d("extra_debug", "Bottom to top fling detected");
            return false;
        }  else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
            Log.d("extra_debug", "Top to bottom fling detected");
            // Top to bottom = neutral
            ratingHandler.neutralRating();
            return false;
        }
        return false;
    }
}

