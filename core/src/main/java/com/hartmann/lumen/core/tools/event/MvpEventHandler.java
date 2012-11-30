package com.hartmann.lumen.core.tools.event;

import java.io.Serializable;

/**
 * Interface for MVP event handlers.
 * 
 * @author Jonas Hartmann &lt;jonasharty@gmail.com&gt;
 * @since 30.11.2012
 * @param <T>
 *            Type of the event to be handled.
 */
public interface MvpEventHandler<T extends MvpEvent> extends Serializable {
	/**
	 * Called upon event being fired.
	 * 
	 * @param event
	 *            - The event that was fired
	 */
	public void onEvent(T event);
}
