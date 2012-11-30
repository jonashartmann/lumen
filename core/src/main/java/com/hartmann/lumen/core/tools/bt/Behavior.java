package com.hartmann.lumen.core.tools.bt;

/**
 * Base class for actions, conditions and composites.
 * 
 * @author Jonas Hartmann
 * 
 */
public abstract class Behavior {

    Status m_eStatus;
    BehaviorObserverDelegate m_Observer;

    public Behavior() {
	m_eStatus = Status.BH_INVALID;
	m_Observer = new BehaviorObserverDelegate();
    }

    /**
     * Update function which is called at every frame.
     * 
     * @return the status of this behavior after execution.
     */
    public abstract Status update();

    /**
     * Called only once before the {@link #update()} is called.
     */
    public abstract void onInitialize();

    /**
     * Called once the update is finished.
     * 
     * @param status
     *            - the status which was returned by the {@link #update()}
     *            function.
     */
    public abstract void onTerminate(Status status);

    /**
     * Helper wrapper function, which helps to make sure that we call our
     * functions correctly.
     * 
     * @return Returns the status after execution.
     */
    public Status tick() {
	if (m_eStatus == Status.BH_INVALID) {
	    onInitialize();
	}

	m_eStatus = update();

	if (m_eStatus != Status.BH_RUNNING) {
	    onTerminate(m_eStatus);
	}
	return m_eStatus;
    }
}
