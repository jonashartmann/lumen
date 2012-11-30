package com.hartmann.lumen.core.tools.bt;

import java.util.ArrayList;
import java.util.Collection;

/**
 * Handles registration and notification of observers.<br>
 * <br>
 * It works like a Delegate.
 * 
 * @author Jonas Hartmann
 * 
 */
public class BehaviorObserverDelegate {

    /**
     * Object handled by the delegate.
     * 
     * @author Jonas Hartmann
     * 
     */
    public interface BehaviorObserver {
	/**
	 * Method called on delegation.
	 */
	public void onComplete();
    }

    /**
     * Collection of registered observers.
     */
    public static Collection<BehaviorObserver> observers = new ArrayList<BehaviorObserver>();

    public BehaviorObserverDelegate() {
    }

    /**
     * Are there observers?
     * 
     * @return true if no observers are registered in the delegate.
     * @see BehaviorObserverDelegate
     */
    public boolean empty() {
	return BehaviorObserverDelegate.observers.isEmpty();
    }

    /**
     * Notifies all observers registered in the delegate.
     * 
     * @see BehaviorObserverDelegate
     */
    public void notifyObservers() {
	for (BehaviorObserver observer : BehaviorObserverDelegate.observers) {
	    observer.onComplete();
	}
    }

    /**
     * Registers a new observer.
     * 
     * @param observer
     *            - the observer to register.
     * @return Returns a delegate capable of notifying the observer registered.
     */
    public static BehaviorObserverDelegate register(BehaviorObserver observer) {
	observers.add(observer);
	return new BehaviorObserverDelegate();
    }

}
