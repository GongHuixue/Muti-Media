package com.example.android.multmedia.notification;

import java.util.Observable;
import java.util.Observer;

public class NotificationHandler extends Observable {
    // single instance holder
    private static NotificationHandler singletonInstance = null;

    // private constructor
    private NotificationHandler() {
    }

    // get single instance of NotificationHandler class
    public static NotificationHandler getInstance() {
        if (singletonInstance == null) {
            singletonInstance = new NotificationHandler();
        }
        return singletonInstance;
    }

    public void registerForNotification(Observer obs) {
        this.addObserver(obs);
    }

    public void unregisterForNotification(Observer obs) {
        this.deleteObserver(obs);
    }

    public void notifyAllObservers(final int actionID, final String message) {
        this.setChanged(); // important call to trigger all notifications

        singletonInstance.notifyObservers(new NotificationInfoObject(actionID, message));
    }

    public void removeAllObservers() {
        // remove all the observers, clear up
        this.deleteObservers();
    }
}
