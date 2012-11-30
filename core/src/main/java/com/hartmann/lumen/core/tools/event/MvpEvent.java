package com.hartmann.lumen.core.tools.event;

import java.io.Serializable;

/**
 * Interface for a MVP event.
 * 
 * @author Jonas Hartmann &lt;jonasharty@gmail.com&gt;
 * @since 30.11.2012
 */
public interface MvpEvent extends Serializable {

	public String getEventName();
}
