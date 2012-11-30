package com.hartmann.lumen.core.tools.bt;

/**
 * Status for the Behaviors.
 * 
 * @author Jonas Hartmann
 * 
 */
public enum Status {
    /**
     * Not initialized.
     */
    BH_INVALID,
    /**
     * The behavior succeeded.
     */
    BH_SUCCESS,
    /**
     * The behavior failed.
     */
    BH_FAILURE,
    /**
     * Is currently running.
     */
    BH_RUNNING,
    /**
     * Is waiting for something to happen.
     */
    BH_SUSPENDED;
}
