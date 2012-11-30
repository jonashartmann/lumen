package com.hartmann.lumen.core.tools.event;

import com.hartmann.lumen.core.tools.event.SimpleEventBus.MvpHandlerRegistration;

/**
 * Event bus to add the functionality of firing and handling events.
 * 
 * @author Jonas Hartmann &lt;jonasharty@gmail.com&gt;
 * @since 30.11.2012
 */
public interface EventBus {
	/**
	 * Fires a new event.
	 * 
	 * @param event
	 */
	public void fireEvent(MvpEvent event);

	/**
	 * Adds a new handler to handle a specific event.
	 * 
	 * @param eventType
	 *            - type of the event
	 * @param handler
	 *            - handler to handle the event
	 * @return an {@link MvpHandlerRegistration} to be able to remove the
	 *         handler.
	 */
	public <E extends MvpEvent> MvpHandlerRegistration addHandler(
			Class<E> eventClazz, MvpEventHandler<E> handler);

}
