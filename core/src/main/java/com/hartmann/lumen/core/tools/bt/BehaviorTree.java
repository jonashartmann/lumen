package com.hartmann.lumen.core.tools.bt;

import java.util.ArrayDeque;
import java.util.Deque;

import playn.core.Asserts;

public class BehaviorTree {

	// TODO: create a better end marker
	private static Behavior END_MARKER = new Behavior() {
		@Override
		public Status update() {
			return null;
		}

		@Override
		public void onTerminate(Status status) {
		}

		@Override
		public void onInitialize() {
		}
	};

	private Deque<Behavior> m_Behaviors;

	public BehaviorTree() {
		m_Behaviors = new ArrayDeque<Behavior>();
	}

	/**
	 * Insert active behavior.
	 * 
	 * @param bh
	 *            - active behavior
	 */
	public void insert(Behavior bh) {
		insert(bh, null);
	}

	/**
	 * Insert active behavior.
	 * 
	 * @param bh
	 *            - active behavior
	 * @param observer
	 *            - observer factory
	 */
	public void insert(Behavior bh, BehaviorObserverDelegate observer) {
		if (observer != null) {
			bh.m_Observer = observer;
		}
		m_Behaviors.addFirst(bh);
	}

	public void terminate(Behavior bh, Status result) {
		Asserts.check(result != Status.BH_RUNNING);
		bh.m_eStatus = result;

		if (!bh.m_Observer.empty()) {
			bh.m_Observer.notifyObservers();
		}
	}

	/**
	 * Main entry-point function. <br>
	 * Loop through all behaviors.
	 */
	public void tick() {
		// Insert an end-of-update marker into the list of tasks.
		m_Behaviors.addLast(END_MARKER);

		while (step()) {
			continue;
		}
	}

	/**
	 * Single stepping, where the magic happens.
	 * 
	 * @return
	 */
	public boolean step() {
		// Get the first element of the deque and remove it.
		Behavior current = m_Behaviors.pollFirst();

		// If this is the end-of-update marker, stop processing.
		if (current.equals(END_MARKER)) {
			return false;
		}

		// Perform the update on this individual task.
		// if (current.m_eStatus != Status.BH_SUSPENDED) {
		current.tick();
		// }

		// Process the observer if the task is terminated.
		if (current.m_eStatus != Status.BH_RUNNING
				&& !current.m_Observer.empty()) {
			// Call the observer to notify the parent
			current.m_Observer.notifyObservers();
		}
		// Otherwise drop it into the queue for the next tick().
		else {
			m_Behaviors.addLast(current);
		}
		return true;
	}
}
