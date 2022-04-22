package com.example.dbl_app_dev.util;

/**
 * Defines an interface for objects that can handle swipe Events on cards
 */
public interface SwipeHandler {
    /**
     * Contains reaction to a card swipe to the right
     */
    void swipedRight();

    /**
     * Contains reaction to a card swipe to the left
     */
    void swipedLeft();

    /**
     * Contains reaction to a card swipe down
     */
    default void swipedDown() {
    }

    /**
     * Contains reaction to a card being swiped up
     */
    default void swipedUp() {
    }
}
