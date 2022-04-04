package com.example.dbl_app_dev;

public interface SwipeHandler {
    void swipedRight();

    void swipedLeft();

    default void swipedDown() {
    }

    ;

    default void swipedUp() {
    }

    ;
}
