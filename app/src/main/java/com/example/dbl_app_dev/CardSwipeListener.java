package com.example.dbl_app_dev;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

class CardSwipeListener extends GestureDetector.SimpleOnGestureListener {
    static final int SWIPE_MIN_DISTANCE = 80;
    static final int SWIPE_THRESHOLD_VELOCITY = 100;
    private final SwipeHandler swipeHandler;
    private final boolean detectHorizontal;
    private final boolean detectVertical;

    public CardSwipeListener(SwipeHandler swipeHandler, boolean detectHorizontal, boolean detectVertical) {
        this.swipeHandler = swipeHandler;
        this.detectHorizontal = detectHorizontal;
        this.detectVertical = detectVertical;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            if (detectHorizontal) {
                Log.d("extra_debug", "Right to left fling detected");
                swipeHandler.swipedLeft();
            }
            return detectHorizontal;
        } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE
                && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            if (detectHorizontal) {
                Log.d("extra_debug", "Left to right fling detected");
                swipeHandler.swipedRight();
            }
            return detectHorizontal;
        }

        if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
            if (detectVertical) {
                Log.d("extra_debug", "Bottom to top fling detected");
                swipeHandler.swipedUp();
            }
            return detectVertical;
        } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE
                && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
            if (detectVertical) {
                Log.d("extra_debug", "Top to bottom fling detected");
                swipeHandler.swipedDown();
            }
            return detectVertical;
        }
        return false;
    }
}
