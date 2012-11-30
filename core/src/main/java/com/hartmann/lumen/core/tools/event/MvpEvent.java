package com.hartmann.lumen.core.tools.event;

import java.io.Serializable;

/**
 * Simple interface of an MVP event.
 * 
 * @author Jonas Hartmann
 * 
 */
public interface MvpEvent extends Serializable{

	public String getEventName();
}
