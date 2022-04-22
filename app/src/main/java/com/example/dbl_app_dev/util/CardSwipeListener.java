package com.example.dbl_app_dev.util;

import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;

import com.example.dbl_app_dev.util.SwipeHandler;

/**
 * Extends the GestureDetector class, and listens for swipe events
 * <p>
 * The class contains a SwipeHandler object that has to be given on construction. Through
 * this object the correct callback will occur (each swipe direction has its' own callback, and
 * can therefore be handled separately)
 */
public class CardSwipeListener extends GestureDetector.SimpleOnGestureListener {
    // define the minimum swiping distance & velocity needed to trigger a 'swipe' event
    static final int SWIPE_MIN_DISTANCE = 80;
    static final int SWIPE_THRESHOLD_VELOCITY = 100;
    private final SwipeHandler swipeHandler;
    private final boolean detectHorizontal;
    private final boolean detectVertical;

    /**
     * @param swipeHandler     a given SwipeHandler object (will not change throughout the lifetime
     *                         of this CardSwipeListener object)
     * @param detectHorizontal given boolean value that dictates if horizontal swipes are detected
     * @param detectVertical   given boolean value that dictates if vertical swipes are detected
     */
    public CardSwipeListener(SwipeHandler swipeHandler, boolean detectHorizontal, boolean detectVertical) {
        this.swipeHandler = swipeHandler;
        this.detectHorizontal = detectHorizontal;
        this.detectVertical = detectVertical;
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        if (e1.getX() - e2.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            // thresholds surpassed
            if (detectHorizontal) {
                // let the swipeHandler handle the event
                swipeHandler.swipedLeft();
            }
            return detectHorizontal;
        } else if (e2.getX() - e1.getX() > SWIPE_MIN_DISTANCE && Math.abs(velocityX) > SWIPE_THRESHOLD_VELOCITY) {
            // thresholds surpassed
            if (detectHorizontal) {
                // let the swipeHandler handle the event
                swipeHandler.swipedRight();
            }
            return detectHorizontal;
        }

        if (e1.getY() - e2.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
            // thresholds surpassed
            if (detectVertical) {
                // let the swipeHandler handle the event
                swipeHandler.swipedUp();
            }
            return detectVertical;
        } else if (e2.getY() - e1.getY() > SWIPE_MIN_DISTANCE && Math.abs(velocityY) > SWIPE_THRESHOLD_VELOCITY) {
            // thresholds surpassed
            if (detectVertical) {
                // let the swipeHandler handle the event
                swipeHandler.swipedDown();
            }
            return detectVertical;
        }
        return false;
    }
}
