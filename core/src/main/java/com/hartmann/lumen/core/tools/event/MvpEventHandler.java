package com.hartmann.lumen.core.tools.event;

import java.io.Serializable;

/**
 * Interface for the mvp event handlers.
 * 
 * @author Jonas Hartmann
 * 
 * @param <T>
 *            event to this handler
 */
public interface MvpEventHandler<T extends MvpEvent> extends Serializable {

    public void onEvent(T event);
}
