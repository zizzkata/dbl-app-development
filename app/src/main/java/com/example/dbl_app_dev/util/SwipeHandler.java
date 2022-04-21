package com.example.dbl_app_dev.util;

public interface SwipeHandler {
    void swipedRight();

    void swipedLeft();

    default void swipedDown() {
    }

    default void swipedUp() {
    }
}
