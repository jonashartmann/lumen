package com.hartmann.lumen.core.tools.bt;

import java.util.Iterator;

import playn.core.Asserts;

import com.hartmann.lumen.core.tools.bt.BehaviorObserverDelegate.BehaviorObserver;

public abstract class Selector extends Composite {

    protected final BehaviorTree m_pBehaviorTree;
    protected Iterator<Behavior> m_Current;
    protected Behavior currentBehavior;

    public Selector(BehaviorTree bt) {
	super();
	m_Current = null;
	currentBehavior = null;
	this.m_pBehaviorTree = bt;
    }

    @Override
    public void onInitialize() {
	m_Current = m_Children.iterator();
	BehaviorObserverDelegate observer = BehaviorObserverDelegate
		.register(new BehaviorObserver() {

		    @Override
		    public void onComplete() {
			onChildComplete();
		    }
		});
	currentBehavior = m_Current.next();
	m_pBehaviorTree.insert(currentBehavior, observer);
    }

    private final void onChildComplete() {
	Behavior child = currentBehavior;
	if (child.m_eStatus == Status.BH_SUCCESS) {
	    m_pBehaviorTree.terminate(this, Status.BH_SUCCESS);
	}

	Asserts.check(child.m_eStatus == Status.BH_FAILURE);
	if (!m_Current.hasNext()) {
	    m_pBehaviorTree.terminate(this, Status.BH_FAILURE);
	} else {
	    currentBehavior = m_Current.next();
	    BehaviorObserverDelegate observer = BehaviorObserverDelegate
		    .register(new BehaviorObserver() {

			@Override
			public void onComplete() {
			    onChildComplete();
			}
		    });
	    m_pBehaviorTree.insert(currentBehavior, observer);
	}
    }

}
