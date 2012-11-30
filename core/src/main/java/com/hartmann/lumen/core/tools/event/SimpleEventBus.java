package com.hartmann.lumen.core.tools.event;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple implementation of an EventBus.
 * 
 * @author Jonas Hartmann
 * 
 */
public class SimpleEventBus implements EventBus {

    private List<MvpHandlerRegistration> registrations = new ArrayList<SimpleEventBus.MvpHandlerRegistration>();

    /**
     * Registration handler of a registered mvp event. With this class you have
     * the possibility to remove the handler without knowing the event handler
     * or event bus instance.
     * 
     * @author Jonas Hartmann
     */
    public class MvpHandlerRegistration implements Serializable {
	private static final long serialVersionUID = 3513695460546356186L;
	@SuppressWarnings("rawtypes")
	private MvpEventHandler handler;
	private Class<? extends MvpEvent> event;

	/**
	 * Removes the registered mvp handler.
	 */
	public void removeHandler() {
	    registrations.remove(this);
	}
    }

    /**
     * Registers a handler/listener to a specific event.
     * 
     * @param eventClazz
     *            class of the event
     * @param handler
     *            handler/listener for listening of the given event
     * @return handler registration for the registered handler.
     */
    public <E extends MvpEvent> MvpHandlerRegistration addHandler(
	    Class<E> eventClazz, MvpEventHandler<E> handler) {
		MvpHandlerRegistration registration = new MvpHandlerRegistration();
		registration.event = eventClazz;
		registration.handler = handler;
		registrations.add(registration);
		return registration;
    }

    /**
     * Fires a event and calls every registered mvp event handler.
     * 
     * @param event
     *            event to fire.
     */
    public void fireEvent(MvpEvent event) {
	for (MvpHandlerRegistration registration : registrations) {
			if (event.getClass() == registration.event) {
				registration.handler.onEvent(event);
			}
		}
    }
}
